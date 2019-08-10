package com.tbio.rpgcommunity

import android.app.Person
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.tbio.rpgcommunity.criar.CriarPersonagem
import com.tbio.rpgcommunity.criar.CriarSessao
import com.tbio.rpgcommunity.fragments.AmigosFragment
import com.tbio.rpgcommunity.fragments.PerfilFragment
import com.tbio.rpgcommunity.fragments.SessoesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.*

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
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //       .setAction("Action", null).show()
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

    //código original onBackPressed
    /*override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }*/

    //código atual onBackPressed
    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        toast(R.string.txtDuploClique)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        //faz o menu de editar personagem não aparecer. Só aparecerá na activity do personagem, o
        //código já tá lá
        menu!!.findItem(R.id.action_editar_personagem).isVisible = false
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
            R.id.nav_home -> {
                // Handle the camera action
                toast("Ao clicar aqui tem que sair da fragment")
                startActivity<Personagem>()
            }
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

                // Acho que esse código serve quando aplicar firebase:
                //Firebase.Auth.getInstance().signOut()

                alert {
                    titleResource = R.string.txtLogoutTitulo
                    messageResource = R.string.txtLogoutCerteza
                    positiveButton(R.string.txtLogoutSim, { dialog ->
                        //Firebase.Auth.getInstance().signOut()
                        finish()
                        toast(R.string.txtLogoutRealizado)
                    })
                    negativeButton(R.string.txtLogoutNao, { dialog ->
                        //dialog.dismiss() <-- não sei o que isso faz, tava no tutorial. Esse mesmo código tava no positiveButton também.
                        toast(R.string.txtLogoutFoiCancelado)
                    })
                    show()
                }
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
