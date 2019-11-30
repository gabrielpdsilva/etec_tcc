package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Codigos
import com.tbio.rpgcommunity.classes_model_do_sistema.Dado
import com.tbio.rpgcommunity.classes_model_do_sistema.Mensagem
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_recycler_view.MensagemAdapter
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.activity_chat_sessao.*
import kotlinx.android.synthetic.main.fragment_sessoes.*
import org.jetbrains.anko.toast

class ChatSessaoActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var mensagens: MutableList<Mensagem>
    private lateinit var mButtonEnviar: AppCompatImageButton
    private lateinit var mEditxtMensagem: EditText
    private lateinit var sessao: Sessao
    private lateinit var btnToChangeBehave: AppCompatImageButton
    private lateinit var btnPlayDice: AppCompatImageButton
    private var currentBehave: Int = Codigos.COMUM
    private var behaviorCount: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_sessao)

        this.mButtonEnviar = findViewById(R.id.activity_chat_sessao_btn_enviar)
        this.mEditxtMensagem = findViewById(R.id.activity_chat_sessao_edt_message)

        val db = FirebaseFirestore.getInstance()
        this.sessao = intent.extras!!["sessao"] as Sessao
        this.mensagens = mutableListOf()
        this.rvChat = findViewById<RecyclerView>(R.id.activity_chat_sessao_recycler_view)
        this.btnToChangeBehave = findViewById(R.id.activity_chat_sessao_btn_acao)
        this.btnPlayDice = findViewById(R.id.activity_chat_sessao_btn_dado)

        this.mButtonEnviar.setOnClickListener {
            val strToSave: String = this.mEditxtMensagem.text.toString()
            this.mEditxtMensagem.text.clear()
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
                                                    mensagem = strToSave,
                                                    comportamento = this.currentBehave)

                            db.collection("${this.sessao.caminho}/Histórico de mensagens")
                                    .add(message.toHashMap())
                        }
                    }
        }

        this.btnPlayDice.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.dado_menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_item_dado_d100 -> {
                        val result = Dado.playDice100()

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    R.id.menu_item_dado_d20 -> {
                        val result = Dado.playDice(20)

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    R.id.menu_item_dado_d12 -> {
                        val result = Dado.playDice(12)

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    R.id.menu_item_dado_d10 -> {
                        val result = Dado.playDice(10)

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    R.id.menu_item_dado_d8 -> {
                        val result = Dado.playDice(8)

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    R.id.menu_item_dado_d6 -> {
                        val result = Dado.playDice(6)

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    R.id.menu_item_dado_d4 -> {
                        val result = Dado.playDice(4)

                        val reference = FirebaseFirestore.getInstance().collection("${sessao.caminho}/Histórico de mensagens")

                        Dado.saveDice(result, reference, it.title.toString())
                    }

                    else -> throw IllegalArgumentException("valor passado para it.itemId inválido")

                }
                true
            }

            popupMenu.show()
        }

        db.collection("${sessao.caminho}/Histórico de mensagens")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w("DebugUpdateMensagens", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    for (doc in value!!.documentChanges) {
                        if (doc.type == DocumentChange.Type.ADDED) {
                            mensagens.add(Mensagem.toNewObject(doc = doc.document))
                        }
                    }

                    rvChat.adapter?.notifyDataSetChanged() ?: this.setMensagensRecyclerView()

                    rvChat.layoutManager?.scrollToPosition(
                            (rvChat.adapter?.itemCount ?: 1) - 1
                    )
                }

        this.btnToChangeBehave.setOnClickListener {
            ++this.behaviorCount
            this.setBehavior()
            Log.i("DebugChatSessao", "Ação: " + this.behaviorCount)
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
            rvChat.adapter = MensagemAdapter(mensagens, this.applicationContext)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(this.applicationContext)
            linearLayout.orientation = RecyclerView.VERTICAL
            rvChat.layoutManager = linearLayout
        }
    }

    fun setBehavior() {
        when(this.behaviorCount) {
            1 -> {
                this.currentBehave = Codigos.ACAO
                btnToChangeBehave.setImageResource(R.mipmap.rpg_render_agir)
            }

            2 -> {
                this.currentBehave = Codigos.FALA
                btnToChangeBehave.setImageResource(R.mipmap.rpg_render_falar)
            }

            3 -> {
                this.currentBehave = Codigos.PENSAMENTO
                btnToChangeBehave.setImageResource(R.mipmap.rpg_render_pensar)
            }

            4 -> {
                this.currentBehave = Codigos.COMUM
                btnToChangeBehave.setImageResource(R.mipmap.rpg_render_comum)
            }

            else -> {
                this.behaviorCount = 1
                btnToChangeBehave.setImageResource(R.mipmap.rpg_render_comum)
                this.setBehavior()
            }
        }
    }
}
