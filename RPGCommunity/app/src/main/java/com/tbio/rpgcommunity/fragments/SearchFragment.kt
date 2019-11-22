package com.tbio.rpgcommunity.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_recycler_view.PesquisaAdapter
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    public var pesquisas: MutableList<String> = mutableListOf()

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        return view
    }

    public fun setRecyclerView() {
        // define o adapter da RecyclerView
        rvSearch.adapter = PesquisaAdapter(pesquisas, view!!.context)

        // define o layout da RecyclerView
        val linearLayout = LinearLayoutManager(view!!.context)
        linearLayout.orientation = RecyclerView.VERTICAL
        rvSearch.layoutManager = linearLayout
    }
}
