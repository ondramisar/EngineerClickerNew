package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.User
import com.companybest.ondra.engineerclickernew.models.UserMaterial
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmModel
import kotlinx.android.synthetic.main.material_item.view.*


class MaterialViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val material = data as UserMaterial
        itemView.market_name_material.text = material.name
        itemView.market_number_of.text = material.numberOf.toString()
        itemView.market_sell.setOnClickListener({
            Realm.getDefaultInstance().use {
                val mAuth = FirebaseAuth.getInstance()
                val userFire = mAuth.currentUser
                val user = it.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
                if (user != null) {
                    it.executeTransaction {
                        user.coins = user.coins + material.value
                        material.numberOf = material.numberOf - 1
                        itemView.market_number_of.text = material.numberOf.toString()
                    }
                    val network = NetworkClient()
                    network.updateUser()
                }
            }
        })

    }
}