package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Mensagem
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_recycler_view.MensagemAdapter
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.activity_chat_sessao.*
import kotlinx.android.synthetic.main.fragment_sessoes.*
import org.jetbrains.anko.toast

class ChatSessaoActivity : AppCompatActivity() {

    private lateinit var mensagens: MutableList<Mensagem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_sessao)

        val db = FirebaseFirestore.getInstance()
        val sessao = intent.extras!!["sessao"] as Sessao
        this.mensagens = mutableListOf()

        db.collection("${sessao.caminho}/Histórico de mensagens")
                .get()
                .addOnSuccessListener {
                    for(m in it) {
                        this.mensagens.add(Mensagem.toNewObject(m))
                    }

                    this.setMensagensRecyclerView()
                }
    }

    fun setMensagensRecyclerView() {
        // define a progressbar como invisível
        // val progressBar = findViewById<ProgressBar>(R.id.PbMensagens)!!
        // progressBar.visibility = View.GONE

        if (mensagens.size > 0) {
            // Log.d("DebugChat", findViewById<ProgressBar>(R.id.PbMensagens).visibility.toString(), Exception("passou por aqui"));
            // Log.d("DebugChat", findViewById<ProgressBar>(R.id.PbMensagens).context.toString(), Exception("passou por aqui"));
            // define o adapter da RecyclerView
            activity_chat_sessao_recycler_view.adapter = MensagemAdapter(mensagens, this.applicationContext)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(this.applicationContext)
            linearLayout.orientation = RecyclerView.VERTICAL
            activity_chat_sessao_recycler_view.layoutManager = linearLayout
        } else {

        }
    }
}
