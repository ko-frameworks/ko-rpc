package com.github.wordfeng.korpc.core.consumer;

import com.github.wordfeng.korpc.core.annotation.RpcConsumer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;

public class ConsumerBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private final Map<String, Object> stub = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void start() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            List<Field> fields = findAnnotatedField(bean.getClass());
            fields.forEach(field -> {
                try {
                    Class<?> service = field.getType();
                    String serviceName = service.getCanonicalName();
                    Object consumer = stub.get(serviceName);
                    if (consumer == null) {
                        consumer = createConsumer(service);
                        stub.put(serviceName, consumer);
                    }
                    field.setAccessible(true);
                    field.set(bean, consumer);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Object createConsumer(Class<?> service) {
        return Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new RpcInvocationHandler(service));
    }

    private List<Field> findAnnotatedField(Class<?> aClass) {
        List<Field> fields = new ArrayList<>();
        do {
            Field[] declaredFields = aClass.getDeclaredFields();
            List<Field> collect = Arrays.stream(declaredFields)
                    .filter(declaredField -> declaredField.isAnnotationPresent(RpcConsumer.class))
                    .toList();
            fields.addAll(collect);
        }
        while ((aClass = aClass.getSuperclass()) != null);
        return fields;
    }

}
