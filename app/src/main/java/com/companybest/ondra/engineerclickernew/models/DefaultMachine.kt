package com.companybest.ondra.engineerclickernew.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DefaultMachine : RealmObject() {
    @PrimaryKey
    var id: String? = null
    var name: String? = null
    var cost: Int = 0
    var timeToReach: Int = 0
    var idMaterialToGive: String = ""
    var numberOfMaterialsToGive: Int = 0
}