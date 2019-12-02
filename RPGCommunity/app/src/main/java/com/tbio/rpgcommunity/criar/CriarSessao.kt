package com.tbio.rpgcommunity.criar

import android.app.Activity
import android.app.Person
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.*
import com.tbio.rpgcommunity.classes_recycler_view.PersonagemAdapter
import com.tbio.rpgcommunity.sub_activitys.AdicionarJogadorActivity
import kotlinx.android.synthetic.main.criar_sessao.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.toast
import java.util.*
import com.tbio.rpgcommunity.classes_model_do_sistema.Codigos
import org.jetbrains.anko.imageResource
import java.io.File

class CriarSessao : AppCompatActivity() {

    // propriedades do XML
    private lateinit var btnSelecionarImagem: Button
    private lateinit var btnCriarSessao: Button
    private lateinit var btnAdicionarJogador: Button
    private lateinit var imagemSessao: ImageView

    // propriedades do banco
    private lateinit var db: FirebaseFirestore
    private var uriImagemSelecionada: Uri? = null
    private lateinit var personagens: MutableList<String>
    private lateinit var p: MutableList<Personagem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.criar_sessao)

        this.db = FirebaseFirestore.getInstance()

        this.personagens = mutableListOf<String>()
        this.p = mutableListOf<Personagem>()
        this.imagemSessao = findViewById(R.id.sessaoImagem)

        if(savedInstanceState != null) {
            this.p = savedInstanceState.getParcelableArrayList<Personagem>("personagensSelecionados").toMutableList() as ArrayList<Personagem>? ?: mutableListOf()
            this.uriImagemSelecionada = savedInstanceState.getString("oldImageSession")?.toUri()

            if(this.uriImagemSelecionada != null) {
                // mostra a imagem selecionada no ImageButton
                val bm = MediaStore.Images.Media.getBitmap(contentResolver, this.uriImagemSelecionada)
                imagemSessao.setImageDrawable(BitmapDrawable(bm))
            }
        }

        this.setRecyclerView(this.p);

        // define o botão da foto da Sessão
        btnSelecionarImagem = findViewById<Button>(R.id.sessaoBotaoImagem)
        btnSelecionarImagem.setOnClickListener {
            val intentToGallery = Intent(Intent.ACTION_PICK)
            intentToGallery.type = "image/*"

            startActivityForResult(intentToGallery, Codigos.CODIGO_PARA_REQUISITAR_IMAGEM)
        }

        // define o botão 'adicionar jogadores'
        btnAdicionarJogador = findViewById(R.id.sessaoBotaoAddJogadores)
        btnAdicionarJogador.setOnClickListener {
            val intent = Intent(this, AdicionarJogadorActivity::class.java)
            startActivityForResult(intent, Codigos.CODIGO_PARA_ADICIONAR_JOGADOR);
        }

        // define o botão 'criar sessão'
        btnCriarSessao = findViewById(R.id.sessaoBotaoCriarSessao)
        btnCriarSessao.setOnClickListener {

            this.uriImagemSelecionada?.let {
                val randomName = UUID.randomUUID()
                FirebaseStorage.getInstance().getReference("/imagens/$randomName")
                        .putFile(it)
                        .addOnSuccessListener {
                            it.metadata!!.reference!!.downloadUrl
                                    .addOnSuccessListener {
                                        this.uriImagemSelecionada = it
                                        salvarSessao()
                                    }
                        }
            } ?: salvarSessao()
        }
    }

    private fun setRecyclerView(p: MutableList<Personagem>) {
        // define o adaptador de RecyclerView de personagens
        rvJogadores.adapter = PersonagemAdapter(applicationContext, p)

        // define o layout
        val mLayout = LinearLayoutManager(this)
        mLayout.orientation = RecyclerView.VERTICAL
        rvJogadores.layoutManager = mLayout
    }

    private fun salvarSessao() {

        if(this.uriImagemSelecionada == null){
            toast(R.string.sessao_sem_imagem);
            return;
        }

        val nome = findViewById<EditText>(R.id.sessaoTituloEt).text.toString()
        val sistema = findViewById<EditText>(R.id.sessaoSistemaEt).text.toString()
        val descricao = findViewById<EditText>(R.id.sessaoDescricaoEt).text.toString()

        if(nome.isEmpty()){
            findViewById<EditText>(R.id.sessaoTituloEt).error = Divergencias.SESSAO_NOME_VAZIO
            findViewById<EditText>(R.id.sessaoTituloEt).isFocusable = true
        }
        else if(sistema.isEmpty()){
            findViewById<EditText>(R.id.sessaoSistemaEt).error = Divergencias.SESSAO_SISTEMA_VAZIO
            findViewById<EditText>(R.id.sessaoSistemaEt).isFocusable = true

        }
        else if(descricao.isEmpty()){
            findViewById<EditText>(R.id.sessaoDescricaoEt).error = Divergencias.SESSAO_DESCRICAO_VAZIA
            findViewById<EditText>(R.id.sessaoDescricaoEt).isFocusable = true
        }

        else
            FirebaseFirestore.getInstance()
                    .collection("Usuarios")
                    .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                    .limit(1)
                    .get()
                    .addOnSuccessListener {
                        for(usuario in it){
                            Sessao(id = null,
                                   parentId = usuario.id,
                                   nome = Nome(nome),
                                   imagem = this.uriImagemSelecionada,
                                   personagens = this.personagens,
                                   sistema = sistema,
                                   descricao = Descricao(descricao))
                                    .saveDB(funcSuccessListener = fun (doc) {
                                        doc.get()
                                                .addOnSuccessListener {
                                                    val s: Sessao = Sessao.toNewObject(it) as Sessao

                                                    s.parentReference!!.get()
                                                            .addOnSuccessListener {u ->
                                                                // split the description and name by ' ' spaces values
                                                                val splitedDescription = s.descricao?.getDescricaoBasica()?.splitToSequence(' ', ignoreCase = true)
                                                                val splitedName = s.nome.nome.splitToSequence(' ', ignoreCase = true)
                                                                val splitedNameMaster = (u["nickname.nome"] as String).splitToSequence(' ', ignoreCase = true)

                                                                // define the search key array list to save in 'Pesquisas'
                                                                val searchKeyList: ArrayList<String> = arrayListOf<String>()

                                                                splitedDescription?.forEach {strFull ->
                                                                    val splitedValueChuncked = strFull.chunked(3)

                                                                    splitedValueChuncked.forEach {strPieces ->
                                                                        searchKeyList.add(strPieces.toLowerCase())
                                                                    }
                                                                }

                                                                splitedName.forEach {strFull ->
                                                                    val splitedValueChuncked = strFull.chunked(3)

                                                                    splitedValueChuncked.forEach {strPieces ->
                                                                        searchKeyList.add(strPieces.toLowerCase())
                                                                    }
                                                                }

                                                                splitedNameMaster.forEach {strFull ->
                                                                    val splitedValueChuncked = strFull.chunked(3)

                                                                    splitedValueChuncked.forEach {strPieces ->
                                                                        searchKeyList.add(strPieces.toLowerCase())
                                                                    }
                                                                }

                                                                val hashToBeSavedOnPesquisas = hashMapOf<String, Any?>(
                                                                        "searchKeyList" to searchKeyList,
                                                                        "docReference" to s.referencia,
                                                                        "searchedTimes" to 0,
                                                                        "docTypeClass" to "sessao"
                                                                )

                                                                FirebaseFirestore.getInstance().collection("Pesquisas")
                                                                        .add(hashToBeSavedOnPesquisas)
                                                                        .addOnFailureListener {
                                                                            Log.i("DebugCriarSessao", it.message)
                                                                        }

                                                                toast(R.string.sessao_salva_com_sucesso)
                                                                finish()
                                                            }

                                                }

                                    }, funcFailListener = fun (exception) {
                                        toast(Erros.ERRO_AO_CRIAR_SESSAO)
                                        finish()
                                    })
                        }
                    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            // recebe a imagem
            Codigos.CODIGO_PARA_REQUISITAR_IMAGEM -> {
                if(resultCode == Activity.RESULT_OK){
                    // armazena o caminho da imagem na nossa proprieda URI
                    this.uriImagemSelecionada = data!!.data
                    // mostra a imagem selecionada no ImageButton
                    val bm = MediaStore.Images.Media.getBitmap(contentResolver, this.uriImagemSelecionada)
                    imagemSessao.setImageDrawable(BitmapDrawable(bm))
                }
            }

            Codigos.CODIGO_PARA_ADICIONAR_JOGADOR -> {
                val personagemResult =
                        data?.extras?.getParcelable<Personagem>("personagem_selecionado")

                if(personagemResult != null) {
                    if (!p.contains(personagemResult)) {
                        p.add(personagemResult)
                        personagens.add(personagemResult.referencia.path)
                        this.setRecyclerView(p)
                    }
                }
            }
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(uriImagemSelecionada != null)
            outState.putString("oldImageSession", uriImagemSelecionada.toString())

        outState.putParcelableArrayList("personagensSelecionados", ArrayList(p))
    }
}