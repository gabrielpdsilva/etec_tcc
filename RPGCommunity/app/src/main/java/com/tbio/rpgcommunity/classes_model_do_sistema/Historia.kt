/** @author: Davi Mendes Pimentel */
package com.tbio.rpgcommunity.classes_model_do_sistema

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class Historia (val nome: Nome? = null,    // nome, caso a história tenha nome
                private var personagensEnvolvidos: MutableList<DocumentReference>? = null, // personagens envolvidos na história
                private var historia: String) // história de fato
    : RpgItem(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Nome::class.java.classLoader),
            mutableListOf(),
            parcel.readString()) {
        this.personagensEnvolvidos?.apply {
            parcel.readList(this, MutableList::class.java.classLoader)
        }
    }

    // define a nova história
    fun setHistoria(novaHistoria: String){
        if(novaHistoria.isNotEmpty())
            this.historia = novaHistoria
    }

    // retorna a história
    fun getHistoria() = this.historia

    // adiciona um novo personagem na lista
    fun addPersonagem(newPersonagem: DocumentReference) {
        if(this.personagensEnvolvidos != null)  // verifica se existem personagens envolvidos
            this.personagensEnvolvidos?.add(newPersonagem)  // adiciona o novo personagem
        else this.personagensEnvolvidos = mutableListOf<DocumentReference>(newPersonagem)  // caso contrário cria uma lista de personagens
    }
    // remove o personagem da lista
    fun deletePersonagem(index: Int) = this.personagensEnvolvidos?.removeAt(index)
    // retorna o personagem da lista
    fun getPersonagem(index: Int) = this.personagensEnvolvidos?.get(index)
    // retorna a lista de personagens
    fun getPersonagens() = this.personagensEnvolvidos

    // retorna o objeto serializado em um HashMap
    override fun toHashMap(): HashMap<String, Any?> {
        val historiaHashMap: HashMap<String, Any?> = hashMapOf()

        historiaHashMap.put("nome", this.nome?.toHashMap())
        historiaHashMap.put("personagensEnvolvidos", personagensEnvolvidos)
        historiaHashMap.put("historia", this.historia)

        return historiaHashMap
    }

    override fun toObject(doc: DocumentSnapshot) {
        @Suppress("UNCHECKED_CAST")
        this.personagensEnvolvidos = (doc.get("personagensEnvolvidos") as Array<DocumentReference>).toMutableList()
        this.historia = doc.get("historia") as String
    }

    companion object CREATOR : Parcelable.Creator<Historia>{
        override fun createFromParcel(parcel: Parcel): Historia = Historia(parcel)
        override fun newArray(size: Int): Array<Historia?> = arrayOfNulls(size)

        fun toNewObject(map: Map<String, Any?>?): RpgItem?{
            @Suppress("UNCHECKED_CAST")
            map?.let {

                val historiaNomeRef = map["nome"] as HashMap<String, Any?>?
                var historiaNome: Nome? = null

                // verifica se existe história
                historiaNomeRef?.let {
                    historiaNome = Nome((map["nome"] as HashMap<String, Any?>).get("nome") as String,
                                        (map["nome"] as HashMap<String, Any?>).get("sobrenome") as List<String>?)
                }

                return Historia(
                        nome = historiaNome,
                        personagensEnvolvidos = (map["personagensEnvolvidos"] as Array<DocumentReference>?)?.toMutableList(),
                        historia = map["historia"] as String
                )
            }

            return null
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(nome, flags)
        parcel.writeString(historia)
        parcel.writeList(personagensEnvolvidos)
    }

    override fun describeContents(): Int {
        return 0
    }
}