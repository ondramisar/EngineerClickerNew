package com.companybest.ondra.engineerclickernew.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DefaultMachine : RealmObject() {
    @PrimaryKey
    @JsonProperty("ID")
    var id: String? = null

    @JsonProperty("Name")
    var name: String? = null

    @JsonProperty("Cost")
    var cost: Int = 0

    @JsonProperty("TimeToReach")
    var timeToReach: Int = 0

    @JsonProperty("IdMaterialToGive")
    var idMaterialToGive: String = ""

    @JsonProperty("NumberOfMaterialsToGive")
    var numberOfMaterialsToGive: Int = 0
}