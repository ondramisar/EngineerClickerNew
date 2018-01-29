package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.graphics.Color
import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.fragments.UserMachineDetailFragment
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity
import com.companybest.ondra.engineerclickernew.models.Machine
import io.realm.RealmModel
import kotlinx.android.synthetic.main.user_machine_item.view.*

class UserMachinesViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val mach = data as Machine
        itemView.user_machine_name.text = mach.name
        itemView.user_machine_lvl.text = mach.lvl.toString()
        itemView.user_machine_time.text = mach.timeToReach.toString()
        itemView.setOnClickListener({
            val activity = itemView.context as MainContainerActivity
            activity.startFragmentTransactionWithBackStack(UserMachineDetailFragment.newInstance(mach.id))

        })
        if (mach.worker != null)
            itemView.user_machine_worker_img.setBackgroundColor(Color.parseColor("#000000"))
        else
            itemView.user_machine_worker_img.setBackgroundColor(Color.parseColor("#dddddd"))
    }
}