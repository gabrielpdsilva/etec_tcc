package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R.*
import com.tbio.rpgcommunity.classes_model_do_sistema.Mensagem
import kotlinx.android.synthetic.main.list_item_mensagens.*
import kotlinx.android.synthetic.main.list_item_mensagens.view.*
import java.lang.Exception
import android.R
import android.graphics.Typeface
import com.tbio.rpgcommunity.classes_model_do_sistema.Codigos
import com.tbio.rpgcommunity.classes_model_do_sistema.Palavras
import java.nio.charset.CodingErrorAction


class MensagemAdapter(val mensagens: MutableList<Mensagem>,
                      val context: Context)
    : RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder =
            MensagemViewHolder(LayoutInflater.from(context).inflate(layout.list_item_mensagens, parent, false))

    override fun getItemCount(): Int = this.mensagens.size

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        var isComplete = false
        FirebaseFirestore.getInstance()
                .document(this.mensagens[position].de!!.path)
                .get()
                .addOnSuccessListener {
                    if(it["email"].toString() == FirebaseAuth.getInstance().currentUser!!.email!!.toString()) {
                        holder.mensagem = holder.itemView.list_item_mensagens_layout_enviando
                        holder.cardView = holder.itemView.list_item_mensagens_crdview_enviando
                    }
                    else {
                        holder.mensagem = holder.itemView.list_item_mensagens_layout_chegando
                        holder.cardView = holder.itemView.list_item_mensagens_crdview_chegando
                    }

                    holder.mensagem!!.text = this.mensagens[position].mensagem;
                    holder.cardView!!.visibility = View.VISIBLE

                    when(this.mensagens[position].comportamento) {
                        Codigos.ACAO -> {
                            holder.mensagem!!.typeface = Typeface.DEFAULT_BOLD
                        }

                        Codigos.FALA -> {
                            holder.mensagem!!.text = " — ${this.mensagens[position].mensagem}"
                        }

                        Codigos.PENSAMENTO -> {
                            holder.mensagem!!.text = """"${this.mensagens[position].mensagem}""""
                            holder.mensagem!!.typeface = Typeface.create(holder.mensagem!!.typeface, Typeface.ITALIC)
                        }

                        Codigos.COMUM -> {

                        }

                        else -> throw IllegalArgumentException("Valor inválido passado para comportamento")
                    }
                }
    }

    override public fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mensagem: TextView? = null
        var cardView: CardView? = null
    }
}