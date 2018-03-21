package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.util.Log
import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.DefaultMachine
import com.companybest.ondra.engineerclickernew.models.DefaultMaterial
import com.companybest.ondra.engineerclickernew.models.User
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmModel
import kotlinx.android.synthetic.main.default_machine_item.view.*

class DefaultMachineViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val mach = data as DefaultMachine
        itemView.default_machine_name.text = mach.name
        val mat = Realm.getDefaultInstance().where(DefaultMaterial::class.java).equalTo("id", mach.idMaterialToGive).findFirst()
        itemView.default_machine_material.text = "DEFAULT_MATERIAL: " + mat?.name
        itemView.default_machine_time.text = "TIME: " + mach.timeToReach
        itemView.default_machine_cost.text = "COST: " + mach.cost.toString()
        itemView.default_machine_buy_btn.setOnClickListener({
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