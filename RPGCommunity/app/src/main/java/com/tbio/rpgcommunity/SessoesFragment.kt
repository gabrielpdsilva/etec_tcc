package com.tbio.rpgcommunity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SessoesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return LayoutInflater.from(container?.context).inflate(R.layout.fragment_sessoes, container, false)
    }
}