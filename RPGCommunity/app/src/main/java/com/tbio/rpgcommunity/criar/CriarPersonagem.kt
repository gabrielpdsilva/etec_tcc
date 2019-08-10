package com.tbio.rpgcommunity.criar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.tbio.rpgcommunity.R
import kotlinx.android.synthetic.main.criar_personagem.*
import org.jetbrains.anko.toast

class CriarPersonagem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.criar_personagem)

        val img: ImageView = findViewById(R.id.imgPersonagem)

        imgPersonagem.setOnClickListener {
            toast("Você clicou na imagem.")
        }
    }
}