package com.companybest.ondra.engineerclickernew

import android.support.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration



open class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .name("realm")
                .build()
        Realm.setDefaultConfiguration(configuration)
    }
}