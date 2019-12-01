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
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.*
import com.tbio.rpgcommunity.classes_recycler_view.CampoAdapter
import org.jetbrains.anko.toast
import java.util.*

class CriarPersonagem : AppCompatActivity() {

    // propriedades do xml
    private lateinit var rvPersonagemAddField: RecyclerView
    private lateinit var imgPersonagem: ImageView
    private lateinit var btnCriarPersonagem: Button
    private lateinit var btnAddCampo: ImageButton
    private lateinit var btnDeleteCampo: ImageButton
    private lateinit var edtNickname: EditText
    private lateinit var edtGenero: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var edtHistoria: EditText
    private lateinit var campos: CampoMap<String, Any?>
    private var keys = mutableListOf<String>()
    private var values = mutableListOf<String>()

    // propriedades do banco
    private var imgPersonagemUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.criar_personagem)

        this.campos = CampoMap()
        edtNickname = findViewById(R.id.criar_personagem_edt_nickname)
        edtGenero = findViewById(R.id.criar_personagem_edt_genero)
        edtDescricao = findViewById(R.id.criar_personagem_edt_descricao)
        edtHistoria = findViewById(R.id.criar_personagem_edt_historia)
        rvPersonagemAddField = findViewById(R.id.rvPersonagemAddField)
        btnAddCampo = findViewById(R.id.criar_personagem_btn_add_campo)
        btnDeleteCampo = findViewById(R.id.criar_personagem_btn_delete_campo)

        this.setRecyclerView()

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

                                        var i = 0

                                        for(key in keys) {
                                            if(keys[i].isBlank() || keys[i].isEmpty())
                                                continue

                                            if(values[i].isBlank() || values[i].isEmpty())
                                                continue

                                            this.campos.put(keys[i], values[i])

                                            ++i
                                        }

                                        salvarPersonagem()
                                    }
                        }
            } ?: salvarPersonagem()
        }

        btnAddCampo.setOnClickListener {
            /*this.campos.put("key" + (this.campos.size + 1), "value")

            this.rvPersonagemAddField.adapter?.notifyDataSetChanged()
            Log.i("DebugAddCampo", this.campos.toString())*/
            this.keys.add(this.keys.size, "key" + this.keys.size)
            this.values.add(this.values.size, "value" + this.values.size)

            this.rvPersonagemAddField.adapter?.notifyDataSetChanged()
                    ?: this.setRecyclerView()

            Log.i("DebugAddCampo", this.keys.toString())
            Log.i("DebugAddCampo", this.values.toString())
        }

        btnDeleteCampo.setOnClickListener {

            if(this.keys.size > 0) {
                this.keys.removeAt(this.keys.size - 1)
                this.values.removeAt(this.values.size - 1)

                this.rvPersonagemAddField.adapter?.notifyDataSetChanged()
                        ?: this.setRecyclerView()

                Log.i("DebugAddCampo", this.keys.toString())
                Log.i("DebugAddCampo", this.values.toString())
            }
        }
    }

    private fun salvarPersonagem() {

        //se o nome do personagem estiver vazio
        if(edtNickname.text.toString().isEmpty()){
            edtNickname.error = Divergencias.PERSONAGEM_NOME_VAZIO
            edtNickname.isFocusable = true
        }

        //se a classe do personagem estiver vazia
        else if(edtGenero.text.toString().isEmpty()){
            edtGenero.error = Divergencias.PERSONAGEM_GENERO_VAZIO
            edtGenero.isFocusable = true
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
                                       image = imgPersonagemUri,
                                       campos = this.campos,
                                       genero = this.edtGenero.text.toString())
                                    .saveDB(funcSuccessListener =  fun(doc){

                                        doc.get()
                                                .addOnSuccessListener {
                                                    val p = Personagem.toNewObject(it) as Personagem

                                                    p.parentReference!!.get()
                                                            .addOnSuccessListener {u ->
                                                                // split the description and name by ' ' spaces values
                                                                val splitedDescription = p.descricao?.getDescricaoBasica()?.splitToSequence(' ', ignoreCase = true)
                                                                val splitedName = p.nome.nome.splitToSequence(' ', ignoreCase = true)
                                                                val splitedNameCreator = (u["nickname.nome"] as String).splitToSequence(' ', ignoreCase = true)

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

                                                                splitedNameCreator.forEach {strFull ->
                                                                    val splitedValueChuncked = strFull.chunked(3)

                                                                    splitedValueChuncked.forEach {strPieces ->
                                                                        searchKeyList.add(strPieces.toLowerCase())
                                                                    }
                                                                }

                                                                val hashToBeSavedOnPesquisas = hashMapOf<String, Any?>(
                                                                        "searchKeyList" to searchKeyList,
                                                                        "docReference" to p.referencia,
                                                                        "searchedTimes" to 0,
                                                                        "docTypeClass" to "personagem"
                                                                )

                                                                FirebaseFirestore.getInstance().collection("Pesquisas")
                                                                        .add(hashToBeSavedOnPesquisas)
                                                                        .addOnFailureListener {e ->
                                                                            Log.i("DebugCriarPersonagem", e.message)
                                                                        }

                                                                toast("Personagem salvo com sucesso!")
                                                                finish()
                                                            }
                                                }
                                    }, funcFailListener = fun (e){
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

    fun setRecyclerView() {
        this.rvPersonagemAddField.adapter = CampoAdapter(this.keys, this,
                onClickFunc = {
                    /*val keys = this.campos.keys.toMutableList()
                    this.campos.remove(keys[it])

                    this.rvPersonagemAddField.adapter!!.notifyDataSetChanged()*/
                    this.keys.removeAt(it)

                    this.rvPersonagemAddField.adapter?.notifyDataSetChanged()
                            ?: this.setRecyclerView()

                    Log.i("DebugAddCampo", this.keys.toString())
                }, onKeyTextChange = { str, pos ->
                    this.keys.removeAt(pos)
                    this.keys.add(pos, str)

                    Log.i("DebugAddCampo", this.keys.toString() + " : pos = ${pos}")
                }, onValueTextChange = { str, pos ->
                    this.values.removeAt(pos)
                    this.values.add(pos, str)

                    Log.i("DebugAddCampo", this.values.toString() + " : pos = ${pos}")
                })

        val mLayout = LinearLayoutManager(this)
        mLayout.orientation = RecyclerView.VERTICAL
        this.rvPersonagemAddField.layoutManager = mLayout
    }
}