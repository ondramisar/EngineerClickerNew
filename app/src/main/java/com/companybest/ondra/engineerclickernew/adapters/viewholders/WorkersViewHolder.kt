package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.util.Log
import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.Worker
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import io.realm.RealmModel
import kotlinx.android.synthetic.main.worker_item.view.*


class WorkersViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val worker = data as Worker
        itemView.worker_name.text = worker.name
        itemView.setOnClickListener({
            val network = NetworkClient()
            network.addWorker(worker)
            Log.i("usern", "worker added")
        })
    }
}