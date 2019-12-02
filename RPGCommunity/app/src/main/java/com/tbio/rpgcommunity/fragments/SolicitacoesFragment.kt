package com.tbio.rpgcommunity.fragments

import android.content.Context
import android.net.Uri
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Solicitacao
import com.tbio.rpgcommunity.classes_recycler_view.SolicitacaoAdapter

class SolicitacoesFragment : Fragment() {
    private lateinit var realView: View
    private lateinit var pbSolicitacoes: ProgressBar
    private lateinit var solicitations: MutableList<Solicitacao>
    private lateinit var txtYouHaveNoPendentSolicitations: TextView
    private lateinit var rvSolicitations: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.realView = inflater.inflate(R.layout.fragment_solicitacoes, container, false)
        this.pbSolicitacoes = this.realView.findViewById(R.id.pbSolicitacoes)
        this.txtYouHaveNoPendentSolicitations = this.realView.findViewById(R.id.txtYouHaveNoPendentSolicitations)
        this.rvSolicitations = this.realView.findViewById(R.id.rvSolicitacoes)
        this.solicitations = mutableListOf()

        FirebaseFirestore.getInstance()
                .collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
                .get()
                .addOnSuccessListener {
                        for(u in it) {
                            FirebaseFirestore.getInstance()
                                    .collection("${u.reference.path}/Solicitacoes de Amizade")
                                    .addSnapshotListener { value, e ->
                                        if (e != null) {
                                            Log.w("DebugUpdateMensagens", "Listen failed.", e)
                                            return@addSnapshotListener
                                        }

                                        for (doc in value!!.documentChanges) {
                                            if (doc.type == DocumentChange.Type.ADDED) {
                                                solicitations.add(Solicitacao.toNewObject(doc = doc.document))
                                            }
                                        }

                                        this.rvSolicitations.adapter?.notifyDataSetChanged()
                                                ?: this.setRecyclerView()
                                    }
                        }
                    }

                    return this.realView
                }

    fun setRecyclerView() {
        this.pbSolicitacoes.visibility = View.GONE

        if(solicitations.size > 0) {
            this.rvSolicitations.adapter = SolicitacaoAdapter(realView.context, solicitations);
            val mLinearLayout = LinearLayoutManager(activity);
            mLinearLayout.orientation = RecyclerView.VERTICAL

            this.rvSolicitations.layoutManager = mLinearLayout
        } else {
            this.txtYouHaveNoPendentSolicitations.visibility = View.VISIBLE
        }
    }
}
