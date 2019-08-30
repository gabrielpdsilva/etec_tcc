package com.tbio.rpgcommunity.classes_model_do_sistema

abstract class SubDocumentoRpgItem(id: String?, private val parentId: String) : DocumentoRpgItem(id){
    fun getParentId() = this.parentId
}