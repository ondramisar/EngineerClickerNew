package com.companybest.ondra.engineerclickernew.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserMachine : RealmObject() {
    @PrimaryKey
    @JsonProperty("ID")
    var id: String = ""

    @JsonProperty("Name")
    var name: String? = null

    var timeBeffore: Long = 0

    @JsonProperty("TimeToReach")
    var timeToReach: Int = 0

    @JsonProperty("IdMaterialToGive")
    var idMaterialToGive: String = ""

    @JsonProperty("NumberOfMaterialsToGive")
    var numberOfMaterialsToGive: Int = 0

    @JsonProperty("Lvl")
    var lvl: Int = 0

    @JsonProperty("Worker")
    var worker: UserWorker? = null

    @JsonProperty("WorkerId")
    var workerId: String = ""

    @JsonProperty("IdUser")
    var idUser: String = ""
}