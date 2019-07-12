package com.tbio.rpgcommunity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class Cadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)

        //botão Já é Cadastrado, ele simplesmente encerra a activity atual
        //e retorna pro login
        val btnJa: TextView = findViewById<TextView>(R.id.tvJaECadastrado) as TextView
        btnJa.setOnClickListener{
            finish()
        }
    }
}