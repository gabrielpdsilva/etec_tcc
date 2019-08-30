/** @author: Davi Mendes Pimentel */
package com.tbio.rpgcommunity.classes_model_do_sistema

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.IllegalArgumentException

data class Nome (val nome: String,  // Nome
                 val sobreNome: List<String>? = null)
                : RpgItem(), Parcelable{
    override fun toHashMap(): HashMap<String, Any?> {
        val nomeHashMap = hashMapOf<String, Any?>()

        with(nomeHashMap){
            put("nome", nome)
            put("sobrenome", sobreNome)
        }

        return nomeHashMap
    }

    override fun toObject(doc: DocumentSnapshot) {  }

    // sobrenome completo
    private lateinit var sobreNomeCompleto: String

    // verifica se o Nome tem sobrenome
    val hasSobreNome: Boolean
        get() = sobreNome?.isEmpty() ?: false

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createStringArrayList()) {
        sobreNomeCompleto = parcel.readString()
    }

    init {
        if(sobreNome?.isNotEmpty() ?: false) {  // verifica se existe sobrenome
            // caso sim percorre todo o sobre nome e o armazena na propriedade sobreNomeCompleto
            sobreNome?.forEach {
                if (it.isEmpty())
                    throw IllegalArgumentException(
                        "String vazia passada como parâmetro para" +
                                "o ArrayList de Strings"
                    )

                sobreNomeCompleto += " $it"
            }
        }
        else sobreNomeCompleto = ""
    }

    // retorna o nome + sobrenomecompleto
    override fun toString(): String {
        return String.format("${this.nome} ${this.sobreNomeCompleto}")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeStringList(sobreNome)
        parcel.writeString(sobreNomeCompleto)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Nome> {
        override fun createFromParcel(parcel: Parcel): Nome {
            return Nome(parcel)
        }

        override fun newArray(size: Int): Array<Nome?> {
            return arrayOfNulls(size)
        }
    }
}