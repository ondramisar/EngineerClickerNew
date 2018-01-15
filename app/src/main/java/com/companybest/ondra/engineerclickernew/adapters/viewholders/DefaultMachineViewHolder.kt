package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.DefaultMachine
import io.realm.RealmModel
import kotlinx.android.synthetic.main.user_machine_item.view.*

class DefaultMachineViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val mach = data as DefaultMachine
        itemView.user_machine_name.text = mach.name
    }
}