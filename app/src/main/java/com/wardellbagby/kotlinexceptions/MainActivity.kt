package com.wardellbagby.kotlinexceptions

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logged_exception_button.setOnClickListener {
            val handler = CoroutineExceptionHandler { _, ex ->
                throw ex
            }
            launch(CommonPool + handler) {
                throw Exception("I'm an exception and I was (sorta) correctly handled by Android's UncaughtExceptionPreHandler. :)")
            }
        }

        unlogged_exception_button.setOnClickListener {
            launch(CommonPool) {
                throw Exception("I'm an exception and I was not correctly handled by Android's UncaughtExceptionPreHandler. :(")
            }
        }
    }
}
