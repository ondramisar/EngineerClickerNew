package com.companybest.ondra.engineerclickernew.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.viewholders.*
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.*
import com.companybest.ondra.engineerclickernew.utilities.OnClick
import io.realm.RealmList
import io.realm.RealmModel


class BasicAdapterForAll(data: RealmList<RealmModel>) : RecyclerView.Adapter<GenericViewHolder>() {
    private val MACHINE_TYPE = 0
    private val DEFAULT_MACHINE_TYPE = 1
    private val USER_WORKERS_TYPE = 2
    private val DEFAULT_WORKERS_TYPE = 3
    private val MARKET_TYPE = 4

    private var callback: OnClick? = null

    private var mData: RealmList<RealmModel>? = null

    init {
        mData = data
    }

    constructor(data: RealmList<RealmModel>, callback: OnClick) : this(data) {
        //TODO thing of better way to do this
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        var viewholder: GenericViewHolder? = null
        when (viewType) {
            MACHINE_TYPE -> viewholder = UserMachinesViewHolder(inflater.inflate(R.layout.user_machine_item, parent, false))
            DEFAULT_MACHINE_TYPE -> viewholder = DefaultMachineViewHolder(inflater.inflate(R.layout.default_machine_item, parent, false))
            USER_WORKERS_TYPE -> viewholder = WorkersMachineViewHolder(inflater.inflate(R.layout.worker_machine_item, parent, false), callback!!)
            DEFAULT_WORKERS_TYPE -> viewholder = WorkersViewHolder(inflater.inflate(R.layout.worker_item, parent, false))
            MARKET_TYPE -> viewholder = MaterialViewHolder(inflater.inflate(R.layout.material_item, parent, false))
        }
        return viewholder!!
    }

    override fun onBindViewHolder(holder: GenericViewHolder?, position: Int) {
        holder?.onBindViewHolder(position, mData?.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            mData?.get(position) is UserMachine -> MACHINE_TYPE
            mData?.get(position) is DefaultMachine -> DEFAULT_MACHINE_TYPE
            mData?.get(position) is UserWorker -> USER_WORKERS_TYPE
            mData?.get(position) is DefaultMaterial -> MARKET_TYPE
            mData?.get(position) is DefaultWorker -> DEFAULT_WORKERS_TYPE
            else -> 5
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

}