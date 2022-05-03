package com.ducdiep.bookmarket.ui.splash

import android.content.Intent
import android.os.Bundle
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseActivity
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import kotlinx.coroutines.*

class SplashActivity:BaseActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            delay(2000)
            withContext(Dispatchers.Main){
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            }
        }
    }
}