package com.tbio.rpgcommunity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.cadastro.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //*********************************************************************************************************************//
        //EXPLICAÇÃO
        //Se colocar background numa fragment, o conteúdo main ficará invisível mas você ainda pode clicar e vai funcionar.
        //Só não vai funcionar se você inserir algo em cima, como um quadrado ou algo do tipo por exemplo.
        //*********************************************************************************************************************//

        //apague daqui
        val btnhue: Button = findViewById(R.id.btnteste3)
        btnhue.setOnClickListener {
            toast("clicou!")
        }//até aqui

        //floating action menu, necessario mudar a funcao
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //floating action button de criar personagem
        floatingPersonagem.setOnClickListener{
            startActivity<CriarPersonagem>()
        }

        //floating action button de criar sessão
        floatingSessao.setOnClickListener{
            startActivity<CriarSessao>()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        //teste tentando fazer os action buttons sumirem em determinados momentos:
        //menu!!.findItem(R.id.action_hue).isVisible = false
        return true
    }

    //Aqui é a opção de Settings (Configurações). Deixei como comentário aqui e no XML
    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
*/
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_perfil -> {
                // Handle the camera action
                loadMeuPerfil(frag = PerfilFragment())

            }
            R.id.nav_amigos -> {
                loadAmigos(frag = AmigosFragment())
            }
            R.id.nav_sessoes -> {
                loadSessoes(frag = SessoesFragment())

            }

            //menu nav_rascunho, que marquei como comentário em xml
            /*
            R.id.nav_rascunho -> {

            }*/

            R.id.nav_configuracoes -> {
                startActivity<Configuracoes>()
            }
            R.id.nav_logout -> {
                finish()
                //O código acima deve ser removido
                // Acho que esse código serve quando aplicar firebase:
                //Firebase.Auth.getInstance().signOut()
                toast("Logout realizado com sucesso.")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //função que carrega a Fragment Meu Perfil
    private fun loadMeuPerfil(frag: PerfilFragment){
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.frameLayout, frag)
        fm.commit()
    }

    //função que carrega a Fragment Amigos
    private fun loadAmigos(frag: AmigosFragment){
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.frameLayout, frag)
        fm.commit()
    }

    //função que carrega a Fragment Sessões
    private fun loadSessoes(frag: SessoesFragment){
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.frameLayout, frag)
        fm.commit()
    }

}
