package com.tbio.rpgcommunity.logincadastro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Divergencias
import com.tbio.rpgcommunity.classes_model_do_sistema.Erros
import com.tbio.rpgcommunity.classes_model_do_sistema.Nome
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import kotlinx.android.synthetic.main.cadastro.*
import org.jetbrains.anko.email
import org.jetbrains.anko.toast

class Cadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)

        val btnCadastrar: Button = findViewById<Button>(R.id.btnCadastrar)
        btnCadastrar.setOnClickListener {
            val emailUsuario: EditText = this.txtEmail
            val senhaUsuario: EditText = this.txtSenha
            val confirmarSenhaUsuario: EditText = this.txtConfirmeASenha

            // verifica se o nickname foi preenchido
            if(this.txtNickname.text.toString().isEmpty()){
                this.txtNickname.error = Divergencias.USUARIO_NICKNAME_VAZIO
                this.txtNickname.isFocusable = true
            }

            // verifica se o email está vazio
            else if(emailUsuario.text.toString().isEmpty()){
                emailUsuario.error = Divergencias.USUARIO_EMAIL_VAZIO
                emailUsuario.isFocusable = true
            }

            // verifica se a senha está vazia
            else if(senhaUsuario.text.toString().isEmpty()){
                senhaUsuario.error = Divergencias.USUARIO_SENHA_VAZIA
                senhaUsuario.isFocusable = true
            }

            // verifica se a senha foi digitada e re-digitada corretamente
            else if(!senhaUsuario.text.toString().equals(confirmarSenhaUsuario.text.toString())) {
                confirmarSenhaUsuario.error = Divergencias.USUARIO_CONFS_INVALIDA
                confirmarSenhaUsuario.isFocusable = true
            }

            // realiza o cadastro
            else{
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(emailUsuario.text.toString(),
                                                        senhaUsuario.text.toString())
                        .addOnSuccessListener {

                            // cria o usuário para ser salvo no banco
                            Usuario(id = it.user!!.uid,
                                    nickname = Nome(this.txtNickname.text.toString()),
                                    email = it.user!!.email.toString())
                                    .saveDB(fun (doc){

                                        // usuário salvo com sucesso
                                        toast("Usuário cadastrado com sucesso")
                                        finish()
                                    }, fun (exception){
                                        toast(Erros.ERRO_AO_CRIAR_USUARIO)
                                        finish()
                                    })
                        }.addOnFailureListener {
                            // caso o usuário ja esteja cadastrado
                            toast("O usuário ja tem cadastro no aplicativo")
                            finish()
                        }
            }
        }

        //TextView Já é Cadastrado, ele simplesmente encerra a activity atual
        //e retorna pro login
        val btnJa: TextView = this.tvJaECadastrado
        btnJa.setOnClickListener{
            finish()
        }
    }
}