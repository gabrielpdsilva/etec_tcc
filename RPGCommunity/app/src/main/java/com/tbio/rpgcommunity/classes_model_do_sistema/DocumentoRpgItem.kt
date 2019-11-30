package com.tbio.rpgcommunity.classes_model_do_sistema

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

abstract class DocumentoRpgItem(private val id: String?) : RpgItem(){
    var nearNumberOfResemble: Int = 0

    // retorna o id do objeto atual
    fun getId() = this.id

    // salva o documento-objeto atual no banco de dados
    fun saveDB(funcFailListener: ((e: Exception) -> Unit)? = null,
               funcSuccessListener: ((doc: DocumentReference) -> Unit)? = null) {
        this.referencia.parent.add(this.toHashMap())
            .addOnSuccessListener {
                funcSuccessListener?.invoke(it)
            }
            .addOnFailureListener {
                funcFailListener?.invoke(it)
            }
    }

    // le o documento-objeto atual do banco de dados
    fun readDB(funcSuccessListener: (doc: DocumentSnapshot) -> Unit, funcFailListener: (e: Exception) -> Unit) {
        this.referencia.get()
            .addOnSuccessListener {
                funcSuccessListener(it)
            }.addOnFailureListener {
                funcFailListener(it)
            }
    }

    // deleta o documento-objeto atual do banco de dados
    fun deleteDB(funcSuccessListener: () -> Unit, funcFailListener: (e: Exception) -> Unit) {
        this.referencia.delete()
            .addOnSuccessListener {
                funcSuccessListener()
            }.addOnFailureListener {
                funcFailListener(it)
            }
    }

    // atualiza o documento-objeto atual do banco de dados
    fun updateDB(funcSuccessListener: () -> Unit, funcFailListener: (e: Exception) -> Unit) {
        this.getId()?.let {
            this.referencia.set(this.toHashMap())
                .addOnSuccessListener {
                    funcSuccessListener()
                }.addOnFailureListener {
                    funcFailListener(it)
                }
        }
    }

    abstract val caminho: String
    abstract val referencia: DocumentReference
}