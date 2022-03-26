package com.example.passwordmanager

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // `startKoin` でKoinを開始します。
        startKoin {
            // Contextを宣言し、
            androidContext(this@MainApplication)
            // 先ほど宣言したmoduleを指定します。
            modules(listOf(
                singleModules,
                factoryModules
            ))
        }
    }
}