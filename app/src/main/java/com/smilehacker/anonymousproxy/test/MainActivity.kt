package com.smilehacker.anonymousproxy.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smilehacker.anonymousproxy.AnonymousProxy
import com.smilehacker.library_kotlin.AnonymousProxyCreate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val proxy = AnonymousProxy.create(ITest::class.java)
        val proxy1 = AnonymousProxyCreate<ITest>()
        proxy1.get()

    }

    interface ITest {
        fun foo()
    }

}

inline fun <reified T: Any> foo() {
    AnonymousProxy.create(T::class.java)
}
