package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Solicitacao
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import kotlinx.android.synthetic.main.list_item_solicitacoes.view.*

class SolicitacaoAdapter(val ctx: Context,
                         val solicitations: MutableList<Solicitacao>)
    : RecyclerView.Adapter<SolicitacaoAdapter.SolicitacaoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitacaoViewHolder =
            SolicitacaoViewHolder(LayoutInflater.from(ctx).inflate(R.layout.list_item_solicitacoes, parent, false))

    override fun getItemCount(): Int = this.solicitations.size

    override fun onBindViewHolder(holder: SolicitacaoViewHolder, position: Int) {

        solicitations[position].from.get()
                .addOnSuccessListener {
                    holder.from.text = it["nickname.nome"] as String
                }

        holder.accept.setOnClickListener {
            FirebaseFirestore.getInstance()
                    .collection("Usuarios")
                    .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
                    .get()
                    .addOnSuccessListener {users ->
                        for(user in users) {
                            Log.i("DebugAddAmigo", "Valor de user.reference = ${user.reference.path}")
                            Log.i("DebugAddAmigo", "Valor de user.reference = ${solicitations[position].from.path}")

                            val userToSaveFromSolicitation = this.solicitations[position].from;

                            user.reference
                                    .get()
                                    .addOnSuccessListener {u ->
                                        val mUser = Usuario.toNewObject(u) as Usuario

                                        Log.i("DebugAddAmigo", "user to save = "
                                                + userToSaveFromSolicitation)

                                        if(mUser.amigos == null)
                                            mUser.amigos = mutableListOf()

                                        mUser.amigos!!.add(userToSaveFromSolicitation)

                                        FirebaseFirestore.getInstance()
                                                .document(u.reference.path)
                                                .set(mUser.toHashMap())
                                                .addOnFailureListener {
                                                    Log.e("error_adf", it.toString())
                                                }
                                    }

                            userToSaveFromSolicitation
                                    .get()
                                    .addOnSuccessListener {u ->
                                        val mUser = Usuario.toNewObject(u) as Usuario

                                        if(mUser.amigos == null)
                                            mUser.amigos = mutableListOf()

                                        mUser.amigos?.let {fl ->
                                            fl.add(user.reference)

                                            FirebaseFirestore.getInstance()
                                                    .document(u.reference.path)
                                                    .set(mUser.toHashMap())
                                                    .addOnFailureListener {
                                                        Log.e("error_adf", it.toString())
                                                    }
                                        }
                                    }
                        }

                        solicitations[position].referencia.delete()
                        solicitations.removeAt(position)

                        this.notifyDataSetChanged()
                    }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }

    inner class SolicitacaoViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        val from = itemView.list_item_solicitacoes_txt_from
        val accept = itemView.list_item_solicitacoes_btn_accept
    }
}