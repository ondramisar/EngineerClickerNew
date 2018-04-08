package com.companybest.ondra.engineerclickernew.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.BasicAdapterForAll
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity
import com.companybest.ondra.engineerclickernew.models.DefaultMaterial
import com.companybest.ondra.engineerclickernew.models.User
import com.companybest.ondra.engineerclickernew.models.UserMachine
import com.companybest.ondra.engineerclickernew.models.UserWorker
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
import com.companybest.ondra.engineerclickernew.utilities.OnClick
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import kotlinx.android.synthetic.main.machine_worker_dialog.*
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
        val mach = realm.where(UserMachine::class.java).equalTo("id", id).findFirst()
        if (mach != null) {
            view.user_machine_detail_stats.text = "STATS"
            view.user_machine_detail_lvl.text = "LVL. " + mach.lvl.toString()
            view.user_machine_detail_name.text = mach.name
            view.user_machine_detail_time.text = "time: " + mach.timeToReach.toString()
            val mat = realm.where(DefaultMaterial::class.java).equalTo("id", mach.idMaterialToGive).findFirst()
            view.user_machine_detail_materials.text = "DEFAULT_MATERIAL: " + (mat?.name ?: "none")
            if (mach.worker != null)
                view.user_machine_worker_img.setBackgroundColor(Color.parseColor("#000000"))

            view.user_machine_add_worker.setOnClickListener({
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.machine_worker_dialog)
                dialog.setTitle("DEFAULT_WORKERS")
                val mAuth = FirebaseAuth.getInstance()
                val userFire = mAuth.currentUser
                val user = realm.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
                if (user !=null) {
                    val workers = RealmList<RealmModel>()
                    workers.addAll(user.userWorkers)
                    val adapter = BasicAdapterForAll(workers, OnClick {
                        if (it is UserWorker) {
                            val worker = it
                            view.user_machine_worker_img.setBackgroundColor(Color.parseColor("#000000"))
                            val network = NetworkClient()
                            network.addWorkerToMachine(mach.id, worker.id)
                            dialog.dismiss()
                        }
                    }, "worker")
                    val linearManaget = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    dialog.machine_workers_choice.adapter = adapter
                    dialog.machine_workers_choice.layoutManager = linearManaget
                    dialog.show()
                }
            })

            view.user_machine_confirm.setOnClickListener({
                val ac = activity as MainContainerActivity
                ac.startFragmentTransactionWithBackStack(MachineFragment.newInstance(MachineFragment.userMachineType))

            })

            view.user_machine_remove.setOnClickListener({
                view.user_machine_worker_img.setBackgroundColor(Color.parseColor("#dddddd"))
                val network = NetworkClient()
                network.removeWorkerToMachine(mach.id)
                realm.executeTransaction({
                  //  mach.worker?.isOnMachine = false
                    mach.worker = null
                })
            })
        }

        return view
    }
}