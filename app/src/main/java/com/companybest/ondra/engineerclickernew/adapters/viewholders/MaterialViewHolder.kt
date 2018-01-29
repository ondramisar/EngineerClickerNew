package com.companybest.ondra.engineerclickernew.adapters.viewholders

import android.view.View
import com.companybest.ondra.engineerclickernew.adapters.viewholders.generic.GenericViewHolder
import com.companybest.ondra.engineerclickernew.models.Material
import io.realm.RealmModel
import kotlinx.android.synthetic.main.material_item.view.*


class MaterialViewHolder(itemView: View) : GenericViewHolder(itemView) {

    override fun onBindViewHolder(position: Int, data: RealmModel?) {
        val material = data as Material
        itemView.market_name_material.text = material.name
        itemView.market_number_of.text = material.numberOf.toString()
    }
}