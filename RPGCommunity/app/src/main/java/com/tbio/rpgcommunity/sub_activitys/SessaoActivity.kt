package com.tbio.rpgcommunity.sub_activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import org.jetbrains.anko.find

class SessaoActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessao)

        val switch = findViewById<Switch>(R.id.activity_sessao_switch);
        val sessao: Sessao = intent!!.extras!!["sessao"] as Sessao;
        val playButton = findViewById<Button>(R.id.sessao_btn_play);

        val currentUserEmail: String = FirebaseAuth.getInstance().currentUser!!.email.toString();

        sessao.parentReference!!.get()
                .addOnSuccessListener {
                    if((it["email"] as String) == currentUserEmail) {
                        switch.isEnabled = true;
                    }
                }

        sessao.referencia.get()
                .addOnSuccessListener {
                    val sessionActiveStatus = it["isActive"] as Boolean? ?: false
                    switch.isChecked = sessionActiveStatus

                    if(sessionActiveStatus) {
                        val personagens = it["personagens"] as List<DocumentReference>?

                        personagens?.forEach {
                            val userParent = it.parent.parent!!.get()

                            userParent.addOnSuccessListener {
                                if (it["email"] == currentUserEmail) {
                                    playButton.isEnabled = true
                                }
                            }
                        }
                    }
                }

        switch.setOnClickListener {
            if(switch.isChecked)
                sessao.referencia.set(hashMapOf("isActive" to false), SetOptions.merge())
            else {
                val intent: Intent = Intent(this.applicationContext, ChatSessaoActivity::class.java);
                intent.putExtra("sessao", sessao);
                sessao.referencia.set(hashMapOf("isActive" to true), SetOptions.merge());
                startActivity(intent);
            }
        }

        playButton.setOnClickListener {
            val intent: Intent = Intent(this.applicationContext, ChatSessaoActivity::class.java);
            intent.putExtra("sessao", sessao);

            startActivity(intent);
        }
    }
}
