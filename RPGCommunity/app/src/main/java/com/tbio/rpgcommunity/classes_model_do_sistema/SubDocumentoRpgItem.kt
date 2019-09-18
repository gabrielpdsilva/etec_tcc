package com.tbio.rpgcommunity.classes_model_do_sistema

import com.google.firebase.firestore.DocumentReference

abstract class SubDocumentoRpgItem(id: String?, private val parentId: String) : DocumentoRpgItem(id){
    fun getParentId() = this.parentId
    abstract var parentReference: DocumentReference?
}