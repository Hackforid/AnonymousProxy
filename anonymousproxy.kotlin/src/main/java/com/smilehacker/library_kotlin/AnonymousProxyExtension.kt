package com.smilehacker.library_kotlin

import com.smilehacker.anonymousproxy.AnonymousProxy

/**
 * Created by zhouquan on 17/7/21.
 */

inline fun <reified T> AnonymousProxyInstance() : AnonymousProxy<T> {
    return AnonymousProxy.create(T::class.java)
}

