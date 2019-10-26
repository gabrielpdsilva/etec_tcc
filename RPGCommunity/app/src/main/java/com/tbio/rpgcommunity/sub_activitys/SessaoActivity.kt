package com.tbio.rpgcommunity.sub_activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.tbio.rpgcommunity.R
import org.jetbrains.anko.find

class SessaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessao)

        val switch = findViewById<Switch>(R.id.activity_sessao_switch)
        switch.setOnClickListener {
            val intent: Intent = Intent(this.applicationContext, ChatSessaoActivity::class.java)
            startActivity(intent)
        }
    }
}
