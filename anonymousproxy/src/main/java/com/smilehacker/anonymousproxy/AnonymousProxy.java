package com.smilehacker.anonymousproxy;


import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Created by zhouquan on 17/7/19.
 */

public class AnonymousProxy<T> {

    private static ArrayMap<Class, Object> mProxyMap = new ArrayMap<>();

    private boolean mIsTryCreated;

    private T mProxy;
    private T mObject;
    private Class<T> mClazz;

    /**
     * get proxy object
     * @return
     */
    @Nullable
    public T get() {
        if (mObject != null) {
            return mObject;
        }

        if (mProxy == null && !mIsTryCreated) {
            mProxy = generateDefaultViewer(mClazz);
            mIsTryCreated = true;
        }

        return mProxy;
    }

    /**
     * set proxy object
     * @param object
     * @return
     */
    public AnonymousProxy<T> set(T object) {
        mObject = object;
        return this;
    }

    /**
     * Is proxy object has actual object
     * @return
     */
    public boolean isPresent() {
        return mObject != null;
    }

    public static void clear() {
        mProxyMap.clear();
    }

    public AnonymousProxy(Class<T> clazz) {
        mClazz = clazz;
    }


    @SuppressWarnings("unchecked")
    private T generateDefaultViewer(Class clazz) {
        if (clazz == null) {
            return null;
        }

        Object fake = mProxyMap.get(clazz);
        if (fake == null) {
            fake = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getDeclaringClass() == Object.class) {
                        return method.invoke(this, args);
                    }

                    Type returnType = method.getGenericReturnType();

                    if (returnType.equals(int.class) || returnType.equals(float.class)
                            || returnType.equals(short.class) || returnType.equals(long.class)
                            || returnType.equals(byte.class) || returnType.equals(double.class)) {
                        return 0;
                    } else if (returnType.equals(boolean.class)) {
                        return false;
                    } else if (returnType.equals(char.class)) {
                        return '0';
                    }

                    return null;
                }
            });
            mProxyMap.put(clazz, fake);
        }

        return (T) fake;
    }
}
