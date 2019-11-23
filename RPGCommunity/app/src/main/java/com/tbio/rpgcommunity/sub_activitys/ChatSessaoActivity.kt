package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
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
    private lateinit var mButtonEnviar: AppCompatImageButton
    private lateinit var mEditxtMensagem: EditText
    private lateinit var sessao: Sessao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_sessao)

        this.mButtonEnviar = findViewById(R.id.activity_chat_sessao_btn_enviar)
        this.mEditxtMensagem = findViewById(R.id.activity_chat_sessao_edt_message)

        val db = FirebaseFirestore.getInstance()
        this.sessao = intent.extras!!["sessao"] as Sessao
        this.mensagens = mutableListOf()

        db.collection("${sessao.caminho}/Histórico de mensagens")
                .limit(50)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener {
                    for(m in it) {
                        this.mensagens.add(Mensagem.toNewObject(m))
                    }

                    this.setMensagensRecyclerView()
                    (activity_chat_sessao_recycler_view.layoutManager as LinearLayoutManager).scrollToPosition(
                            activity_chat_sessao_recycler_view.adapter?.itemCount ?: 1 - 1
                    )
                }

        this.mButtonEnviar.setOnClickListener {
            db.collection("Usuarios")
                    .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email!!)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {
                        for(user in it) {
                            val message = Mensagem( id = null,
                                                    parentId = this.sessao.referencia.id,
                                                    parentReference = this.sessao.referencia,
                                                    de = user.reference,
                                                    para = this.sessao.referencia,
                                                    mensagem = this.mEditxtMensagem.text.toString())

                            db.collection("${this.sessao.caminho}/Histórico de mensagens")
                                    .add(message.toHashMap())
                                    .addOnSuccessListener {
                                        toast("Mensagem enviada")
                                    }
                        }
                    }
        }

        db.collection("${sessao.caminho}/Histórico de mensagens")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w("DebugUpdateMensagens", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    for (doc in value!!.documentChanges) {
                        if(doc.type == DocumentChange.Type.ADDED)
                            mensagens.add(Mensagem.toNewObject(doc = doc.document))
                    }

                    activity_chat_sessao_recycler_view.adapter?.notifyDataSetChanged()
                            ?: setMensagensRecyclerView()

                    (activity_chat_sessao_recycler_view.layoutManager as LinearLayoutManager).scrollToPosition(
                            activity_chat_sessao_recycler_view.adapter?.itemCount ?: 1 - 1
                    )
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
