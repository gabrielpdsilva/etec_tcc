package com.tbio.rpgcommunity.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Solicitacao

class SolicitacoesFragment : Fragment() {
    private lateinit var realView: View
    private lateinit var pbSolicitacoes: ProgressBar
    private lateinit var solicitations: MutableList<Solicitacao>
    private lateinit var txtYouHaveNoPendentSolicitations: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.realView = inflater.inflate(R.layout.fragment_solicitacoes, container, false)
        this.pbSolicitacoes = this.realView.findViewById(R.id.pbSolicitacoes)
        this.txtYouHaveNoPendentSolicitations = this.realView.findViewById(R.id.txtYouHaveNoPendentSolicitations)


        return this.realView
    }

    fun setRecyclerView() {
        this.pbSolicitacoes.visibility = View.GONE

        if(solicitations.size > 0) {

        } else {
            this.txtYouHaveNoPendentSolicitations.visibility = View.VISIBLE
        }
    }
}
