package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import com.tbio.rpgcommunity.sub_activitys.PerfilActivity
import kotlinx.android.synthetic.main.list_item_amigos.view.*

// RecyclerView de amigos
class AmigoAdapter(val amigos: MutableList<Usuario>,
                   val context: Context)
    : RecyclerView.Adapter<AmigoAdapter.AmigoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder =
        AmigoViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_amigos,
                parent,
                false))

    override fun getItemCount(): Int = this.amigos.size

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {

        // define os valores nos atributos do XML
        holder.nome.text = amigos[position].nickname.nome
        holder.nickname.text = amigos[position].nickname.toString()

        holder.cardview.setOnClickListener {
            val intentToPerfilActivity: Intent = Intent(this.context, PerfilActivity::class.java)
            intentToPerfilActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intentToPerfilActivity.putExtra("usuario", this.amigos[position])
            this.context.startActivity(intentToPerfilActivity)
        }

        // carrega a foto do amigo
        Picasso.get()
                .load(amigos[position].foto)
                .resize(95, 95)
                .into(holder.foto)
    }

    override public fun getItemViewType(position: Int): Int {
        return position
    }

    inner class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardview = itemView.custom_cdv_item_amigo
        var nickname = itemView.nicknameAmigo
        var nome = itemView.nomeAmigo
        var foto = itemView.imgAmigo
    }
}