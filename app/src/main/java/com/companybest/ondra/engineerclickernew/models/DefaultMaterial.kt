package com.companybest.ondra.engineerclickernew.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DefaultMaterial : RealmObject() {
    @PrimaryKey
    @JsonProperty("ID")
    var id: String = ""

    @JsonProperty("Value")
    var value: Int = 0

    @JsonProperty("Name")
    var name:String = ""
}