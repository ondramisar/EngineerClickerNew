package com.companybest.ondra.engineerclickernew.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.BasicAdapterForAll
import com.companybest.ondra.engineerclickernew.models.User
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import kotlinx.android.synthetic.main.market_fragment.view.*


class MarketFragment : Fragment() {

    companion object {
        fun newInstance(): MarketFragment {
            val bundle = Bundle()
            val fragment = MarketFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.market_fragment, container, false)
        val realm = Realm.getDefaultInstance()

        val mAuth = FirebaseAuth.getInstance()
        val userFire = mAuth.currentUser
        val user = realm.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
        if (user != null) {
            val dataRealm = RealmList<RealmModel>()
            dataRealm.addAll(user.materials)
            val adapter = BasicAdapterForAll(dataRealm)
            val linearManaget = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            view.market_materials.adapter = adapter
            view.market_materials.layoutManager = linearManaget
           /* view.refresh.setOnClickListener({
                val user = realm.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
                dataRealm.clear()
                dataRealm.addAll(user?.materials!!)
                val adapter = BasicAdapterForAll(dataRealm)
                view.market_materials.adapter = adapter
            })*/
        }
        return view
    }
}