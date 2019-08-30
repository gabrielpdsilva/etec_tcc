package com.tbio.rpgcommunity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tbio.rpgcommunity.criar.CriarPersonagem
import com.tbio.rpgcommunity.criar.CriarSessao
import com.tbio.rpgcommunity.fragments.AmigosFragment
import com.tbio.rpgcommunity.fragments.HomeFragment
import com.tbio.rpgcommunity.fragments.PerfilFragment
import com.tbio.rpgcommunity.fragments.SessoesFragment
import com.tbio.rpgcommunity.logincadastro.Login
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        loadHome(frag = HomeFragment())
        //*********************************************************************************************************************//
        //EXPLICAÇÃO
        //Se colocar background numa fragment, o conteúdo main ficará invisível mas você ainda pode clicar e vai funcionar.
        //Só não vai funcionar se você inserir algo em cima, como um quadrado ou algo do tipo por exemplo.
        //*********************************************************************************************************************//

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
        menu.findItem(R.id.action_editar_personagem).isVisible = false
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
                // home
                loadHome(frag = HomeFragment())
            }
            R.id.nav_perfil -> {
                // perfil
                loadMeuPerfil(frag = PerfilFragment())
            }
            R.id.nav_amigos -> {
                // amigos
                loadAmigos(frag = AmigosFragment())
            }
            R.id.nav_sessoes -> {
                // sessões
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
                    positiveButton(R.string.txtLogoutSim) { dialog ->
                        //Firebase.Auth.getInstance().signOut()
                        FirebaseAuth.getInstance().signOut()
                        val intentToLogin = Intent(applicationContext, Login::class.java)
                        intentToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(intentToLogin)
                        finish()
                    }
                    negativeButton(R.string.txtLogoutNao) { dialog ->
                        //dialog.dismiss() <-- não sei o que isso faz, tava no tutorial. Esse mesmo código tava no positiveButton também.
                        toast(R.string.txtLogoutFoiCancelado)
                    }
                    show()
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadHome(frag: HomeFragment) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.frameLayout, frag)
        fm.commit()
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
