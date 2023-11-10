package com.westee.quartz.config;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class QuartzConfig {
    DataSource dataSource;

    public QuartzConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobFactory jobFactory() {
        return new SpringBeanJobFactory();
    }

    @Bean
    public Scheduler scheduler() throws Exception {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setApplicationContext(applicationContext);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.afterPropertiesSet();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");

        properties.setProperty("org.quartz.jobStore.dataSource", "love");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.dataSource.love.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("org.quartz.dataSource.love.user", "root");
        properties.setProperty("org.quartz.dataSource.love.password", "root");
        properties.setProperty("org.quartz.dataSource.love.URL", "jdbc:mysql://localhost:9999/quartz");
        properties.setProperty("org.quartz.dataSource.love.provider", "hikaricp");
        return properties;
    }


}
