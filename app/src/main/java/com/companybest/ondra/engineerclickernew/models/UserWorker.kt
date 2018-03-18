package com.companybest.ondra.engineerclickernew.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class UserWorker : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var timeCutBy: Float = 0f
    var materialMultiplayer: Int = 0
    var payment: Int = 0
    var isOnMachine: Boolean = false
    var lvl: Int = 0
    var userId: String = ""
}