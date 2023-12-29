package com.westee.redis.serializer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

@Slf4j
public class MultiClassLoaderObjectInputStream extends ObjectInputStream {
    public MultiClassLoaderObjectInputStream(InputStream s) throws Exception {
        super(s);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            log.debug("Cannot access thread context ClassLoader!", ex);
        }

        try {
            ClassLoader cl = MultiClassLoaderObjectInputStream.class.getClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            log.debug("Cannot access application ClassLoader", ex);
        }

        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            log.debug("Cannot access system ClassLoader", ex);
        }

        return super.resolveClass(desc);
    }
}
