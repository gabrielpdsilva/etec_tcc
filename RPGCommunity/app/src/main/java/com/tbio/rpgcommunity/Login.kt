package com.tbio.rpgcommunity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
//import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.longToast
//import android.widget.Toast
//import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        //botao Login
        var usuario: TextView = findViewById<TextView>(R.id.txtUsuario)
        var senha: TextView = findViewById<TextView>(R.id.txtSenha)
        val btnLogin: Button = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if(usuario.text.toString().equals("root") && senha.text.toString().equals("123")){
                startActivity<MainActivity>()
                longToast("Botão login rodou, você está na MainActivity!")
            }else{
                toast("usuário inválido...")
            }

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