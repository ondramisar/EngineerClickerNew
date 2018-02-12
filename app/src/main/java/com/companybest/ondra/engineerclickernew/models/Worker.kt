package com.companybest.ondra.engineerclickernew.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Worker : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var timeCutBy: Float = 0f
    var materialMultiplayer: Int = 0
    var payment: Float = 0f
    var isBought:Boolean = false
    var isOnMachine:Boolean = false
}