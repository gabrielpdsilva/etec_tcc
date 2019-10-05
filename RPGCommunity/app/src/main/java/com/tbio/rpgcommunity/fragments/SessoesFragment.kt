package com.tbio.rpgcommunity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sessoes.*

class SessoesFragment : Fragment() {
    private lateinit var sessoes: MutableList<Sessao>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(container?.context).inflate(R.layout.fragment_sessoes, container, false)

        FirebaseFirestore.getInstance()
                .collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {

                    for(user in it) {
                        FirebaseFirestore.getInstance()
                                .collection("Usuarios/${user.id}/Sessoes")
                                .get()
                                .addOnSuccessListener {
                                    sessoes = mutableListOf()

                                    for(sessao in it) {
                                        this.sessoes.add(Sessao.toNewObject(sessao) as Sessao)
                                    }

                                    this.setSessionRecyclerView()
                                }
                    }
                }

        return view;
    }

    fun setSessionRecyclerView() {
        // define a progressbar como invisível
        view!!.findViewById<ProgressBar>(R.id.PbSessoes).visibility = View.GONE

        if(sessoes.size > 0) {
            // define a visibilidade de 'Não existem sessões disponíveis' como false
            txtNaoExistemSessoesSuas.visibility = View.GONE

            // define o adapter da RecyclerView
            fragment_sessoes_recycler_view.adapter = SessaoAdapter(sessoes, view!!.context)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(view!!.context)
            linearLayout.orientation = RecyclerView.VERTICAL
            fragment_sessoes_recycler_view.layoutManager = linearLayout
        } else {
            txtNaoExistemSessoesSuas.visibility = View.VISIBLE
        }
    }
}