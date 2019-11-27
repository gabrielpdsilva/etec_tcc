package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import kotlinx.android.synthetic.main.activity_perfil.*
import org.jetbrains.anko.toast

class PerfilActivity : AppCompatActivity() {
    private lateinit var btnAddFriend: Button
    private lateinit var mUser: Usuario
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        this.btnAddFriend = findViewById<Button>(R.id.activity_perfil_btn_add_friend)
        this.mUser = intent.getParcelableExtra<Usuario>("usuario")
        this.title = mUser.nickname.toString()

        Picasso.get()
                .load(mUser.foto)
                .into(findViewById<ImageView>(R.id.img_perfil_usuario))

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

                        Log.i("DebugAddAmigo", "thisUserAlreadySendSolicitation = $thisUserAlreadyIsFriend")

                        btnAddFriend.isEnabled = ! thisUserAlreadyIsFriend;
                    }
                }
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
                                        if(solicitation["userDocFromSolicitation"] == u.reference)
                                            thisUserAlreadySendSolicitation = true
                                    }

                                    Log.i("DebugAddAmigo", "thisUserAlreadySendSolicitation = $thisUserAlreadySendSolicitation")

                                    btnAddFriend.isEnabled = ! thisUserAlreadySendSolicitation;
                                }
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
}
