package com.companybest.ondra.engineerclickernew.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.adapters.viewholders.DefaultMachineViewHolder
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.viewholders.UserMachinesViewHolder
import com.companybest.ondra.engineerclickernew.models.DefaultMachine
import com.companybest.ondra.engineerclickernew.models.Machine
import io.realm.RealmList
import io.realm.RealmModel


class UserMachinesAdapter(data: RealmList<RealmModel>) : RecyclerView.Adapter<GenericViewHolder>() {
    val MACHINE_TYPE = 0
    val DEFAULT_MACHINE_TYPE = 1

    var mData: RealmList<RealmModel>? = null

    init {
        mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        var viewholder: GenericViewHolder? = null
        when (viewType) {
            MACHINE_TYPE -> viewholder = UserMachinesViewHolder(inflater.inflate(R.layout.user_machine_item, parent, false))
            DEFAULT_MACHINE_TYPE -> viewholder = DefaultMachineViewHolder(inflater.inflate(R.layout.user_machine_item, parent, false))
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
        }
        return 0
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

}