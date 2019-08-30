package com.tbio.rpgcommunity

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
                recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                recreate()
            }
        }
    }

    private fun restartApp() {
        val i: Intent = Intent(applicationContext, Configuracoes::class.java)
        startActivity(i)
        finish()

    }
}