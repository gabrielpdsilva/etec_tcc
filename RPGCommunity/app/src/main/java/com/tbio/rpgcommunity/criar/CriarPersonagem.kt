package com.tbio.rpgcommunity.criar

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.*
import org.jetbrains.anko.toast
import java.util.*

class CriarPersonagem : AppCompatActivity() {

    // propriedades do xml
    private lateinit var imgPersonagem: ImageView
    private lateinit var btnCriarPersonagem: Button
    private lateinit var edtNickname: EditText
    private lateinit var edtClasse: EditText
    private lateinit var edtGenero: EditText
    private lateinit var edtHabilidades: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var edtHistoria: EditText

    // propriedades do banco
    private var imgPersonagemUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.criar_personagem)

        edtNickname = findViewById(R.id.criar_personagem_edt_nickname)
        edtClasse = findViewById(R.id.criar_personagem_edt_classe)
        edtGenero = findViewById(R.id.criar_personagem_edt_genero)
        //edtHabilidades = findViewById(R.id.criar_personagem_edt_habilidades)
        edtDescricao = findViewById(R.id.criar_personagem_edt_descricao)
        edtHistoria = findViewById(R.id.criar_personagem_edt_historia)

        imgPersonagem = findViewById<ImageView>(R.id.criar_personagem_imv_imagem)
        imgPersonagem.setOnClickListener {
            val intentToGallery = Intent(Intent.ACTION_PICK)
            intentToGallery.type = "image/*"
            startActivityForResult(intentToGallery, Codigos.CODIGO_PARA_REQUISITAR_IMAGEM)
        }

        btnCriarPersonagem = findViewById<Button>(R.id.criar_personagem_btn_criar)
        btnCriarPersonagem.setOnClickListener {

            imgPersonagemUri?.let {
                val filename = UUID.randomUUID()
                FirebaseStorage.getInstance().getReference("/imagens/$filename")
                        .putFile(this.imgPersonagemUri!!)
                        .addOnSuccessListener {
                            it.metadata!!.reference!!.downloadUrl
                                    .addOnSuccessListener {
                                        this.imgPersonagemUri = it
                                        salvarPersonagem()
                                    }
                        }
            } ?: salvarPersonagem()
        }
    }

    private fun salvarPersonagem() {

        //se o nome do personagem estiver vazio
        if(edtNickname.text.toString().isEmpty()){
            edtNickname.error = Divergencias.PERSONAGEM_NOME_VAZIO
            edtNickname.isFocusable = true
        }

        //se a classe do personagem estiver vazia
        else if(edtClasse.text.toString().isEmpty()){
            edtClasse.error = Divergencias.PERSONAGEM_CLASSE_VAZIA
            edtClasse.isFocusable = true
        }

        else
            FirebaseFirestore.getInstance()
                    .collection("Usuarios")
                    .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                    .limit(1)
                    .get()
                    .addOnSuccessListener {
                        for(usuario in it) {
                            Personagem(id = null,
                                       parentId = usuario.id,
                                       nome = Nome(edtNickname.text.toString()),
                                       sessao = null,
                                       descricao = Descricao(edtDescricao.text.toString()),
                                       historia = Historia(historia = edtHistoria.text.toString()),
                                       image = imgPersonagemUri)
                                    .saveDB( fun(doc){
                                        toast("Personagem salvo com sucesso!")
                                        finish()
                                    }, fun (e){
                                        toast(Erros.ERRO_AO_CRIAR_PERSONAGEM)
                                        Log.e(Tags.TAG_ERROR_CPA, e.message.toString() + e.stackTrace)
                                    })
                        }
                    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            Codigos.CODIGO_PARA_REQUISITAR_IMAGEM -> {
                if(resultCode == Activity.RESULT_OK){
                    this.imgPersonagemUri = data!!.data!!

                    val bm = MediaStore.Images.Media.getBitmap(this.contentResolver, imgPersonagemUri)
                    this.imgPersonagem.setImageDrawable(BitmapDrawable(bm))
                }
            }
        }
    }
}