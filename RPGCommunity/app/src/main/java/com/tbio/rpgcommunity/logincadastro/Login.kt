package com.tbio.rpgcommunity.logincadastro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tbio.rpgcommunity.MainActivity
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Divergencias
import com.tbio.rpgcommunity.classes_model_do_sistema.Erros
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        if(FirebaseAuth.getInstance()
                        .currentUser != null)
            startActivity<MainActivity>()

        //botao Login
        val email: TextView = this.txtEmail
        val senha: TextView = this.txtSenha
        val btnLogin: Button = this.btnLogin

        btnLogin.setOnClickListener {

            // verifica se o email está vazio
            if(email.text.toString().isEmpty()){
                email.error = Divergencias.USUARIO_EMAIL_VAZIO
                email.isFocusable = true
            }

            // verifica se a senha está vazia
            else if(senha.text.toString().isEmpty()){
                senha.error = Divergencias.USUARIO_SENHA_VAZIA
                senha.isFocusable = true
            }

            // realiza o login
            else {
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email.text.toString(),
                                                    senha.text.toString())
                        .addOnSuccessListener {
                            val intentToLogin = Intent(applicationContext, MainActivity::class.java)
                            intentToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            startActivity(intentToLogin)
                        }
                        .addOnFailureListener {
                            toast(Erros.ERRO_AO_LOGAR_USUARIO)
                            toast(it.message.toString())
                            toast(it.localizedMessage)
                        }
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