package com.example.android.sample.myownfrashcard

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Realmの初期化
        Realm.init(this)

        var config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

        // TODO 未解決
//        val folderPath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Realm")
//        folderPath.mkdirs()

//        RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
//        Realm.setDefaultConfiguration(config)
    }
}