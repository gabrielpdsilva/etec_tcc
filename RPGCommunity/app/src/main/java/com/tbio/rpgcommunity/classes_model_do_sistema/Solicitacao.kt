package com.tbio.rpgcommunity.classes_model_do_sistema

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class Solicitacao(
        // dados do banco
        id: String?,
        parentId: String) : SubDocumentoRpgItem(id, parentId) {
    override var parentReference: DocumentReference?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override val caminho: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val referencia: DocumentReference
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()!!) {  }

    override fun toHashMap(): HashMap<String, Any?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toObject(doc: DocumentSnapshot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<Solicitacao> {
        override fun createFromParcel(parcel: Parcel): Solicitacao {
            return Solicitacao(parcel)
        }

        override fun newArray(size: Int): Array<Solicitacao?> {
            return arrayOfNulls(size)
        }
    }
}