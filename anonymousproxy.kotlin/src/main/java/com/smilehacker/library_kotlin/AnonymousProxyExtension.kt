package com.smilehacker.library_kotlin

import com.smilehacker.anonymousproxy.AnonymousProxy

/**
 * Created by zhouquan on 17/7/21.
 */

inline fun <reified T> AnonymousProxyCreate() : AnonymousProxy<T> {
    return AnonymousProxy(T::class.java)
}

