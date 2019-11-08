package com.tbio.rpgcommunity.criar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.toast
import java.util.*

class CriarSessao : AppCompatActivity() {

    // propriedades do XML
    private lateinit var btnSelecionarImagem: Button
    private lateinit var btnCriarSessao: Button
    private lateinit var btnAdicionarJogador: Button
    private lateinit var imagemSessao: ImageView

    // propriedades do banco
    private var uriImagemSelecionada: Uri? = null
    private var personagens: MutableList<DocumentReference>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.criar_sessao)

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
                                    .saveDB(fun (doc) {
                                        toast(R.string.sessao_salva_com_sucesso)
                                        finish()
                                    }, fun (exception) {
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
        }
    }
}