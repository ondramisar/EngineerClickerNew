package com.companybest.ondra.engineerclickernew.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Material : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var value: Int = 0
    var numberOf: Int = 0
}