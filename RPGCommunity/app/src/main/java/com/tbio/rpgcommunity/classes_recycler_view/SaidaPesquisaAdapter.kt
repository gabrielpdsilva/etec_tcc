package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.*
import com.tbio.rpgcommunity.sub_activitys.PerfilActivity
import com.tbio.rpgcommunity.sub_activitys.PersonagemActivity
import com.tbio.rpgcommunity.sub_activitys.SessaoActivity
import kotlinx.android.synthetic.main.list_item_output_pesquisas.view.*
import kotlin.reflect.KClass

class SaidaPesquisaAdapter (val pesquisasItems: MutableList<out DocumentoRpgItem>,
                            val context: Context) :
        RecyclerView.Adapter<SaidaPesquisaAdapter.SaidaPesquisaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaidaPesquisaViewHolder =
            SaidaPesquisaViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_output_pesquisas,
                    parent,
                    false))

    override fun getItemCount(): Int = this.pesquisasItems.size

    override fun onBindViewHolder(holder: SaidaPesquisaViewHolder, position: Int) {
        when(this.getItemViewType(position)) {
            0 -> {
                addPersonagem(position, holder)
            }

            1 -> {
                addSessao(position, holder)
            }

            2 -> {
                addUsuario(position, holder)
            }
        }

        holder.pesquisaSuperCard.visibility = View.VISIBLE
    }

    override public fun getItemViewType(position: Int): Int {
        var returnNumber: Int? = null

                    when(pesquisasItems[position]) {
                        is Personagem -> {
                            returnNumber = 0
                        }

                        is Sessao -> {
                            returnNumber = 1
                        }

                        is Usuario -> {
                            returnNumber = 2
                        }

                        else ->
                            throw
                            IllegalArgumentException(
                                    """Valor inválido: ${pesquisasItems[position]}
                                        | passado para 'docType'""".trimMargin())
                    }

        return returnNumber
    }

    private fun addPersonagem(position: Int, holder : SaidaPesquisaViewHolder) {
        val p: Personagem = pesquisasItems[position] as Personagem
        val rangeToDescription =
                if((p.descricao?.getDescricaoBasica()?.length ?: 1) > 130)
                    130
                else
                    (p.descricao?.getDescricaoBasica()?.length ?: 1) - 1

        holder.personName.text = p.nome.nome
        holder.personDescription.text =
                p.descricao?.getDescricaoBasica()?.substring(0..rangeToDescription)
                        ?: "sem descrição"

        if(p.sessao !== null)
            p.sessao?.get()
                ?.addOnSuccessListener {
                    holder.personSession.text = it["nome.nome"] as String
                }
        else
            holder.personSession.text = "Não possui sessão"

        holder.personLyt.visibility = View.VISIBLE
        holder.personLyt.setOnClickListener {
            val intentToPersonagemActivity: Intent = Intent(this.context, PersonagemActivity::class.java)
            intentToPersonagemActivity.putExtra("personagem", p)
            this.context.startActivity(intentToPersonagemActivity)
        }

        Picasso.get()
                .load(p.image)
                .into(holder.personImg)
    }

    private fun addSessao(position: Int, holder : SaidaPesquisaViewHolder) {
        val s: Sessao = pesquisasItems[position] as Sessao
        val rangeToDescription =
                if((s.descricao?.getDescricaoBasica()?.length ?: 1) > 130)
                    130
                else
                    (s.descricao?.getDescricaoBasica()?.length ?: 1) - 1

        holder.sessionName.text = s.nome.nome
        s.parentReference?.get()?.
                addOnSuccessListener {
                    holder.sessionMasterName.text = it["nickname.nome"] as String
                }
        holder.sessionSystemName.text = s.sistema
        holder.sessionDescriptionContent.text =
                s.descricao?.getDescricaoBasica()?.substring(0..rangeToDescription)
                ?: "sem descrição"

        holder.sessionLyt.visibility = View.VISIBLE
        holder.sessionLyt.setOnClickListener {
            val intentToSessaoActivity: Intent = Intent(this.context, SessaoActivity::class.java)
            intentToSessaoActivity.putExtra("sessao", s)
            this.context.startActivity(intentToSessaoActivity)
        }

        Picasso.get()
                .load(s.imagem)
                .into(holder.sessionImg)
    }

    private fun addUsuario(position: Int, holder : SaidaPesquisaViewHolder) {
        val u: Usuario = pesquisasItems[position] as Usuario

        holder.userNickName.text = u.nickname.nome

        holder.userLyt.visibility = View.VISIBLE
        holder.userLyt.setOnClickListener {
            val intentToUsuarioActivity: Intent = Intent(this.context, PerfilActivity::class.java)
            intentToUsuarioActivity.putExtra("usuario", u)
            this.context.startActivity(intentToUsuarioActivity)
        }

        Picasso.get()
                .load(u.foto)
                .into(holder.userImg)
    }

    inner class SaidaPesquisaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // cardview da pesquisa
        val pesquisaSuperCard = itemView.list_item_output_pesquisa_super_cdv

        // Usuarios
        val userLyt = itemView.list_item_output_pesquisa_user_lyt
        val userImg = itemView.list_item_output_pesquisa_user_img
        val userNickName = itemView.list_item_output_pesquisa_user_txt_nick
        val userNome = itemView.list_item_output_pesquisa_user_txt_name

        // Sessões
        val sessionLyt = itemView.list_item_output_pesquisa_session_lyt
        val sessionName = itemView.list_item_output_pesquisa_session_txt_name
        val sessionImg = itemView.list_item_output_pesquisa_session_img
        val sessionMasterName = itemView.list_item_output_pesquisa_session_txt_master_name
        val sessionSystemName = itemView.list_item_output_pesquisa_session_txt_system_name
        val sessionDescriptionContent = itemView.list_item_output_pesquisa_session_txt_description_content

        // Personagens
        val personLyt = itemView.list_item_output_pesquisa_person_lyt
        val personImg = itemView.list_item_output_pesquisa_person_img
        val personName = itemView.list_item_output_pesquisa_person_txt_name
        val personDescription = itemView.list_item_output_pesquisa_person_txt_description
        val personSession = itemView.list_item_output_pesquisa_person_txt_session
    }
}