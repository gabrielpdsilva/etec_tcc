package com.tbio.rpgcommunity.sub_activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.Personagem
import de.hdodenhof.circleimageview.CircleImageView

class PersonagemActivity : AppCompatActivity() {
    private lateinit var personGender: TextView
    private lateinit var personImage: CircleImageView
    private lateinit var personName: TextView
    private lateinit var personClass: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem)

        this.personImage = findViewById(R.id.activity_personagem_person_img)
        this.personName = findViewById(R.id.dinamic_personagem_edt_nickname)
        this.personClass = findViewById(R.id.dinamic_personagem_edt_classe)
        this.personGender = findViewById(R.id.dinamic_personagem_edt_sexo)

        val personagem = intent!!.getParcelableExtra<Personagem>("personagem")

        Picasso.get()
                .load(personagem.image)
                .resize(100, 100)
                .into(this.personImage)

        this.personName.text = personagem.nome.toString()

        // this.personClass.text = personagem.
    }
}