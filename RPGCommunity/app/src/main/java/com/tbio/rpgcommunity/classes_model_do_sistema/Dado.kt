package com.tbio.rpgcommunity.classes_model_do_sistema

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

fun IntRange.random() = Random().nextInt((this.endInclusive + 1) - this.start) + 1

object Dado {
    fun playDice(endNumber: Int): Int {
        val result: Int = (1..endNumber).random()

        return result
    }

    fun playDice100(): Int{
        var myD100result = playDice(100)

        myD100result = myD100result / 10 * 10

        return myD100result
    }

    fun saveDice(result: Int, referenceToSave: CollectionReference,
                 diceType: String,
                 successFunc: ((DocumentSnapshot) -> Unit)? = null) {
        FirebaseFirestore.getInstance().collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email!!)
                .limit(1)
                .get()
                .addOnSuccessListener {
                    for (user in it) {
                        val mensagemStr = "${user["nickname.nome"]} jogou o ${diceType}" +
                                          " e tirou ${result}";

                        Mensagem(id = null,
                                parentId = referenceToSave.parent!!.id,
                                parentReference = referenceToSave.parent!!,
                                de = user.reference,
                                para = referenceToSave.parent!!,
                                mensagem = mensagemStr,
                                comportamento = Codigos.SISTEMA)
                                .saveDB {
                                    it.get()
                                      .addOnSuccessListener {
                                          successFunc?.invoke(it)
                                      }
                                }
                    }
                }
    }
}