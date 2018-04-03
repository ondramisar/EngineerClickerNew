package com.companybest.ondra.engineerclickernew.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class UserWorker : RealmObject() {
    @PrimaryKey
    @JsonProperty("ID")
    var id: String = ""

    @JsonProperty("Name")
    var name: String = ""

    @JsonProperty("TimeCutBy")
    var timeCutBy: Float = 0f

    @JsonProperty("MaterialMultiplayer")
    var materialMultiplayer: Int = 0

    @JsonProperty("Payment")
    var payment: Int = 0

    @JsonProperty("IsOnMachine")
    var isOnMachine: Boolean = false

    @JsonProperty("Lvl")
    var lvl: Int = 0

    @JsonProperty("IdUser")
    var idUser: String = ""
}