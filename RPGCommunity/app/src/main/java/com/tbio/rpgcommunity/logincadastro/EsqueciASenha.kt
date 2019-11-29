package com.tbio.rpgcommunity.logincadastro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tbio.rpgcommunity.R
import org.jetbrains.anko.toast

class EsqueciASenha : AppCompatActivity() {
    private lateinit var edtEmailResetPass: EditText
    private lateinit var btnConfirmReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.esqueciasenha)

        this.edtEmailResetPass = findViewById(R.id.txt_esqueci_a_senha)
        this.btnConfirmReset = findViewById(R.id.btnConfirmarResetDeSenha)

        this.btnConfirmReset.setOnClickListener {
            val emailToSendReset: String = this.edtEmailResetPass.text.toString()

            if(emailToSendReset.isEmpty() || emailToSendReset.isBlank()) {
                this.edtEmailResetPass.isFocusable = true
                this.edtEmailResetPass.error = "Preencha este campo"
            } else {
                try {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailToSendReset)
                            .addOnSuccessListener {
                                toast("email de reset de senha enviado")
                                finish()
                            }
                } catch (e: Exception) {
                    toast("exception = ${e.message}")
                    finish()
                }
            }
        }
    }
}