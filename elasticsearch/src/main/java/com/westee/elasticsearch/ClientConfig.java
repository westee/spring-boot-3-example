package com.westee.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ClientConfig {
    @Value("${es.hosts}")
    private String hosts;
    @Value("${es.name:elastic}")
    private String name;
    @Value("${es.password}")
    private String password;

    @Bean
    public ElasticsearchClient docqaElasticsearchClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(name, password));
        List<HttpHost> httpHosts = new ArrayList<>();
        String[] split = hosts.split(",");
        for (String s : split) {
            httpHosts.add(HttpHost.create(s));
        }
        HttpHost[] httpHosts1 = httpHosts.toArray(new HttpHost[0]);
        SSLContext sslContext;
        try {
            sslContext = readCrtFile("src/main/resources/elastic.crt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RestClient client = RestClient
                .builder(httpHosts1)
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setSSLContext(sslContext).setDefaultCredentialsProvider(credentialsProvider).setKeepAliveStrategy((response, context) -> 180 * 1000))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    public static SSLContext readCrtFile(String crtFilePath) throws Exception {
        // 读取crt文件
        InputStream inputStream = new FileInputStream(crtFilePath);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);

        // 创建KeyStore并将crt文件添加到KeyStore中
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("cert", certificate);

        // 创建TrustManagerFactory并初始化KeyStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // 创建KeyManagerFactory并初始化KeyStore
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, null);

        // 创建SSLContext并初始化TrustManager和KeyManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext;
    }

}
