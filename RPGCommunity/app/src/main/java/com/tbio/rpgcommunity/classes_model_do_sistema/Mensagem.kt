package com.tbio.rpgcommunity.classes_model_do_sistema

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Mensagem(
               // dados do banco
               id: String?,
               parentId: String,
               override var parentReference: DocumentReference?,

               // dados da mensagem
               var de: DocumentReference?,
               var para: DocumentReference?,
               val mensagem: String) : SubDocumentoRpgItem(id, parentId) {
    override val caminho: String
        get() = this.referencia.path

    override val referencia: DocumentReference
    get() = FirebaseFirestore.getInstance().document("${this.parentReference!!.path}/Histórico de Mensagens/${this.getId()}")

    override fun toHashMap(): HashMap<String, Any?> {
        val mensagemHashMap = hashMapOf<String, Any?>()

        mensagemHashMap.put("de", this.de)
        mensagemHashMap.put("para", this.para)
        mensagemHashMap.put("mensagem", this.mensagem)

        return mensagemHashMap
    }

    override fun toObject(doc: DocumentSnapshot) {

    }

    constructor(parcel: Parcel)
            : this(parcel.readString(),
                   parcel.readString(),
                  null,
                  null,
                  null,
                   parcel.readString()) {
        val docPathParent: String? = parcel.readString()
        val docPathDe: String = parcel.readString()!!
        val docPathPara: String = parcel.readString()!!

        // recupera o documento pai
        if(docPathParent != null)
            FirebaseFirestore.getInstance().document(docPathParent)
                    .get()
                    .addOnSuccessListener {
                        this.parentReference = it.reference
                    }

        // recupera o documento 'de'
        FirebaseFirestore.getInstance().document(docPathDe)
                .get()
                .addOnSuccessListener {
                    this.de = it.reference
                }

        // recupera o documento 'para'
        FirebaseFirestore.getInstance().document(docPathPara)
                .get()
                .addOnSuccessListener {
                    this.para = it.reference
                }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.getId())
        parcel.writeString(this.getParentId())
        parcel.writeString(this.mensagem)
        parcel.writeString(this.parentReference?.path)
        parcel.writeString(this.de!!.path)
        parcel.writeString(this.para!!.path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mensagem> {
        override fun createFromParcel(parcel: Parcel): Mensagem {
            return Mensagem(parcel)
        }

        override fun newArray(size: Int): Array<Mensagem?> {
            return arrayOfNulls(size)
        }
    }
}