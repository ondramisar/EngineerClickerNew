package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.util.Log
import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.DefaultWorker
import com.companybest.ondra.engineerclickernew.models.User
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmModel
import kotlinx.android.synthetic.main.worker_item.view.*


class WorkersViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val worker = data as DefaultWorker
        itemView.worker_name.text = worker.name
        itemView.worker_pay.text = "DAY PAYMENT" + worker.payment.toString()
        itemView.worker_time.text = "TIME SHORTEN BY: " + worker.timeCutBy.toString() + " %"
        itemView.worker_material.text = "MATERIAL MULTIPLY BY: " + worker.materialMultiplayer.toString()
    /*    if (worker.isBought){
            itemView.worker_buy.text = "WORKER IS ALREADY EMPLOYED"
        }else {*/
            itemView.worker_buy.setOnClickListener({
                val network = NetworkClient()
                val mAuth = FirebaseAuth.getInstance()
                val userFire = mAuth.currentUser
                val user = Realm.getDefaultInstance().where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
                network.addWorker(worker, user)
                Log.i("usern", "worker added")
            })
      //  }
    }
}