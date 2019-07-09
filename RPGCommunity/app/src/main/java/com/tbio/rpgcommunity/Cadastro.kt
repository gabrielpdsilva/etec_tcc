package com.tbio.rpgcommunity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import org.jetbrains.anko.startActivity

class Cadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)

        val btnJa: TextView = findViewById<TextView>(R.id.tvJaECadastrado) as TextView
        btnJa.setOnClickListener{
            startActivity<Login>()
        }
    }
}