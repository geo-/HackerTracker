package com.shortstack.hackertracker.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shortstack.hackertracker.Application.App
import com.shortstack.hackertracker.Event.SetupDatabaseEvent
import com.shortstack.hackertracker.R
import com.shortstack.hackertracker.Service.UpdateDatabaseService
import com.squareup.otto.Subscribe
import java.util.*


class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 500L
    private var isComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        App.application.registerBusListener(this)

        startService(Intent(this@SplashActivity, UpdateDatabaseService::class.java))

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startHomeActivity()
            }
        }, SPLASH_DELAY)

    }

    override fun onDestroy() {
        super.onDestroy()
        App.application.unregisterBusListener(this)
    }



    private fun startHomeActivity() {
        if( !isComplete ) {
            isComplete = true
            return
        }

        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }


    @Subscribe
    public fun handleDatabaseSetup(event: SetupDatabaseEvent) {
        startHomeActivity()
    }
}
