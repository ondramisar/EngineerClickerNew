package com.companybest.ondra.engineerclickernew.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.viewholders.DefaultMachineViewHolder
import com.companybest.ondra.engineerclickernew.adapters.viewholders.MaterialViewHolder
import com.companybest.ondra.engineerclickernew.adapters.viewholders.UserMachinesViewHolder
import com.companybest.ondra.engineerclickernew.adapters.viewholders.WorkersViewHolder
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.DefaultMachine
import com.companybest.ondra.engineerclickernew.models.Machine
import com.companybest.ondra.engineerclickernew.models.Material
import com.companybest.ondra.engineerclickernew.models.Worker
import io.realm.RealmList
import io.realm.RealmModel


class BasicAdapterForAll(data: RealmList<RealmModel>) : RecyclerView.Adapter<GenericViewHolder>() {
    private val MACHINE_TYPE = 0
    private val DEFAULT_MACHINE_TYPE = 1
    private val WORKERS_TYPE = 2
    private val MARKET_TYPE = 3

    var mData: RealmList<RealmModel>? = null

    init {
        mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        var viewholder: GenericViewHolder? = null
        when (viewType) {
            MACHINE_TYPE -> viewholder = UserMachinesViewHolder(inflater.inflate(R.layout.user_machine_item, parent, false))
            DEFAULT_MACHINE_TYPE -> viewholder = DefaultMachineViewHolder(inflater.inflate(R.layout.default_machine_item, parent, false))
            WORKERS_TYPE -> viewholder = WorkersViewHolder(inflater.inflate(R.layout.worker_item, parent, false))
            MARKET_TYPE -> viewholder = MaterialViewHolder(inflater.inflate(R.layout.material_item, parent, false))
        }
        return viewholder!!
    }

    override fun onBindViewHolder(holder: GenericViewHolder?, position: Int) {
        holder?.onBindViewHolder(position, mData?.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (mData?.get(position) is Machine) {
            return MACHINE_TYPE
        } else if (mData?.get(position) is DefaultMachine) {
            return DEFAULT_MACHINE_TYPE
        } else if (mData?.get(position) is Worker){
            return WORKERS_TYPE
        }else if (mData?.get(position) is Material) {
            return MARKET_TYPE
        }
        return 0
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

}