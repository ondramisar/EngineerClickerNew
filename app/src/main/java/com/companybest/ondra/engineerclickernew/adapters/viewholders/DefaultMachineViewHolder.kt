package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.util.Log
import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.DefaultMachine
import com.companybest.ondra.engineerclickernew.models.User
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmModel
import kotlinx.android.synthetic.main.user_machine_item.view.*

class DefaultMachineViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val mach = data as DefaultMachine
        itemView.user_machine_name.text = mach.name
        itemView.setOnClickListener({
            val realm = Realm.getDefaultInstance()
            val mAuth = FirebaseAuth.getInstance()
            val userFire = mAuth.currentUser
            val user = realm.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
            val network = NetworkClient()
            network.addMachine(mach, user)
            Log.i("usern", "machine added")
        })
    }
}