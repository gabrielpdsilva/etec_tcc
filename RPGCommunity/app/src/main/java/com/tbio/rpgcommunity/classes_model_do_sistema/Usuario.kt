package com.tbio.rpgcommunity.classes_model_do_sistema

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.core.net.toUri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Document

class Usuario(id: String?,
              val nickname: Nome,
              val email: String,
              val foto: Uri? = null,
              val amigos: MutableList<DocumentReference>? = null)
    : DocumentoRpgItem(id), Parcelable{

    // retorna o objeto serializado em um HashMap
    override fun toHashMap(): HashMap<String, Any?> {
        val userHashMap: HashMap<String, Any?> = hashMapOf()

        with(userHashMap){
            put("nickname", nickname.toHashMap())
            put("email", email)
            put("foto", foto.toString())
            put("amigos", amigos)
        }

        return userHashMap
    }

    // especifica (em uma String) o caminho do item no banco de dados
    override val caminho: String
        get() = FirebaseFirestore
            .getInstance()
            .document("Usuarios/${this.getId()}").path

    // referencia no banco de dados para o personagem atual
    override val referencia: DocumentReference
        get() = FirebaseFirestore
            .getInstance()
            .document("Usuarios/${this.getId()}")

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable<Nome>(Nome::class.java.classLoader),
            parcel.readString(),
            parcel.readParcelable(Uri::class.java.classLoader),
            mutableListOf()) {

            this.amigos.apply {
                parcel.readList(this, DocumentReference::class.java.classLoader)
            }
    }

    override fun toObject(doc: DocumentSnapshot) {  }

    companion object CREATOR : Parcelable.Creator<Usuario>{
        override fun createFromParcel(parcel: Parcel): Usuario = Usuario(parcel)
        override fun newArray(size: Int): Array<Usuario?> = arrayOfNulls(size)

        // retorna um objeto usuário a partir dos dados no banco
        fun toNewObject(doc: DocumentSnapshot): RpgItem{
            val nome = Nome((doc["nickname"] as HashMap<String, Any?>).get("nome").toString(),
                            (doc["nickname"] as HashMap<String, Any?>).get("sobrenome") as List<String>?)

            @Suppress("UNCHECKED_CAST")
            return Usuario(id = doc.id,
                           nickname = nome,
                           email = doc["email"] as String,
                           foto = (doc["foto"] as String?)?.toUri(),
                           amigos = doc["amigos"] as MutableList<DocumentReference>?)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.getId())
        parcel.writeParcelable(this.nickname, flags)
        parcel.writeString(email)
        parcel.writeParcelable(foto, flags)
        parcel.writeList(this.amigos)
    }

    override fun describeContents(): Int {
        return 0
    }
}
