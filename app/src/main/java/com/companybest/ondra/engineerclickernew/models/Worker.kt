package com.companybest.ondra.engineerclickernew.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Worker : RealmObject() {
    @PrimaryKey
    var id:String = ""
    var name:String = ""
}