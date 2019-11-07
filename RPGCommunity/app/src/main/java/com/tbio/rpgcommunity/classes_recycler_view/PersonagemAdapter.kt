package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import com.tbio.rpgcommunity.sub_activitys.PerfilActivity
import com.tbio.rpgcommunity.sub_activitys.PersonagemActivity
import kotlinx.android.synthetic.main.list_item_personagens.view.*

class PersonagemAdapter(val context: Context,
                        val personagens: MutableList<Personagem>)
                        : RecyclerView.Adapter<PersonagemAdapter.PersonagemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonagemViewHolder =
            PersonagemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_personagens,
                                                                         parent,
                                                                         false))

    override fun getItemCount(): Int = this.personagens.size

    override fun onBindViewHolder(holder: PersonagemViewHolder, position: Int) {

        holder.nome.text = personagens[position].nome.nome
        holder.descricao.text = String.format("Descrição: %s",
                personagens[position].descricao?.getDescricaoBasica() ?: "Sem descrição")

        Picasso.get()
                .load(personagens[position].image)
                .resize(95, 95)
                .error(R.drawable.ic_add_avatar)
                .into(holder.foto)

        personagens[position].sessao?.get()
                ?.addOnSuccessListener {
                    val nome = it["nome"] as HashMap<String, Any?>
                    holder.sessao.text = String.format("Sessão: %s", nome["nome"] as String)
                }

        if(personagens[position].sessao == null){
            holder.sessao.text = String.format("Sessão: %s", "Não está em sessões")
        }

    }

    inner class PersonagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nome = itemView.custom_txt_nome_personagem
        val foto = itemView.custom_img_foto_personagem
        val descricao = itemView.custom_txt_descricao_personagem
        val sessao = itemView.custom_txt_sessao_personagem

        init{
            itemView.setOnClickListener {
                val intentToPersonagemActivity = Intent(this@PersonagemAdapter.context, PersonagemActivity::class.java)
                intentToPersonagemActivity.putExtra("personagem", this@PersonagemAdapter.personagens[this.adapterPosition])
                this@PersonagemAdapter.context.startActivity(intentToPersonagemActivity)
            }
        }
    }
}