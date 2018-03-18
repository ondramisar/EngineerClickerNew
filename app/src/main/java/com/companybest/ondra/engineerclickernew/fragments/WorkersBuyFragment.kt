package com.companybest.ondra.engineerclickernew.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.adapters.BasicAdapterForAll
import com.companybest.ondra.engineerclickernew.models.DefaultWorker
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults
import kotlinx.android.synthetic.main.workers_buy_fragment.view.*

class WorkersBuyFragment : Fragment() {

    companion object {
        fun newInstance(): WorkersBuyFragment {
            val bundle = Bundle()
            val fragment = WorkersBuyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.workers_buy_fragment, container, false)
        val realm = Realm.getDefaultInstance()
        val dataRealm = RealmList<RealmModel>()
        val workers: RealmResults<DefaultWorker> = realm.where(DefaultWorker::class.java).findAll()
        dataRealm.addAll(workers)

        val adapter = BasicAdapterForAll(dataRealm)
        view.workers_recyclerview.adapter = adapter
        val linearManaget = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.workers_recyclerview.layoutManager = linearManaget
        return view
    }
}