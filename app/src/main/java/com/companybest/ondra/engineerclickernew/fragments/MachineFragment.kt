package com.companybest.ondra.engineerclickernew.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.UserMachinesAdapter
import com.companybest.ondra.engineerclickernew.models.DefaultMachine
import com.companybest.ondra.engineerclickernew.models.User
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import kotlinx.android.synthetic.main.machine_fragment.view.*


class MachineFragment : Fragment() {

    companion object {
        val userMachineType = "userMachine"
        val defaultMachineType = "defaultMachine"
        fun newInstance(type: String): MachineFragment {
            val bundle = Bundle()
            bundle.putString("type", type)
            val fragment = MachineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.machine_fragment, container, false)
        val type = arguments.getString("type")

        val realm = Realm.getDefaultInstance()
        val dataRealm = RealmList<RealmModel>()
        if (type == userMachineType) {
            val mAuth = FirebaseAuth.getInstance()
            val userFire = mAuth.currentUser
            val user = realm.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()

            if (user != null) {
                dataRealm.addAll(user.machines)
            }
        } else if (type == defaultMachineType) {
            val defaultMachine = realm.where(DefaultMachine::class.java).findAll()
            dataRealm.addAll(defaultMachine)
        }

        val adapter = UserMachinesAdapter(dataRealm)
        val linearManaget = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.user_machines_recyclerview.adapter = adapter
        view.user_machines_recyclerview.layoutManager = linearManaget
        return view
    }
}