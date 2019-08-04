package com.tbio.rpgcommunity.logincadastro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.tbio.rpgcommunity.MainActivity
import com.tbio.rpgcommunity.R
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //botao Login
        var email: TextView = findViewById<TextView>(R.id.txtEmail)
        var senha: TextView = findViewById<TextView>(R.id.txtSenha)
        val btnLogin: Button = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if (email.text.toString().equals("root") && senha.text.toString().equals("123")) {
                startActivity<MainActivity>()
                longToast(R.string.txtLoginRealizado)

                //só pra deixar os campos de usuario e senha em branco após o login
                email.setText("")
                senha.setText("")
            } else {
                toast(R.string.txtLoginErro)
            }
        }

        //TextView Cadastro
        val tvCadastro: TextView = findViewById(R.id.tvCadastrar) as TextView
        tvCadastro.setOnClickListener{
            startActivity<Cadastro>()
        }

        //TextView Esqueci a Senha
        val tvEsqueceu: TextView = findViewById(R.id.tvEsqueceu) as TextView
        tvEsqueceu.setOnClickListener {
            startActivity<EsqueciASenha>()
        }

    }
}