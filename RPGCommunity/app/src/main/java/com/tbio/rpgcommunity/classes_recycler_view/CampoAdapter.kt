package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.CampoMap
import kotlinx.android.synthetic.main.criar_personagem.view.*
import kotlinx.android.synthetic.main.list_item_campos.view.*

class CampoAdapter(val keys: MutableList<String>,
                   val ctx: Context,
                   val onClickFunc: ((position: Int) -> Unit)? = null,
                   val onKeyTextChange: ((newText: String, position: Int) -> Unit)? = null,
                   val onValueTextChange: ((newText: String, position: Int) -> Unit)? = null)
    : RecyclerView.Adapter<CampoAdapter.CampoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampoViewHolder =
            CampoViewHolder(LayoutInflater.from(ctx).inflate(R.layout.list_item_campos,
                            parent,
                    false))

    override fun getItemCount(): Int = keys.size

    override fun onBindViewHolder(holder: CampoViewHolder, position: Int) {

        holder.key.isFocusable = true

        /*holder.key.setOnFocusChangeListener { view, b ->
            view.isEnabled = false
        }*/
        val keyWatcher = object : TextWatcher {
            private val pos = position
            override fun afterTextChanged(p0: Editable?) {
                if(holder.key.text.toString().isBlank()) {
                    holder.key.error = "Preencha o campo solicitado"
                    holder.key.isFocusable = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                this@CampoAdapter.onKeyTextChange?.invoke(p0.toString(), pos)
            }
        }

        holder.key.addTextChangedListener(keyWatcher)

        val valueWatcher = object : TextWatcher {
            private val pos = position
            override fun afterTextChanged(p0: Editable?) {
                if(holder.value.text.toString().isBlank()) {
                    holder.value.error = "Preencha o campo solicitado"
                    holder.value.isFocusable = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                this@CampoAdapter.onValueTextChange?.invoke(p0.toString(), pos)
            }
        }

        holder.value.addTextChangedListener(valueWatcher)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CampoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val key = itemView.list_item_campos_key
        val value = itemView.list_item_campos_value
    }
}