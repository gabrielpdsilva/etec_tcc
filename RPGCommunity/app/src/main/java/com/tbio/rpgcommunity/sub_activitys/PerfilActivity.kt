package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import com.tbio.rpgcommunity.classes_recycler_view.PersonagemAdapter
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.activity_perfil.*
import org.jetbrains.anko.toast

class PerfilActivity : AppCompatActivity() {
    private lateinit var btnAddFriend: Button
    private lateinit var mUser: Usuario
    private lateinit var rvPersonagens: RecyclerView
    private lateinit var rvSessoes: RecyclerView
    private var personagens = mutableListOf<Personagem>()
    private var sessoes = mutableListOf<Sessao>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        this.rvPersonagens = findViewById(R.id.activity_perfil_rv_personagens)
        this.rvSessoes = findViewById(R.id.activity_perfil_rv_sessoes)

        this.btnAddFriend = findViewById<Button>(R.id.activity_perfil_btn_add_friend)
        this.mUser = intent.getParcelableExtra<Usuario>("usuario")
        this.title = mUser.nickname.toString()

        Picasso.get()
                .load(mUser.foto)
                .into(findViewById<ImageView>(R.id.img_perfil_usuario))

        db.collection("${mUser.referencia.path}/Personagens")
                .get()
                .addOnSuccessListener {
                    for(personagem in it) {
                        this.personagens.add(Personagem.toNewObject(personagem) as Personagem)
                    }

                    this.setPersonagensRecyclerView()
                }

        db.collection("${mUser.referencia.path}/Sessoes")
                .get()
                .addOnSuccessListener {
                    for(sessao in it) {
                        this.sessoes.add(Sessao.toNewObject(sessao) as Sessao)
                    }

                    this.setSessoesRecyclerView()
                }

        findViewById<TextView>(R.id.nickname_perfil_usuario).text = mUser.nickname.nome

        db.collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {returnValues ->

                    for(thisUser in returnValues) {
                        var thisUserAlreadyIsFriend = false
                        mUser.amigos?.forEach { doc ->
                            if (doc == thisUser.reference)
                                thisUserAlreadyIsFriend = true
                        }

                        Log.i("DebugAddAmigo", "thisUserAlreadyIsFriend = $thisUserAlreadyIsFriend")

                        this.verifySolicitation(thisUserAlreadyIsFriend)
                    }
                }

        btnAddFriend.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, btnAddFriend)
            popupMenu.inflate(R.menu.profile_menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_item_add_amigo -> {
                        addAmigoSolicitation()
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun verifySolicitation(isAFriend: Boolean) {
        if(isAFriend)
            return

        db.collection("${mUser.caminho}/Solicitacoes de Amizade")
                .get()
                .addOnSuccessListener {solicitations ->
                    db.collection("Usuarios")
                            .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                            .get()
                            .addOnSuccessListener {user ->
                                for(u in user) {
                                    var thisUserAlreadySendSolicitation = false

                                    for(solicitation in solicitations) {
                                        if((solicitation["userDocFromSolicitation"] as DocumentReference) == u.reference)
                                            thisUserAlreadySendSolicitation = true
                                    }

                                    Log.i("DebugAddAmigo", "thisUserAlreadySendSolicitation = $thisUserAlreadySendSolicitation")

                                    if(! thisUserAlreadySendSolicitation) {
                                        this.btnAddFriend.visibility = View.VISIBLE
                                        this.btnAddFriend.isEnabled = true
                                    }
                                }
                            }
                }
    }

    private fun addAmigoSolicitation() {
        db.collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {
                    for(thisUser in it) {
                        val hashToSaveSolicitation = hashMapOf<String, Any?>(
                                "date" to Timestamp.now(),
                                "userDocFromSolicitation" to thisUser.reference,
                                "isAccepted" to false
                        )

                        db.collection("${mUser.caminho}/Solicitacoes de Amizade")
                                .add(hashToSaveSolicitation)
                                .addOnSuccessListener {
                                    toast("solicitação de amizade enviada para o usuário")
                                    finish()
                                }
                    }
                }
    }

    fun setSessoesRecyclerView() {
        this.rvSessoes.adapter = SessaoAdapter(this.sessoes, this)

        val mLyt = LinearLayoutManager(this)
        mLyt.orientation = RecyclerView.HORIZONTAL
        this.rvSessoes.layoutManager = mLyt
    }

    fun setPersonagensRecyclerView() {
        this.rvPersonagens.adapter = PersonagemAdapter(this, this.personagens)

        val mLyt = LinearLayoutManager(this)
        mLyt.orientation = RecyclerView.HORIZONTAL
        this.rvPersonagens.layoutManager = mLyt
    }
}
