package com.tbio.rpgcommunity.sub_activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Codigos
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import com.tbio.rpgcommunity.classes_recycler_view.AmigoAdapter
import com.tbio.rpgcommunity.classes_recycler_view.PersonagemAdapter
import kotlinx.android.synthetic.main.activity_adicionar_jogador.*
import kotlinx.android.synthetic.main.fragment_amigos.*

class AdicionarJogadorActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser
    private lateinit var amigos: MutableList<Usuario>
    private lateinit var personagens: MutableList<Personagem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogador)

        this.amigos = mutableListOf()
        this.personagens = mutableListOf()

        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseFirestore.getInstance()
        db.collection("Usuarios")
                .whereEqualTo("email", user.email.toString())
                .limit(1)
                .get()
                .addOnSuccessListener {
                    for(this_u in it) {
                        db.document(this_u.reference.path)
                                .get()
                                .addOnSuccessListener {
                                     val amigos = it["amigos"] as MutableList<DocumentReference>? ?: mutableListOf()
                                     for(f in amigos) {
                                         f.get().addOnSuccessListener {
                                                this.amigos.add(Usuario.toNewObject(it) as Usuario)

                                                db.collection("${it.reference.path}/Personagens")
                                                        .get()
                                                        .addOnSuccessListener {
                                                            for(p in it) {
                                                                this.personagens.add(Personagem.toNewObject(p) as Personagem)
                                                            }

                                                            this.setRecyclerView()
                                                        }
                                            }
                                     }
                                }
                    }
                }
                .addOnCompleteListener {
                    this.setRecyclerView()
                }
    }

    public fun setRecyclerView() {
        if(personagens.size > 0) {
            // define o texto 'Você não possui amigos' como invisível
            txtVoceNaoPossuiAmigosAdd.visibility = View.GONE
            txtVoceNaoPossuiAmigosAdd.isEnabled = false

            // define o adapter para o RecyclerView
            rvPersonagensAdd.adapter =
                    PersonagemAdapter(this.applicationContext,
                                      this.personagens,
                   true) {
                        val resultIntent = Intent()

                        resultIntent.putExtra("personagem_selecionado", it)

                        setResult(Codigos.CODIGO_PARA_ADICIONAR_JOGADOR, resultIntent)
                        finish()
                    }

            // define o layout
            val linearLayout = LinearLayoutManager(this)
            linearLayout.orientation = RecyclerView.VERTICAL
            rvPersonagensAdd.layoutManager = linearLayout
        } else {
            // define o texto 'Você não possui amigos' como visível
            txtVoceNaoPossuiAmigosAdd.visibility = View.VISIBLE
            txtVoceNaoPossuiAmigosAdd.isEnabled = true
        }
    }
}
