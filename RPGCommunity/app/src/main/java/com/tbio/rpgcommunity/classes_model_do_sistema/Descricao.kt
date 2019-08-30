/** @author: Davi Mendes Pimentel */
package com.tbio.rpgcommunity.classes_model_do_sistema

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.IllegalArgumentException

// classe destinada a representar a descrição tanto de personagens, como de sessões

/** @see Descricao recebe como argumentos os seguintes dados:
 *
 *  @property descricaoBasica representa a descrição de fato;
 *  @property campos representa os campos que a descrição pode conter, como exemplo: Classe, Raça, Etnia, etc.*/
class Descricao(private var descricaoBasica: String,
                private var campos: MutableMap<String, Any>? = null)
                : RpgItem(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            mutableMapOf<String, Any>()) {
        this.campos.apply {
            parcel.readMap(this, MutableMap::class.java.classLoader)
        }
    }

    /** @see addCampo que adiciona um novo campo à descrição com base no
     *  @param newCampo que representa o novo campo à adicionar, como exemplo: Classe, Raça, Magias, etc.*/
    fun addCampo(newCampo: String, valor: Any) = this.campos?.put(newCampo, valor)

    /** @see getCampo que retorna o elemento especificado pelo index, com base no
     *  @param index que representa o index do elemento na lista mutável de campos
     *  @return campos[index] */
    fun getCampo(index: String): Any? = this.campos?.get(index)

    fun getCampos() = this.campos

    /** @see deleteCampo que deleta um campo da descrição com base no
     *  @param index que representa o index do elemento na lista mutável de campos
     *  @return [true] se a operação correr bem e [false] caso o index não seja nem uma String e nem um Int */
    fun deleteCampo(index: String){
        this.campos?.remove(index)
    }

    fun setDescricao(novaDescricao: Descricao){

        if(novaDescricao.descricaoBasica.isEmpty())
            throw IllegalArgumentException("'nova descrição' vazia passada para 'setDescricao'")
        this.descricaoBasica = novaDescricao.descricaoBasica
        this.campos = novaDescricao.campos
    }

    fun setDescricao(novaDescricao: String){

        if(novaDescricao.isEmpty())
            throw IllegalArgumentException("'nova descrição' vazia passada para 'setDescricao'")
        this.descricaoBasica = novaDescricao
    }

    fun getDescricaoBasica() = this.descricaoBasica

    override fun toHashMap(): HashMap<String, Any?> {
        val descricaoHashMap: HashMap<String, Any?> = hashMapOf()

        descricaoHashMap.put("descricao", this.descricaoBasica)
        descricaoHashMap.put("campos", this.campos)

        return descricaoHashMap
    }

    override fun toObject(doc: DocumentSnapshot) {
        // recupera a descrição basica do item
        this.descricaoBasica = doc.get("descricao") as String

        // recupera os campos do item
        @Suppress("UNCHECKED_CAST")
        this.campos = (doc.get("campos") as Map<String, Any>).toMutableMap()
    }

    companion object CREATOR : Parcelable.Creator<Descricao>{
        override fun createFromParcel(parcel: Parcel): Descricao = Descricao(parcel)
        override fun newArray(size: Int): Array<Descricao?> = arrayOfNulls(size)

        fun toNewObject(map: Map<String, Any?>?): RpgItem?{
            @Suppress("UNCHECKED_CAST")

            map?.let {
                return Descricao(
                    map.get("descricao") as String,
                    (map.get("campos") as Map<String, Any>?)?.toMutableMap()
                )
            }
            return null
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(descricaoBasica)
        parcel.writeMap(campos)
    }

    override fun describeContents(): Int {
        return 0
    }
}