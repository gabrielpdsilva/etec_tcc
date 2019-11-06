package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Mensagem
import kotlinx.android.synthetic.main.list_item_mensagens.*
import kotlinx.android.synthetic.main.list_item_mensagens.view.*

class MensagemAdapter(val mensagens: MutableList<Mensagem>,
                      val context: Context)
    : RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder =
            MensagemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_mensagens, parent, false))

    override fun getItemCount(): Int = this.mensagens.size

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        var isComplete = false
        FirebaseFirestore.getInstance()
                .document(this.mensagens[position].de!!.path)
                .get()
                .addOnSuccessListener {
                    holder.mensagem =
                            if(it["email"] === FirebaseAuth.getInstance().currentUser!!.email!!)
                                holder.itemView.list_item_mensagens_layout_enviando
                            else
                                holder.itemView.list_item_mensagens_layout_chegando

                    isComplete = true;
                }

        while(!isComplete);

        holder.mensagem!!.text = this.mensagens[position].mensagem;
    }

    inner class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mensagem: TextView? = null
    }
}