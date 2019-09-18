package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Mensagem
import kotlinx.android.synthetic.main.list_item_mensagens_outros.view.*

class MensagemAdapter(val mensagens: MutableList<Mensagem>,
                      val context: Context)
    : RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder =
            MensagemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_mensagens_outros, parent))

    override fun getItemCount(): Int = this.mensagens.size

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        FirebaseFirestore.getInstance()
                .document(this.mensagens[position].de!!.path)
                .get()
                .addOnSuccessListener {
                    holder.de.text = it["email"].toString()
                }

        holder.mensagem.text = this.mensagens[position].mensagem
    }

    inner class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val de = itemView.list_item_mensagens_outros_de
        val mensagem = itemView.list_item_mensagens_outros_mensagem

        fun getLayout(): Int{
            var layout: Int = R.layout.list_item_mensagens_minhas
            var isComplete = false

            FirebaseFirestore.getInstance()
                    .document(this@MensagemAdapter.mensagens[this.adapterPosition].de!!.id)
                    .get()
                    .addOnSuccessListener {
                        if(it["email"] == FirebaseAuth.getInstance().currentUser!!.email!!.toString())
                            layout = R.layout.list_item_mensagens_outros

                        isComplete = true
                    }

            while(!isComplete);

            return layout
        }
    }
}