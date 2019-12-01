package com.tbio.rpgcommunity.classes_recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tbio.rpgcommunity.R
import com.tbio.rpgcommunity.classes_model_do_sistema.CampoMap
import kotlinx.android.synthetic.main.list_item_campos_display.view.*

class CampoDisplayAdapter (val ctx: Context,
                           val campos: CampoMap<String, Any?>)
    : RecyclerView.Adapter<CampoDisplayAdapter.CampoDisplayViewHolder>(){

    private var keys = campos.keys.toMutableList()
    private var values = campos.values.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampoDisplayViewHolder =
            CampoDisplayViewHolder(LayoutInflater.from(ctx).inflate(R.layout.list_item_campos_display, parent, false))

    override fun getItemCount(): Int = this.campos.size

    override fun onBindViewHolder(holder: CampoDisplayViewHolder, position: Int) {
        holder.key.text = this.keys[position]
        holder.value.text = this.values[position].toString()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CampoDisplayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val key = itemView.list_item_campos_display_key
        val value = itemView.list_item_campos_display_value
    }
}