package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.Machine
import io.realm.RealmModel
import kotlinx.android.synthetic.main.user_machine_item.view.*

class UserMachinesViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val mach = data as Machine
        itemView.user_machine_name.text = mach.name
        itemView.user_machine_lvl.text = mach.lvl.toString()
        itemView.user_machine_time.text = mach.timeToReach.toString()

    }
}