package com.tbio.rpgcommunity.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbio.rpgcommunity.R

class SessoesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return LayoutInflater.from(container?.context).inflate(R.layout.fragment_sessoes, container, false)
    }
}