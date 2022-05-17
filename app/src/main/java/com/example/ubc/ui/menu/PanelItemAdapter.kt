package com.example.ubc.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ubc.R
import com.example.ubc.data.entities.PanelEntity

class PanelItemAdapter(
    private val onItemClick: (PanelEntity) -> Unit,
    private val onItemLongClick: (PanelEntity) -> Unit
) : RecyclerView.Adapter<PanelItemAdapter.ProductViewHolder>() {

    private var _items = listOf<PanelEntity>()

    class ProductViewHolder(view: View, private val onClick: (PanelEntity) -> Unit, private val onLongClick: (PanelEntity) -> Unit)
        : RecyclerView.ViewHolder(view) {
        private var panel : PanelEntity? = null

        init {
            view.setOnClickListener {
                panel?.let(onClick)
            }
            view.setOnLongClickListener {
                panel?.let(onLongClick)
                true
            }

        }

        fun bind(panel: PanelEntity) {
            this.panel = panel
            itemView.findViewById<TextView>(R.id.item_panel_name).text = panel.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_panel, parent, false)

        return ProductViewHolder(view, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = _items[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    fun setItems(newItems : List<PanelEntity>) {
        _items = newItems
        notifyDataSetChanged()
    }
}