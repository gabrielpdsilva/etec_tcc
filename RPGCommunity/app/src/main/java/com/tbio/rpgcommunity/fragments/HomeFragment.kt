package com.tbio.rpgcommunity.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.*
import com.tbio.rpgcommunity.classes_recycler_view.SaidaPesquisaAdapter
import com.tbio.rpgcommunity.classes_recycler_view.SessaoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_sessoes.view.*
import org.jetbrains.anko.support.v4.toast
import kotlin.IllegalArgumentException
import kotlin.reflect.KClass

class HomeFragment : Fragment() {
    private val itens: MutableList<DocumentoRpgItem> = mutableListOf<DocumentoRpgItem>()
    private lateinit var realView: View
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        realView = inflater.inflate(R.layout.fragment_home, container, false)
        val itemToSearch: String? = arguments?.getString("search", null)

        if(itemToSearch == null || itemToSearch == "") {
            FirebaseFirestore.getInstance()
                    .collection("Pesquisas")
                    .orderBy("searchedTimes", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener {
                        for(doc in it) {
                            val docTypeClass = doc["docTypeClass"] as String
                            (doc["docReference"] as DocumentReference)
                                    .get()
                                    .addOnSuccessListener {
                                        val genericDocRpgItem: DocumentoRpgItem?

                                        when(docTypeClass) {
                                            "personagem" -> {
                                                genericDocRpgItem = Personagem.toNewObject(it) as DocumentoRpgItem
                                            }

                                            "sessao" -> {
                                                genericDocRpgItem = Sessao.toNewObject(it) as DocumentoRpgItem
                                            }

                                            "usuario" -> {
                                                genericDocRpgItem = Usuario.toNewObject(it) as DocumentoRpgItem
                                            }

                                            else -> throw IllegalArgumentException("""valor de 'docTypeClass' inválido: $docTypeClass""")
                                        }

                                        itens.add(genericDocRpgItem)
                                        realView.findViewById<RecyclerView>(R.id.rvSessoes)
                                                .adapter?.notifyDataSetChanged()
                                                ?: setHomeRecyclerView()
                                    }
                        }
                    }.addOnFailureListener {
                        Log.e("error_ffc", it.message.toString())
                    }
        } else {
            val itemToSearchSplited = itemToSearch.toLowerCase().split(' ')
            val itemToSearchKeys: MutableList<String> = mutableListOf()
            itemToSearchSplited.forEach {
                itemToSearchKeys.addAll(it.chunked(3))
            }

            itemToSearchKeys.forEach {
                Log.i("DebugSearch", it)
            }

            itemToSearchKeys.forEach {
                FirebaseFirestore.getInstance()
                        .collection("Pesquisas")
                        .whereArrayContains("searchKeyList", it)
                        .get()
                        .addOnSuccessListener {
                            for (doc in it) {
                                val docTypeClass = doc["docTypeClass"] as String

                                (doc["docReference"] as DocumentReference)
                                        .get()
                                        .addOnSuccessListener {
                                            val genericDocRpgItem: DocumentoRpgItem?

                                            when (docTypeClass) {
                                                "personagem" -> {
                                                    genericDocRpgItem = Personagem.toNewObject(it) as DocumentoRpgItem
                                                }

                                                "sessao" -> {
                                                    genericDocRpgItem = Sessao.toNewObject(it) as DocumentoRpgItem
                                                }

                                                "usuario" -> {
                                                    genericDocRpgItem = Usuario.toNewObject(it) as DocumentoRpgItem
                                                }

                                                else -> throw IllegalArgumentException("""valor de 'docTypeClass' inválido: $docTypeClass""")
                                            }

                                            var alreadyInList = false;
                                            itens.forEach {
                                                if(it.referencia == genericDocRpgItem.referencia) {
                                                    Log.i("DebugSearch", "it.referencia == genericDocRpgItem.referencia : " + (it.referencia == genericDocRpgItem.referencia))
                                                    alreadyInList = true
                                                    return@forEach
                                                }
                                            }

                                            if(! alreadyInList) {
                                                itens.add(genericDocRpgItem)
                                                itens.sortWith(compareByDescending (DocumentoRpgItem::nearNumberOfResemble))

                                                realView.findViewById<RecyclerView>(R.id.rvSessoes)
                                                        .adapter?.notifyDataSetChanged()
                                                        ?: setHomeRecyclerView()
                                            }
                                        }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("error_ffc", it.message.toString())
                        }
            }
        }

        // Inflate the layout for this fragment
        return realView
    }

    private fun setHomeRecyclerView() {
        // define a progressbar como invisível
        realView.findViewById<ProgressBar>(R.id.pbHome).visibility = View.GONE

        if(itens.size > 0) {
            // define a visibilidade de 'Não existem sessões disponíveis'
            realView.findViewById<TextView>(R.id.txtNaoExistemSessoes).visibility = View.GONE

            // define o adapter da RecyclerView
            realView.findViewById<RecyclerView>(R.id.rvSessoes)
                    .adapter = SaidaPesquisaAdapter(itens, realView.context)

            // define o layout da RecyclerView
            val linearLayout = LinearLayoutManager(realView.context)
            linearLayout.orientation = RecyclerView.VERTICAL
            realView.findViewById<RecyclerView>(R.id.rvSessoes).layoutManager = linearLayout
        } else {
            realView.findViewById<TextView>(R.id.txtNaoExistemSessoes).visibility = View.VISIBLE
        }
    }
}
