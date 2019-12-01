package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.CampoMap
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import com.tbio.rpgcommunity.classes_recycler_view.CampoDisplayAdapter
import de.hdodenhof.circleimageview.CircleImageView

class PersonagemActivity : AppCompatActivity() {
    private lateinit var personGender: TextView
    private lateinit var personImage: CircleImageView
    private lateinit var personName: TextView
    private lateinit var personHistory: EditText
    private lateinit var personDescription: EditText
    private lateinit var rvPersonagemField: RecyclerView

    private lateinit var personagem: Personagem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem)

        this.personImage = findViewById(R.id.activity_personagem_person_img)
        this.personName = findViewById(R.id.dinamic_personagem_edt_nickname)
        this.personGender = findViewById(R.id.dinamic_personagem_edt_sexo)
        this.rvPersonagemField = findViewById(R.id.rvPersonagemFields)
        this.personDescription = findViewById(R.id.personagem_edt_descricao)
        this.personHistory = findViewById(R.id.personagem_edt_historia)

        personagem = intent!!.getParcelableExtra<Personagem>("personagem")

        Picasso.get()
                .load(personagem.image)
                .resize(100, 100)
                .into(this.personImage)

        this.personName.text = personagem.nome.toString()
        this.personGender.text = personagem.genero ?: "Sem Gênero"
        this.personDescription.setText(personagem.descricao?.getDescricaoBasica() ?: "Sem descrição")
        this.personHistory.setText(personagem.historia?.getHistoria() ?: "Sem História")

        this.setRecyclerView()
    }

    fun setRecyclerView() {
        this.rvPersonagemField.adapter = CampoDisplayAdapter(this, personagem.campos ?: CampoMap())

        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = RecyclerView.VERTICAL
        this.rvPersonagemField.layoutManager = mLayoutManager
    }
}