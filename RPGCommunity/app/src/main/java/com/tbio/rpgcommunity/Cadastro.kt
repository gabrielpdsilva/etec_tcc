package com.tbio.rpgcommunity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.cadastro.*
import org.jetbrains.anko.toast

class Cadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)

        val btnCadastrar: Button = findViewById<Button>(R.id.btnCadastrar)
        btnCadastrar.setOnClickListener {
           toast("foi")
        }

        //TextView Já é Cadastrado, ele simplesmente encerra a activity atual
        //e retorna pro login
        val btnJa: TextView = findViewById<TextView>(R.id.tvJaECadastrado) as TextView
        btnJa.setOnClickListener{
            finish()
        }
    }
}