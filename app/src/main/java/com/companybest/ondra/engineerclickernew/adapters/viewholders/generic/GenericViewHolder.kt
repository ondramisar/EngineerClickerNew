package com.companybest.ondra.engineerclickernew.adapters.viewholders.generic

import android.support.v7.widget.RecyclerView
import android.view.View
import io.realm.RealmModel

abstract class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBindViewHolder(position: Int, data: RealmModel?)
}