package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.sub_activitys.SessaoActivity
import kotlinx.android.synthetic.main.list_item_sessoes.view.*

class SessaoAdapter(val sessoes: MutableList<Sessao>,
                  val context: Context)
    : RecyclerView.Adapter<SessaoAdapter.SessaoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessaoViewHolder =
            SessaoViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_sessoes,
                    parent,
                    false))

    override fun getItemCount(): Int = this.sessoes.size

    override fun onBindViewHolder(holder: SessaoViewHolder, position: Int) {

        FirebaseFirestore
                .getInstance()
                .document("Usuarios/${sessoes[position].getParentId()}")
                .get().addOnSuccessListener {
                    holder.mestre.text = (it["nickname"] as HashMap<String, Any?>).get("nome") as String
                }

        Picasso.get()
                .load(sessoes[position].imagem)
                .resize(265, 155)
                .into(holder.image)

        holder.cardview.setOnClickListener {
            val intentToSessaoActivity: Intent = Intent(this.context, SessaoActivity::class.java)
            intentToSessaoActivity.putExtra("sessao", this.sessoes[position])
            this.context.startActivity(intentToSessaoActivity)
        }

        holder.sistema.text = sessoes[position].sistema
        holder.nome.text = sessoes[position].nome.toString()
        holder.descricao.text = sessoes[position].descricao?.getDescricaoBasica()
                ?: "sem descrição"
    }

    override public fun getItemViewType(position: Int): Int {
        return position
    }

    inner class SessaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardview = itemView.custom_cdv_item_sessao
        val image = itemView.custom_img_profile_sessao
        val nome = itemView.custom_txt_nome_sessao
        val sistema = itemView.custom_txt_sistema_sessao
        val mestre = itemView.custom_txt_mestre_sessao
        val descricao =  itemView.custom_txt_descricao_sessao
    }
}