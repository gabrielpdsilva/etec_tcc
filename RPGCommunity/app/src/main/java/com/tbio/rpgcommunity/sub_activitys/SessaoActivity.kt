package com.tbio.rpgcommunity.sub_activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import org.jetbrains.anko.find

class SessaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessao)

        val switch = findViewById<Switch>(R.id.activity_sessao_switch);
        val sessao: Sessao = intent!!.extras!!["sessao"] as Sessao;

        val currentUserEmail: String = FirebaseAuth.getInstance().currentUser!!.email.toString();

        /*dbRef.collectionGroup("Sessoes")
                .get()
                .addOnSuccessListener {
                    for(s in it) {
                        val u = s.reference.parent.parent!!

                        u.get().addOnSuccessListener {
                            if()
                        }
                    }
                }*/

        sessao.parentReference!!.get()
                .addOnSuccessListener {
                    if((it["email"] as String) == currentUserEmail) {
                        switch.isEnabled = true;
                    }
                }

        switch.setOnClickListener {
            val intent: Intent = Intent(this.applicationContext, ChatSessaoActivity::class.java)
            intent.putExtra("sessao", sessao);
            startActivity(intent);
        }
    }
}
