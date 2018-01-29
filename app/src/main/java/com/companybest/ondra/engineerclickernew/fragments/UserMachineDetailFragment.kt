package com.companybest.ondra.engineerclickernew.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity
import com.companybest.ondra.engineerclickernew.models.Machine
import com.companybest.ondra.engineerclickernew.models.Worker
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import io.realm.Realm
import kotlinx.android.synthetic.main.user_machine_detail_fragment.view.*


class UserMachineDetailFragment : Fragment() {

    companion object {
        fun newInstance(idMachine: String): UserMachineDetailFragment {
            val bundle = Bundle()
            bundle.putString("id", idMachine)
            val fragment = UserMachineDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.user_machine_detail_fragment, container, false)
        val id = arguments.getString("id")
        val realm = Realm.getDefaultInstance()
        val mach = realm.where(Machine::class.java).equalTo("id", id).findFirst()
        if (mach != null) {
            view.user_machine_detail_lvl.text = mach.lvl.toString()
            view.user_machine_detail_name.text = mach.name
            view.user_machine_detail_time.text = mach.timeToReach.toString()
            if (mach.worker != null)
                view.user_machine_worker_img.setBackgroundColor(Color.parseColor("#000000"))

            view.user_machine_add_worker.setOnClickListener({
                view.user_machine_worker_img.setBackgroundColor(Color.parseColor("#000000"))
                val worker = realm.where(Worker::class.java).findFirst()
                val network = NetworkClient()
                network.addWorkerToMachine(mach, worker)
                realm.executeTransaction({
                    mach.worker = worker
                })
            })

            view.user_machine_confirm.setOnClickListener({
                val ac = activity as MainContainerActivity
                ac.startFragmentTransactionWithBackStack(MachineFragment.newInstance(MachineFragment.userMachineType))

            })

            view.user_machine_remove.setOnClickListener({
                view.user_machine_worker_img.setBackgroundColor(Color.parseColor("#dddddd"))
                val network = NetworkClient()
                network.removeWorkerToMachine(mach)
                realm.executeTransaction({
                    mach.worker = null
                })
            })
        }

        return view
    }
}