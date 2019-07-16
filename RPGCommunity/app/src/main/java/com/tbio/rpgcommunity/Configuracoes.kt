package com.tbio.rpgcommunity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.widget.CompoundButton
import android.widget.Switch
import kotlinx.android.synthetic.main.configuracoes.*

class Configuracoes : AppCompatActivity() {

    private lateinit var switchTema: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.darkTheme)
        else setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracoes)


        switchTema = findViewById<Switch>(R.id.switchTema)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchTema.isChecked = true
        }

        switchTema.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                restartApp()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                restartApp()
            }
        }
    }

    private fun restartApp() {
        val i: Intent = Intent(applicationContext, Configuracoes::class.java)
        startActivity(i)
        finish()

    }
}