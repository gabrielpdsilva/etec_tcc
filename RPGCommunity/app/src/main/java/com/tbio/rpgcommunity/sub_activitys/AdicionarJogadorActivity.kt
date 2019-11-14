package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario

class AdicionarJogadorActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser
    private lateinit var amigos: MutableList<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogador)

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
                                     val amigos = it["amigos"] as MutableList<DocumentReference> ?: mutableListOf()
                                     for(f in amigos) {
                                         f.get().addOnSuccessListener {
                                                this.amigos.add(Usuario.toNewObject(it) as Usuario)
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

    }
}
