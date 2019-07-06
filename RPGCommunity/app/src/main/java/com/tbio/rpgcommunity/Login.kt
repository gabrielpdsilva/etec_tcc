package com.tbio.rpgcommunity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        //botao Login
        val btnLogin: Button = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            toast("Botão login rodou...")
        }

        //TextView Cadastro
        val tvCadastro: TextView = findViewById(R.id.tvCadastrar) as TextView
        tvCadastro.setOnClickListener{
            startActivity<Cadastro>()
            toast("activity Cadastro rodou...")
        }

        //TextView Esqueci a Senha
        val tvEsqueceu: TextView = findViewById(R.id.tvEsqueceu) as TextView
        tvEsqueceu.setOnClickListener {
            startActivity<EsqueciASenha>()
            toast("activity EsqueciASenha rodou...")
        }

    }
}