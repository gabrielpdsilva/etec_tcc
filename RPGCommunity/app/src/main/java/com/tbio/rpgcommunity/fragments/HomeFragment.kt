package com.tbio.rpgcommunity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
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
    private lateinit var realView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        realView = inflater.inflate(R.layout.fragment_home, container, false)
        val sessaoAPesquisar: String? = arguments?.getString("search", null)

        if(sessaoAPesquisar == null || sessaoAPesquisar == "") {
            FirebaseFirestore.getInstance()
                    .collectionGroup("Sessoes")
                    .orderBy("likes", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener {
                        sessoes = mutableListOf<Sessao>()

                        for(sessao in it) {
                            sessoes.add(Sessao.toNewObject(sessao) as Sessao)
                        }

                        setHomeRecyclerView()
                    }.addOnFailureListener {
                        Log.e("error_ffc", it.message.toString())
                    }
        } else {
            FirebaseFirestore.getInstance()
                    .collectionGroup("Sessoes")
                    .whereEqualTo("nome.nome", sessaoAPesquisar)
                    .orderBy("likes", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener {
                        sessoes = mutableListOf<Sessao>()

                        for(sessao in it) {
                            sessoes.add(Sessao.toNewObject(sessao) as Sessao)
                        }

                        setHomeRecyclerView()
                    }.addOnFailureListener {
                        Log.e("error_ffc", it.message.toString())
                    }
        }

        // Inflate the layout for this fragment
        return realView
    }

    private fun setHomeRecyclerView() {
        // define a progressbar como invisível
        realView.findViewById<ProgressBar>(R.id.pbHome).visibility = View.GONE

        if(sessoes.size > 0) {
            // define a visibilidade de 'Não existem sessões disponíveis'
            realView.findViewById<TextView>(R.id.txtNaoExistemSessoes).visibility = View.GONE

            // define o adapter da RecyclerView
            realView.findViewById<RecyclerView>(R.id.rvSessoes)
                    .adapter = SessaoAdapter(sessoes, realView.context)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(realView.context)
            linearLayout.orientation = RecyclerView.VERTICAL
            realView.findViewById<RecyclerView>(R.id.rvSessoes).layoutManager = linearLayout
        } else {
            realView.findViewById<TextView>(R.id.txtNaoExistemSessoes).visibility = View.VISIBLE
        }
    }
}
