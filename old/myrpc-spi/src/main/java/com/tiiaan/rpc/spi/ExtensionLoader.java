package com.tiiaan.rpc.spi;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public final class ExtensionLoader<T> {

    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class<?> type;

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }




    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        if (type.getAnnotation(MySPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @MySPI");
        }
        // firstly get from cache, if not hit, create one
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<S>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }




    /**
     * 获取拓展类的对象
     * @param name 拓展项名称
     * @return E
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    public T getExtension(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Extension name should not be null or empty");
        }
        //从缓存中取出name对应的holder, 如果没有就创建一对name-holder
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        //从holder中取出对象, 如果为null就利用双检锁创建单例对象
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name); //创建
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }


    /**
     * 创建拓展类对象
     * @param name 拓展项名称
     * @return T
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    private T createExtension(String name) {
        //获取所有的拓展类
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("Extension not found " + name);
        }
        T instance = null;
        try {
            instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
        } catch (InstantiationException |IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }


    /**
     * 根据配置文件解析拓展项名称到拓展类的映射关系
     * @return java.util.Map<java.lang.String, java.lang.Class < ?>>
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = new HashMap<>();
                    loadDirectory(classes, ExtensionLoader.SERVICE_DIRECTORY);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }


    private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir) {
        String fileName = dir + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceURL = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceURL) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8));
            try {
                String line;
                //按行读取配置内容
                while ((line = reader.readLine()) != null) {
                    //定位 # 字符
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        //截取 # 之前的字符串，# 之后的内容为注释，需要忽略
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            String clazzName = null;
                            final int ei = line.indexOf('=');
                            if (ei > 0) {
                                //以等于号 = 为界，截取键与值
                                name = line.substring(0, ei).trim();
                                clazzName = line.substring(ei + 1).trim();
                            }
                            if (name != null && clazzName != null) {
                                if (name.length() > 0 && clazzName.length() > 0) {
                                    //加载类，并通过 loadClass 方法对类进行缓存
                                    Class<?> clazz = classLoader.loadClass(clazzName);
                                    extensionClasses.put(name, clazz);
                                }
                            }

                        } catch (Throwable t) {
                            IllegalStateException e = new IllegalStateException("Failed to load extension class...");
                        }
                    }
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            log.error("Exception when load extension class...");
        }
    }

}
