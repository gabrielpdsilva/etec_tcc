/** @author: Davi Mendes Pimentel */
package com.tbio.rpgcommunity.classes_model_do_sistema

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.core.net.toUri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Personagem (
                  // dados do documento
                  id: String?,   // id do documento
                  parentId: String, // id do Usuário a quem pertence este personagem

                  // dados do personagem (dados estes que vão para o banco)
                  val nome: Nome,   // nome do personagem
                  var sessao: DocumentReference? = null,
                  var descricao: Descricao? = null,
                  var historia: Historia? = null,
                  var image: Uri? = null)
    : SubDocumentoRpgItem(id, parentId){
    override var parentReference: DocumentReference? = null
        get() = this.referencia.parent.parent

    constructor(id: String?,
                parentId: String,
                nome: Nome,
                sessao: DocumentReference? = null,
                descricao: Descricao? = null,
                historia: Historia? = null,
                image: Uri? = null,
                initPersonagemFunction: Personagem.() -> Unit) : this(id, parentId, nome, sessao, descricao, historia, image) {
        this.initPersonagemFunction()
    }

    // valida a visibilidade do personagem
    var visibilidadeDeAcesso: Int = 0
        set(newVisibilidade) {
            if(newVisibilidade in (Visibilidade.VISIBILIDADE_PUBLICA - 1 .. Visibilidade.VISIBILIDADE_PRIVADA_NAO_RIGIDA + 1)){
                field = newVisibilidade
            }
            //else throw IllegalArgumentException("valor passado para 'visibilidadeDeAcesso' inválido")
        }

    // valida se o personagem tem um sobrenome
    val hasSobreNome: Boolean = this.nome.hasSobreNome

    // retorna o objeto serializado em um HashMap
    override fun toHashMap(): HashMap<String, Any?> {
        val personagemHashMap: HashMap<String, Any?> = hashMapOf()

        with(personagemHashMap) {
            put("nome", nome.toHashMap())
            put("descricao", descricao?.toHashMap())
            put("historia", historia?.toHashMap())
            put("imagem", image.toString())
            sessao?.let {
                this.put("sessao", it)
            }
        }

        return personagemHashMap
    }

    // especifica (em uma String) o caminho do item no banco de dados
    override val caminho: String
        get () = referencia.path

    // referencia no banco de dados para o personagem atual
    override val referencia: DocumentReference
        get() = FirebaseFirestore
            .getInstance()
            .document("Usuarios/${this.getParentId()}/Personagens/${this.getId()}")

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable<Nome>(Nome::class.java.classLoader),
            null,
            parcel.readParcelable<Descricao>(Descricao::class.java.classLoader),
            parcel.readParcelable<Historia>(Historia::class.java.classLoader),
            parcel.readParcelable(Uri::class.java.classLoader)){
        parcel.readString()?.let {
            FirebaseFirestore.getInstance().document(it)
                    .get()
                    .addOnSuccessListener {
                        this.sessao = it.reference
                    }
        }
    }

    override fun toObject(doc: DocumentSnapshot) {
        // recupera a descrição do personagem
        @Suppress("UNCHECKED_CAST") // supressiona o aviso de cast não checado
        this.descricao = Descricao(doc["descricaoBasica"] as String,
                                   doc["campos"] as MutableMap<String, Any>)
        // recupera o Map 'historia' do banco de dados
        val historiaHash = doc["historia"] as HashMap<*, *>

        // recupera o Nome da 'historia' do banco de dados
        val historiaNomeRef = doc["nome"] as HashMap<String, Any?>?
        var nomeHistoria: Nome? = null

        // verifica se a história tem nome
        historiaNomeRef?.let {

            // recupera a história do personagem
            @Suppress("UNCHECKED_CAST") // supressiona o aviso de cast não checado
            nomeHistoria =
                    Nome((doc["nome"] as HashMap<String, Any?>).get("nome") as String,
                         (doc["nome"] as HashMap<String, Any?>).get("sobrenome") as List<String>?)

        }

        // define a historia do personagem com a nova historia do banco
        this.historia =
            Historia(nome = nomeHistoria,
                     personagensEnvolvidos = (historiaHash.get("personagensEnvolvidos") as Array<DocumentReference>).toMutableList(),
                     historia = historiaHash.get("historia") as String)

    }

    companion object CREATOR : Parcelable.Creator<Personagem>{
        override fun createFromParcel(parcel: Parcel): Personagem = Personagem(parcel)
        override fun newArray(size: Int): Array<Personagem?> = arrayOfNulls(size)

        // instancia um novo objeto-personagem com baso nos dados do documento do Banco de Dados
        fun toNewObject(doc: DocumentSnapshot): RpgItem{
            val nome = Nome((doc["nome"] as HashMap<String, Any?>).get("nome").toString(),
                            (doc["nome"] as HashMap<String, Any?>).get("sobrenome") as List<String>?)

            @Suppress("UNCHECKED_CAST")
            return Personagem(id = doc.id,
                              parentId = doc.reference.parent.parent!!.id,
                              nome = nome,
                              sessao = doc["sessao"] as DocumentReference?,
                              descricao = Descricao.toNewObject(doc["descricao"] as Map<String, Any?>) as Descricao?,
                              historia = Historia.toNewObject(doc["historia"] as Map<String, Any?>?) as Historia?,
                              image = (doc["imagem"] as String?)?.toUri())
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.getId())
        parcel.writeString(this.getParentId())
        parcel.writeParcelable(nome, flags)
        parcel.writeParcelable(descricao, flags)
        parcel.writeParcelable(historia, flags)
        parcel.writeParcelable(image, flags)
        parcel.writeString(sessao?.path)
    }

    override fun describeContents(): Int {
        return 0
    }
}