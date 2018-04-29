package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.fragments.UserMachineDetailFragment
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity
import com.companybest.ondra.engineerclickernew.models.UserMachine
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmObject
import kotlinx.android.synthetic.main.user_machine_item.view.*


class UserMachinesViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val mach = data as UserMachine
        itemView.user_machine_name.text = mach.name
        itemView.user_machine_lvl.text = "LVL. " + mach.lvl.toString()
        itemView.user_machine_time.text = "TIME: " + mach.timeToReach.toString()
        itemView.user_machine_detail_btn.setOnClickListener({
            val activity = itemView.context as MainContainerActivity
            activity.startFragmentTransactionWithBackStack(UserMachineDetailFragment.newInstance(mach.id))
        })
        if (mach.worker != null)
            itemView.user_machine_worker_img.setBackgroundColor(Color.parseColor("#000000"))
        else
            itemView.user_machine_worker_img.setBackgroundColor(Color.parseColor("#dddddd"))

        RealmObject.addChangeListener(mach, RealmChangeListener {
            var i = 0
            itemView.user_machine_progress_bar.progress = i
            itemView.user_machine_progress_bar.max = mach.timeToReach * 1000
            val mCountDownTimer = object : CountDownTimer((mach.timeToReach * 1000).toLong(), 1) {

                override fun onTick(millisUntilFinished: Long) {
                    Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished)
                    i++
                    itemView.user_machine_progress_bar.progress = i * 100 / (5000 / 1000)

                }

                override fun onFinish() {
                    i++
                    itemView.user_machine_progress_bar.progress = 100
                }
            }
            mCountDownTimer.start()
        })
    }
}