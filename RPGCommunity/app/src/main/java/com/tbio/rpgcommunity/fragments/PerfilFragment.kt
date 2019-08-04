package com.tbio.rpgcommunity.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.tbio.rpgcommunity.R

class PerfilFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(container?.context).inflate(R.layout.fragment_perfil, container, false)
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
}