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

fun getDefaultMachinePath(): String {
    return "DefaultMachines"
}

fun getWorkersPath(): String {
    return "Workers"
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

