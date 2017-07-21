package com.smilehacker.anonymousproxy.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smilehacker.anonymousproxy.AnonymousProxy
import com.smilehacker.library_kotlin.AnonymousProxyCreate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val proxy = AnonymousProxy(ITest::class.java)
        AnonymousProxyCreate<ITest>().isPresent
    }

    interface ITest {
        fun foo()
    }
}

