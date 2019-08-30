package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val mUser: Usuario = intent.getParcelableExtra<Usuario>("usuario")
        this.title = mUser.nickname.toString()

        Picasso.get()
                .load(mUser.foto)
                .into(findViewById<ImageView>(R.id.img_perfil_usuario))

        findViewById<TextView>(R.id.nickname_perfil_usuario).text = mUser.nickname.nome
    }
}
