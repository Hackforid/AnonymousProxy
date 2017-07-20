package com.smilehacker.anonymousproxy;


import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhouquan on 17/7/19.
 */

public class AnonymousProxy<T> {

    private static ArrayMap<Class, Object> mProxyMap = new ArrayMap<>();

    private boolean mIsTryCreated;

    private T mProxy;
    private T mObject;
    private Class<T> mClazz;

    @Nullable
    public T get() {
        if (mObject != null) {
            return mObject;
        }

        if (mProxy == null && !mIsTryCreated) {
            mProxy = generateDefaultViewer(mClazz);
        }
        return mProxy;
    }

    public void set(T object) {
        mObject = object;
    }

    public void clear() {
        mObject = null;
    }

    private AnonymousProxy(Class<T> clazz) {
        mClazz = clazz;
    }

    public static <T> AnonymousProxy<T> create(Class<T> clazz) {
        AnonymousProxy<T> inst = new AnonymousProxy<>(clazz);
        return inst;
    }

    private T generateDefaultViewer(final Class clazz) {
        if (clazz == null) {
            return null;
        }

        Object viewer = mProxyMap.get(clazz);
        if (viewer == null) {
            viewer = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getDeclaringClass() == Object.class) {
                        return method.invoke(this, args);
                    }

                    return JDKInvoker.get().invokeMethod(method, clazz, proxy, args);

//                    Type returnType = method.getGenericReturnType();
//
//                    if (returnType.equals(int.class) || returnType.equals(float.class)
//                            || returnType.equals(short.class) || returnType.equals(long.class)
//                            || returnType.equals(byte.class) || returnType.equals(double.class)) {
//                        return 0;
//                    } else if (returnType.equals(boolean.class)) {
//                        return false;
//                    } else if (returnType.equals(char.class)) {
//                        return '0';
//                    }
//
//                    return null;
                }
            });
            mProxyMap.put(clazz, viewer);
        }

        return (T) viewer;
    }
}
