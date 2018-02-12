package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.Worker
import com.companybest.ondra.engineerclickernew.utilities.OnClick
import io.realm.RealmModel
import kotlinx.android.synthetic.main.worker_machine_item.view.*


class WorkersMachineViewHolder(itemView: View, val callback: OnClick) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val worker = data as Worker
        itemView.worker_machine_name.text = worker.name
        itemView.worker_machine_pay.text = "DAY PAYMENT" + worker.payment.toString()
        itemView.worker_machine_time.text = "TIME SHORTEN BY: " + worker.timeCutBy.toString() + " %"
        itemView.worker_machine_material.text = "MATERIAL MULTIPLY BY: " + worker.materialMultiplayer.toString()
        if (worker.isOnMachine) {
            itemView.worker_machine_add.text = "ALREADY WORKING"
        } else {
            itemView.worker_machine_add.setOnClickListener({
                callback.onClick(worker)
            })
        }
    }
}