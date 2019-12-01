package com.tbio.rpgcommunity.classes_model_do_sistema

fun HashMap<String, Any?>.toCampoMap() : CampoMap<String, Any?> {
    val c = CampoMap<String, Any?>()

    c.putAll(this)

    return c
}

class CampoMap<K, T>() : LinkedHashMap<K, T>()
    where K : String {  }