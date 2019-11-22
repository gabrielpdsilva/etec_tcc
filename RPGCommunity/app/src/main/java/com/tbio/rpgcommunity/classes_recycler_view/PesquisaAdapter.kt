package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import kotlinx.android.synthetic.main.list_item_pesquisas.view.*

class PesquisaAdapter (val pesquisas: MutableList<String>,
                       val context: Context)
    : RecyclerView.Adapter<PesquisaAdapter.PesquisaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesquisaViewHolder =
            PesquisaViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_pesquisas,
                    parent,
                    false))

    override fun getItemCount(): Int = this.pesquisas.size

    override fun onBindViewHolder(holder: PesquisaViewHolder, position: Int) {
        holder.cardview.setOnClickListener {

        }

        holder.pesquisa.text = pesquisas[position]
    }

    override public fun getItemViewType(position: Int): Int {
        return position
    }

    inner class PesquisaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardview = itemView.custom_cdv_item_pesquisa
        val pesquisa = itemView.custom_txt_pesquisa
    }
}