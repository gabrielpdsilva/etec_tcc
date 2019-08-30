package com.tbio.rpgcommunity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Nome
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_sessoes.view.*
import org.jetbrains.anko.support.v4.toast

class HomeFragment : Fragment() {
    private lateinit var sessoes: MutableList<Sessao>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        FirebaseFirestore.getInstance()
                .collectionGroup("Sessoes")
                .orderBy("likes", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener {
                    sessoes = mutableListOf<Sessao>()

                    for(sessao in it){
                        sessoes.add(Sessao.toNewObject(sessao) as Sessao)
                    }

                    setHomeRecyclerView()
                }.addOnFailureListener {
                    Log.e("error_ffc", it.message.toString())
                }

        // Inflate the layout for this fragment
        return view
    }

    private fun setHomeRecyclerView() {
        // define a progressbar como invisível
        view!!.findViewById<ProgressBar>(R.id.pbHome).visibility = View.GONE

        if(sessoes.size > 0) {
            // define a visibilidade de 'Não existem sessões disponíveis'
            txtNaoExistemSessoes.visibility = View.GONE

            // define o adapter da RecyclerView
            rvSessoes.adapter = SessaoAdapter(sessoes, view!!.context)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(view!!.context)
            linearLayout.orientation = RecyclerView.VERTICAL
            rvSessoes.layoutManager = linearLayout
        } else {
            txtNaoExistemSessoes.visibility = View.VISIBLE
        }
    }
}
