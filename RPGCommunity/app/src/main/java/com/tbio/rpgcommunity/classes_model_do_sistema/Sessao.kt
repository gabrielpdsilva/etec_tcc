/** @author: Davi Mendes Pimentel */
package com.tbio.rpgcommunity.classes_model_do_sistema

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.provider.DocumentsContract
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.w3c.dom.Document
import java.lang.IllegalArgumentException

class Sessao (
              // dados do documento
        id: String?,
        parentId: String,

              // dados da Sessão
        val nome: Nome, // Nome da sessão
        val imagem: Uri? = null,  // foto da sessão
        var personagens: MutableList<String>? = null, // lista de personagens envolvidos
        var historiaDeUniverso: Historia? = null, // história do Universo da Sessão
        var sistema: String? = null,  // sistema da Sessão
        var descricao: Descricao? = null // descrição da Sessão
        )
    : SubDocumentoRpgItem(id, parentId) {
    init {
        this.referencia.get()
                .addOnSuccessListener {
                    this.isActive = it["isActive"] as Boolean? ?: false
                }
    }

    override var parentReference: DocumentReference? = null
        get() = this.referencia.parent.parent

    var isActive: Boolean = false


    fun saveSessionState(newState: Boolean) {
            this.referencia
                    .set(hashMapOf<String, Boolean>("isActive" to newState), SetOptions.merge())

            this.referencia
                    .get()
                    .addOnSuccessListener{
                        Log.i("DebugActivitySessao", "isActive = ${it["isActive"]}")
                    }

            this.isActive = newState
    }

    fun getSessionState(funcBeforeStart: ((newSession: DocumentSnapshot) -> Any?)? = null) {
        this.referencia.get()
                .addOnSuccessListener {
                    funcBeforeStart?.invoke(it)
                }
    }

    var visibilidadeDeAcesso: Int = 0
        set(newVisibilidade) {
            if(newVisibilidade in (Visibilidade.VISIBILIDADE_PUBLICA - 1) .. (Visibilidade.VISIBILIDADE_PRIVADA_NAO_RIGIDA + 1)){
                field = newVisibilidade
            }
            else throw IllegalArgumentException("valor passado para 'visibilidadeDeAcesso' inválido")
        }

    // retorna um Map do objeto atual
    override fun toHashMap(): HashMap<String, Any?> {
        val sessaoHashMap = hashMapOf<String, Any?>() // variável de retorno

        // serializa o objeto
        with(sessaoHashMap) {
            val personagensToSave: MutableList<DocumentReference> = mutableListOf()

            put("nome", nome.toHashMap())
            put("historiaDeUniverso", historiaDeUniverso?.toHashMap())
            put("sistema", sistema)
            put("descricao", descricao?.toHashMap())
            put("imagem", imagem.toString())
            put("likes", 0)
            put("deslikes", 0)
            personagens?.forEach {
                personagensToSave.add(FirebaseFirestore.getInstance().document(it))
            }
            put("personagens", personagensToSave)
            put("isActive", this@Sessao.isActive)
        }

        return sessaoHashMap // retorna o nosso Map do objeto
    }

    override val caminho: String
        get() = referencia.path

    override val referencia: DocumentReference
        get() = FirebaseFirestore
            .getInstance()
            .document("Usuarios/${this.getParentId()}/Sessoes/${this.getId()}")

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Nome::class.java.classLoader),
            parcel.readParcelable(Uri::class.java.classLoader),
            parcel.createStringArrayList(),
            parcel.readParcelable(Historia::class.java.classLoader),
            parcel.readString(),
            parcel.readParcelable(Descricao::class.java.classLoader)) {
        this.isActive = parcel.readByte() == 1.toByte()
        parcel.readList(this.personagens, DocumentReference::class.java.classLoader)
    }

    override fun toObject(doc: DocumentSnapshot) { }

    companion object CREATOR : Parcelable.Creator<Sessao>{
        override fun createFromParcel(parcel: Parcel): Sessao = Sessao(parcel)
        override fun newArray(size: Int): Array<Sessao?> = arrayOfNulls(size)

        fun toNewObject(doc: DocumentSnapshot): RpgItem{
            val nome = Nome(doc["nome.nome"].toString(),
                            (doc["nome.sobrenome"] as List<String>?))
            val personagens: MutableList<String> = mutableListOf()

            (doc["personagens"] as List<DocumentReference>?)?.forEach {
                personagens.add(it.path)
            }

            @Suppress("UNCHECKED_CAST")
            val s = Sessao(id = doc.id,
                    parentId = doc.reference.parent.parent!!.id,
                    nome = nome,
                    personagens = if(personagens.isEmpty()) null else personagens,
                    historiaDeUniverso = Historia.toNewObject(doc["historiaDeUniverso"] as Map<String, Any?>?) as Historia?,
                    sistema = doc["sistema"] as String,
                    descricao = Descricao.toNewObject(doc.get("descricao") as Map<String, Any?>) as Descricao,
                    imagem = (doc["imagem"] as String?)?.toUri()
                    )
            s.isActive = doc["isActive"] as Boolean? ?: false

            return s
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.getId())
        parcel.writeString(this.getParentId())
        parcel.writeParcelable(nome, flags)
        parcel.writeParcelable(imagem, flags)
        parcel.writeStringList(personagens)
        parcel.writeParcelable(historiaDeUniverso, flags)
        parcel.writeString(sistema)
        parcel.writeParcelable(descricao, flags)
        parcel.writeByte(if(isActive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }
}