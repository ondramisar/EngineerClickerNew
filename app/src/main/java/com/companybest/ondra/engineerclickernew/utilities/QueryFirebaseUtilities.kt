package com.companybest.ondra.engineerclickernew.utilities

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


fun getBaseRef(): FirebaseFirestore {
    return FirebaseFirestore.getInstance()
}

fun getUserDocumentReferenc():DocumentReference{
    return getBaseRef().collection(getUsersPath()).document(getFirebaseUser()!!.uid)
}

fun getFirebaseUser(): FirebaseUser? {
    return getFirebaseAuth().currentUser
}

fun getFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}

fun getMachineCalbacks(): String {
    return "MachineCallBack"
}

fun getMaterialsCalbacks(): String {
    return "MaterialCallBack"
}

fun getUserCalbacks(): String {
    return "UserCallBack"
}

fun getUserMaterialMachineParsingCallbacks(): String {
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

