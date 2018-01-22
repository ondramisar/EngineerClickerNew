package com.companybest.ondra.engineerclickernew.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity
import kotlinx.android.synthetic.main.shop_fragment.view.*


class ShopFragment: Fragment() {

    companion object {
        fun newInstance(): ShopFragment {
            val bundle = Bundle()
            val fragment = ShopFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.shop_fragment, container, false)

        view.shop_machines_default_buy.setOnClickListener({
            val ac = activity as MainContainerActivity
            ac.startFragmentTransactionWithBackStack(MachineFragment.newInstance(MachineFragment.defaultMachineType))
        })

        view.shop_workers_buy.setOnClickListener({
            val ac = activity as MainContainerActivity
            ac.startFragmentTransactionWithBackStack(WorkersBuyFragment.newInstance())
        })

        return view
    }
}