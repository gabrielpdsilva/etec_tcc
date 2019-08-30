package com.tbio.rpgcommunity.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import com.tbio.rpgcommunity.classes_recycler_view.AmigoAdapter
import kotlinx.android.synthetic.main.fragment_amigos.*
import kotlinx.android.synthetic.main.list_item_amigos.*
import kotlinx.android.synthetic.main.list_item_amigos.view.*
import org.jetbrains.anko.support.v4.toast

class AmigosFragment : Fragment() {

    private var amigos: MutableList<Usuario>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activity!!.findViewById<ProgressBar>(R.id.pbAmigos)?.visibility = View.VISIBLE

        // primeiro será efetuada a query para retornar os dados do usuário atual
        FirebaseFirestore.getInstance()
                .collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                .limit(1)
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        // nossa lista de amigos
                        amigos = mutableListOf<Usuario>()

                        // efetua a query dos dados de cada amigo do usuário atual
                        (it["amigos"] as List<DocumentReference>?)?.forEach {

                            // recupera os dados do amigo
                            it.get().addOnSuccessListener {

                                // adiciona o amigo na nossa lista de amigos
                                amigos!!.add(Usuario.toNewObject(it) as Usuario)
                                applyChangeRecyclerView()
                            }.addOnFailureListener {
                                toast(it.message.toString())
                            }
                        } ?: applyChangeRecyclerView()
                    }
                }
                .addOnFailureListener {
                    toast(it.message.toString())
                    pbAmigos.visibility = View.GONE
                }

        return LayoutInflater.from(container?.context).inflate(R.layout.fragment_amigos, container, false)
    }

    // define o RecyclerView
    private fun applyChangeRecyclerView() {
        pbAmigos.visibility = View.GONE // torna a ProgressBar invisível

        // verifica se existem objetos na lista amigos
        if(amigos?.size!! > 0){
            // define o texto 'Você não possui amigos' como invisível
            txtVoceNaoPossuiAmigos.visibility = View.GONE
            txtVoceNaoPossuiAmigos.isEnabled = false

            // define o adapter para o RecyclerView
            rvAmigos.adapter = AmigoAdapter(amigos!!, activity!!.applicationContext)

            // define o layout
            val linearLayout = LinearLayoutManager(activity)
            linearLayout.orientation = RecyclerView.VERTICAL
            rvAmigos.layoutManager = linearLayout
        }

        // caso não existam amigos na lista "amigos"
        else {
            txtVoceNaoPossuiAmigos.visibility = View.VISIBLE
            txtVoceNaoPossuiAmigos.isEnabled = true
        }
    }


}