package com.smilehacker.anonymousproxy.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.smilehacker.anonymousproxy.AnonymousProxy

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val proxy = AnonymousProxy.create(ITest::class.java)
        Log.i(TAG, "result=" + proxy.get()?.foo())

        try {
            Class.forName("java.util.Optional")
            Log.i(TAG, "java8")
        } catch (ignored: ClassNotFoundException) {
            Log.i(TAG, "java7")
        }

    }

    interface ITest {
        fun foo() : Int {
            return 2
        }
    }
}
