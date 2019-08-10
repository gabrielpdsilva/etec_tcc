package com.tbio.rpgcommunity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class Personagem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        //faz a barra de pesquisa sumir
        menu!!.findItem(R.id.action_search).isVisible = false
        return true
    }
}
