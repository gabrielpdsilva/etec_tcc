package com.tbio.rpgcommunity.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Codigos
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import com.tbio.rpgcommunity.classes_model_do_sistema.Usuario
import com.tbio.rpgcommunity.classes_recycler_view.PersonagemAdapter
import kotlinx.android.synthetic.main.fragment_perfil.*
import org.jetbrains.anko.support.v4.alert
import java.util.*

class PerfilFragment : Fragment() {

    private lateinit var personagens: MutableList<Personagem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(container?.context).inflate(R.layout.fragment_perfil, container, false)

        view.findViewById<ProgressBar>(R.id.pbPersonagens).visibility = View.VISIBLE

        FirebaseFirestore
                .getInstance()
                .collection("Usuarios")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email.toString())
                .limit(1)
                .get()
                .addOnSuccessListener {
                    for(usuario in it){
                        val mUsuario = Usuario.toNewObject(usuario) as Usuario

                        val nicknamePerfil = activity!!.findViewById<TextView>(R.id.nicknamePerfil_perfil)
                        val fotoPerfil = activity!!.findViewById<ImageView>(R.id.avatarPerfil)
                        val emailPerfil = activity!!.findViewById<TextView>(R.id.lblEmailUsuario)

                        nicknamePerfil.text = mUsuario.nickname.nome
                        //idPerfil.text = mUsuario.getId()
                        emailPerfil.text = mUsuario.email

                        Picasso.get()
                                .load(mUsuario.foto)
                                .error(R.drawable.ic_add_avatar)
                                .resize(120, 120)
                                .into(fotoPerfil)

                        FirebaseFirestore.getInstance()
                                .collection("Usuarios/${mUsuario.getId()}/Personagens")
                                .get()
                                .addOnSuccessListener {
                                    personagens = mutableListOf<Personagem>()

                                    for(person in it){
                                        personagens.add(Personagem.toNewObject(person) as Personagem)
                                    }

                                    setPersonagemRecyclerView()
                                }
                    }
                }

        val imgPerfil = view.findViewById<ImageView>(R.id.avatarPerfil)

        imgPerfil.setOnClickListener {
            val intentToGalery = Intent(Intent.ACTION_PICK)
            intentToGalery.type = "image/*"

            startActivityForResult(intentToGalery, Codigos.CODIGO_PARA_REQUISITAR_IMAGEM)
        }



        return view
    }

    private fun setPersonagemRecyclerView() {
        this.pbPersonagens.visibility = View.GONE

        if(personagens.size > 0){
            txt_vazio_personagens_perfil.visibility = View.GONE

            // define o adaptador de RecyclerView de personagens
            rvPersonagens.adapter = PersonagemAdapter(activity!!.applicationContext,
                                                      personagens)

            // define o layout
            val mLayout = LinearLayoutManager(activity!!)
            mLayout.orientation = RecyclerView.VERTICAL
            rvPersonagens.layoutManager = mLayout
        }
        else{
            txt_vazio_personagens_perfil.visibility = View.VISIBLE
        }
    }

    //Daqui pra baixo é tudo teste tentando fazer aparecer/desaparecer os action buttons. Fiz o mesmo na mainactivity
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateOptionsMenu (menu: Menu?, inflater: MenuInflater?){
        inflater!!.inflate(R.menu.main, menu)
        menu!!.findItem(R.id.action_hue).isVisible = true
        menu!!.findItem(R.id.action_search).isVisible = false

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean{
        val id = item!!.itemId

        if(id == R.id.action_hue){
            toast("hue")
        }

        return super.onOptionsItemSelected(item)
    }
*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            // verifica se o código de requisição foi para pegar imagens da galeria
            Codigos.CODIGO_PARA_REQUISITAR_IMAGEM -> {

                // verifica se tudo correu bem
                if(resultCode == Activity.RESULT_OK){

                    // caso sim, recupera a imagem e preenche-a na tela
                    alert ("Você tem certeza que deseja alterar sua foto de perfil?"){
                        title = "Alteração de Imagem"
                        positiveButton("sim"){
                            // ID da imagem
                            val fileName = UUID.randomUUID()

                            // faz a inserção da imagem no storage
                            FirebaseStorage.getInstance()
                                    .getReference("/imagens/$fileName")
                                    .putFile(data!!.data!!)
                                    .addOnSuccessListener {
                                        it.metadata!!.reference!!.downloadUrl
                                                .addOnSuccessListener {
                                                    val mDownLoadUri = it

                                                    FirebaseFirestore.getInstance()
                                                            .collection("Usuarios")
                                                            .whereEqualTo("email",
                                                                    FirebaseAuth.getInstance()
                                                                            .currentUser
                                                                            !!.email.toString())
                                                            .limit(1)
                                                            .get()
                                                            .addOnSuccessListener {
                                                                for(usuario in it){
                                                                    FirebaseFirestore.getInstance()
                                                                            .document("Usuarios/${usuario.id}")
                                                                            .set(hashMapOf("foto" to mDownLoadUri.toString()),
                                                                                    SetOptions.merge())
                                                                }
                                                            }
                                                }
                                    }
                            val mBitMap = MediaStore.Images.Media.getBitmap(
                                    this@PerfilFragment.activity!!.contentResolver,
                                    data.data!!)
                            avatarPerfil.setImageDrawable(BitmapDrawable(mBitMap))
                        }
                        negativeButton("não tenho certeza"){
                            // não faz nada
                        }

                        show()
                    }
                }
                else{
                    Log.d("DebugPerfil", "erro ao buscar a imagem: ${data}")
                }
            }
        }
    }
}