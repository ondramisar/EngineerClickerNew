package com.companybest.ondra.engineerclickernew.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R


class UserMachineDetailFragment : Fragment() {

    companion object {
        fun newInstance(): UserMachineDetailFragment {
            val bundle = Bundle()
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

        return view
    }
}