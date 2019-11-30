package com.tbio.rpgcommunity.classes_model_do_sistema

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Solicitacao(
        // dados do banco
        id: String?,
        parentId: String,

        // dados da solicitacao
        val fromPath: String) : SubDocumentoRpgItem(id, parentId) {
    override var parentReference: DocumentReference? = null
        get() =
            FirebaseFirestore.getInstance().
                    document(
                            "Usuarios/${getParentId()}"
                    )

    override val caminho: String
        get() = this.referencia.path

    override val referencia: DocumentReference
        get() = FirebaseFirestore.getInstance()
                .document("/Usuarios/${getParentId()}/Solicitacoes de Amizade/${getId()}")

    val from: DocumentReference
        get() = FirebaseFirestore.getInstance().document(this.fromPath)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()!!,
            parcel.readString()!!) {  }

    override fun toHashMap(): HashMap<String, Any?> {
        val hashMapToSolicitation = hashMapOf<String, Any?>()

        with(hashMapToSolicitation) {
            put("date", Timestamp.now())
            put("userDocFromSolicitation", this@Solicitacao.from)
        }

        return hashMapToSolicitation
    }

    override fun toObject(doc: DocumentSnapshot) {

    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(this.getId());
        dest.writeString(this.getParentId());
        dest.writeString(this.fromPath);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Solicitacao> {
        override fun createFromParcel(parcel: Parcel): Solicitacao {
            return Solicitacao(parcel)
        }

        override fun newArray(size: Int): Array<Solicitacao?> {
            return arrayOfNulls(size)
        }

        fun toNewObject(doc: DocumentSnapshot) : Solicitacao =
                Solicitacao(doc.id, doc.reference.parent.parent!!.id, (doc["userDocFromSolicitation"] as DocumentReference).path)
    }
}