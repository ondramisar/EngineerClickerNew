package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.Worker
import com.companybest.ondra.engineerclickernew.utilities.OnClick
import io.realm.RealmModel
import kotlinx.android.synthetic.main.worker_item.view.*


class WorkersMachineViewHolder(itemView: View, val callback: OnClick) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val worker = data as Worker
        itemView.worker_name.text = worker.name
        itemView.worker_pay.text = "DAY PAYMENT" + worker.payment.toString()
        itemView.worker_time.text = "TIME SHORTEN BY: " + worker.timeCutBy.toString() + " %"
        itemView.worker_material.text = "MATERIAL MULTIPLY BY: " + worker.materialMultiplayer.toString()
        itemView.setOnClickListener({
            callback.onClick(worker)
        })
        itemView.worker_buy.visibility = View.GONE
    }
}