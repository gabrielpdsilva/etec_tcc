package com.tbio.rpgcommunity.classes_model_do_sistema

import com.google.firebase.firestore.DocumentSnapshot


// classe base (classe TAG) para qualquer objeto do aplicativo que deverá ser salvo no banco de dados
// tais como Personagem, Descrição, História, etc.
abstract class RpgItem {
    abstract fun toHashMap(): HashMap<String, Any?>
    abstract fun toObject(doc: DocumentSnapshot): Unit
}