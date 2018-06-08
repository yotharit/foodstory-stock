package me.developer.yotharit.foodstorystock

import android.app.Application
import me.developer.yotharit.foodstorystock.manager.Contextor

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Contextor.initInstance(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}