package com.smilehacker.anonymousproxy;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by zhouquan on 17/7/21.
 */

public abstract class JDKInvoker {

    private static boolean isJava8() {
        try {
            Class.forName("java.util.Optional");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static JDKInvoker mJDKInvoker = chooseInvoker();

    private static JDKInvoker chooseInvoker() {
        if (isJava8()) {
            return new Java8Invoker();
        } else {
            return new Java7Invoker();
        }
    }


    public static JDKInvoker get() {
        return mJDKInvoker;
    }


    protected Object invokeFakeMethod(Method method) {
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

    public abstract Object invokeMethod(Method method, Class<?> clazz, Object proxy, Object[] args);

    static class Java7Invoker extends JDKInvoker {

        @Override
        public Object invokeMethod(Method method, Class<?> clazz, Object proxy, Object[] args) {
            return invokeFakeMethod(method);
        }
    }

    static class Java8Invoker extends JDKInvoker {


        private Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, Object... args) throws Throwable {
            // Because the service interface might not be public, we need to use a MethodHandle lookup
            // that ignores the visibility of the declaringClass.
            Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class, int.class);
            constructor.setAccessible(true);
            return constructor.newInstance(declaringClass, -1 /* trusted */)
                    .unreflectSpecial(method, declaringClass)
                    .bindTo(object)
                    .invokeWithArguments(args);
        }

        @Override
        public Object invokeMethod(Method method, Class<?> clazz, Object proxy, Object[] args) {
            if (isDefault(method)) {
                try {
                    return invokeDefaultMethod(method, clazz, proxy, args);
                } catch (Throwable throwable) {
                    return invokeFakeMethod(method);
                }
            } else {
                return invokeFakeMethod(method);
            }
        }

        private boolean isDefault(Method method) {
            return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) ==
                    Modifier.PUBLIC) &&
                    method.getDeclaringClass().isInterface();
        }
    }

}
