package com.companybest.ondra.engineerclickernew.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.models.Machine
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
        }

        return view
    }
}