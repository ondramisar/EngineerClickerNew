package com.companybest.ondra.engineerclickernew.utilities

import com.google.firebase.firestore.FirebaseFirestore


fun getBaseRef(): FirebaseFirestore {
    return FirebaseFirestore.getInstance()
}

fun getDefaultParsingCalbacks(): String {
    return "CallBack"
}

fun getDefaultMachinePath(): String {
    return "DefaultMachines"
}

fun getUsersMachinePath(): String {
    return "UsersMachines"
}
fun getUsersPath(): String {
    return "users"
}

fun getMaterialPath(): String {
    return "Materials"
}

