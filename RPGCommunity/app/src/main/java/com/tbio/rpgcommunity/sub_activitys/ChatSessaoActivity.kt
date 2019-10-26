package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Mensagem
import com.tbio.rpgcommunity.classes_recycler_view.MensagemAdapter
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.fragment_sessoes.*

class ChatSessaoActivity : AppCompatActivity() {

    private lateinit var mensagens: MutableList<Mensagem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_sessao)

        // continuar daqui com a requisição
        // das mensagens no banco de dados
    }

    fun setSessionRecyclerView() {
        // define a progressbar como invisível
        findViewById<ProgressBar>(R.id.PbSessoes).visibility = View.GONE

        if (mensagens.size > 0) {
            // define a visibilidade de 'Não existem sessões disponíveis' como false
            txtNaoExistemSessoesSuas.visibility = View.GONE

            // define o adapter da RecyclerView
            fragment_sessoes_recycler_view.adapter = MensagemAdapter(mensagens, this.applicationContext)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(this.applicationContext)
            linearLayout.orientation = RecyclerView.VERTICAL
            fragment_sessoes_recycler_view.layoutManager = linearLayout
        } else {
            txtNaoExistemSessoesSuas.visibility = View.VISIBLE
        }
    }
}
