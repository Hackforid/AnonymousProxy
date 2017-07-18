package com.smilehacker.anonymousproxy.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smilehacker.anonymousproxy.AnonymousProxy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val proxy = AnonymousProxy.create(ITest::class.java)
        proxy.get()
    }

    interface ITest {
        fun foo()
    }
}
