package com.tbio.rpgcommunity.sub_activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import com.tbio.rpgcommunity.classes_model_do_sistema.Sessao
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import com.tbio.rpgcommunity.classes_recycler_view.PersonagemAdapter
import org.jetbrains.anko.find

class SessaoActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var rvSessao: RecyclerView
    private lateinit var txtSessaoNome: TextView
    private lateinit var edtSessaoSistema: TextView
    private lateinit var edtSessaoDescricao: TextView
    private lateinit var edtSessaoMestre: TextView
    private lateinit var imgSessao: ImageView
    private var personagens: MutableList<Personagem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessao)

        if(savedInstanceState != null) {
            this.personagens = savedInstanceState.getParcelableArrayList<Personagem>("personagens")!!.toMutableList()
        }

        val switch = findViewById<Switch>(R.id.activity_sessao_switch);
        val sessao: Sessao = intent!!.extras!!["sessao"] as Sessao;
        val playButton = findViewById<Button>(R.id.sessao_btn_play);
        this.title = "Sessão: " + sessao.nome.nome;
        this.rvSessao = findViewById(R.id.rvSessao)
        this.edtSessaoDescricao = findViewById(R.id.dinamic_txt_descricao_sessao)
        this.edtSessaoSistema = findViewById(R.id.dinamic_txt_sistema_sessao)
        this.edtSessaoMestre = findViewById(R.id.dinamic_txt_mestre_sessao)
        this.txtSessaoNome = findViewById(R.id.txt_titulo_sessao)
        this.imgSessao = findViewById(R.id.sessao_img_profile_sessao)

        sessao.personagens?.forEach {
            db.document(it)
                    .get()
                    .addOnSuccessListener {
                        this.personagens.add(Personagem.toNewObject(it) as Personagem)

                        this.rvSessao.adapter?.notifyDataSetChanged()
                                ?: this.setRecyclerView()
                    }
        }

        this.edtSessaoSistema.text = sessao.sistema ?: "Sem sistema"
        this.edtSessaoDescricao.text = sessao.descricao?.getDescricaoBasica() ?: "Sem Descrição"
        this.txtSessaoNome.text = sessao.nome.nome
        sessao.parentReference!!.get()
                .addOnSuccessListener {
                    this.edtSessaoMestre.text = it["nickname.nome"].toString()
                }

        Picasso.get()
                .load(sessao.imagem)
                .resize(350, 150)
                .into(this.imgSessao)

        val currentUserEmail: String = FirebaseAuth.getInstance().currentUser!!.email.toString();

        sessao.getSessionState {
            switch.isChecked = it["isActive"] as Boolean
            Log.i("DebugActivitySessao", "Valor de isActive fora do chat: ${sessao.isActive}")
        }

        sessao.parentReference!!.get()
                .addOnSuccessListener {
                    if((it["email"] as String) == currentUserEmail) {
                        switch.isEnabled = true;

                        if(switch.isChecked)
                            playButton.isEnabled = true;
                    }
                }

        sessao.getSessionState {
            if (it["isActive"] as Boolean) {
                val personagens = mutableListOf<DocumentReference>()

                sessao.personagens?.forEach {
                    personagens.add(db.document(it))
                }

                personagens.forEach {
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
            val statusSession = sessao.isActive
            sessao.saveSessionState(! sessao.isActive)
            playButton.isEnabled = ! playButton.isEnabled

            if(! statusSession) {
                val intent: Intent = Intent(this.applicationContext, ChatSessaoActivity::class.java);
                intent.putExtra("sessao", sessao);
                startActivity(intent);
            }
        }

        playButton.setOnClickListener {
            val intent: Intent = Intent(this.applicationContext, ChatSessaoActivity::class.java);
            intent.putExtra("sessao", sessao);

            startActivity(intent);
        }
    }

    private fun setRecyclerView() {
        this.rvSessao.adapter = PersonagemAdapter(this, this.personagens) {
            val intent: Intent = Intent(this, PersonagemActivity::class.java)
            intent.putExtra("personagem", it)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        val mLyt = LinearLayoutManager(this)
        mLyt.orientation = RecyclerView.VERTICAL
        this.rvSessao.layoutManager = mLyt
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)

        val mArrayPersonagens = arrayListOf<Personagem>()

        this.personagens.forEach {
            mArrayPersonagens.add(it)
        }

        outState!!.putParcelableArrayList("personagens", mArrayPersonagens)
    }
}
