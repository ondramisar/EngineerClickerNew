package com.companybest.ondra.engineerclickernew.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserMachine : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var name: String? = null
    var timeBeffore: Long = 0
    var timeToReach: Int = 0
    var idMaterialToGive: String = ""
    var numberOfMaterialsToGive: Int = 0
    var lvl: Int = 0
    var worker: UserWorker? = null
    var workerId: String = ""
    var idUser: String = ""
}