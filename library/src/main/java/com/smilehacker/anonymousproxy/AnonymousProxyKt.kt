package com.smilehacker.anonymousproxy

import android.support.v4.util.ArrayMap
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by zhouquan on 17/7/18.
 */

class AnonymousProxyKt {

    companion object {
        val anonymousMap = ArrayMap<Class<*>, Any>()
    }


    inline fun <reified T> generateDefaultViewer(): T? {
        val clazz = T::class.java
        var viewer = anonymousMap[clazz]
        if (viewer == null) {
            viewer = Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz), object : InvocationHandler {
                @Throws(Throwable::class)
                override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
                    if (method.declaringClass == Any::class.java) {
                        return method.invoke(this, *args)
                    }

                    val returnType = method.genericReturnType

                    if (returnType == Int::class.javaPrimitiveType || returnType == Float::class.javaPrimitiveType
                            || returnType == Short::class.javaPrimitiveType || returnType == Long::class.javaPrimitiveType
                            || returnType == Byte::class.javaPrimitiveType || returnType == Double::class.javaPrimitiveType) {
                        return 0
                    } else if (returnType == Boolean::class.javaPrimitiveType) {
                        return false
                    } else if (returnType == Char::class.javaPrimitiveType) {
                        return '0'
                    }

                    return null
                }
            })
            anonymousMap.put(clazz, viewer)
        }

        return viewer as T
    }

}
