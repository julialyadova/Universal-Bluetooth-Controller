package com.example.ubc.ui.panel.items.ubcarduino

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.example.ubc.R
import com.example.ubc.databinding.ItemHistoryBinding
import com.example.ubc.items.smf.ItemHistory
import com.example.ubc.ui.panel.items.ItemView

class HistoryView @JvmOverloads constructor(
    private val item: ItemHistory,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemHistoryBinding.inflate(LayoutInflater.from(context),this,true)

    init{
        binding.itemHistoryOptionsBtn.setOnClickListener {
            showOptions()
        }
    }

    override fun onDataReceived(data: ByteArray) {
        item.onDataReceived(data)
        binding.itemHistoryText.text = item.history.joinToString("\r\n")
    }

    private fun copy() {
        val text = item.history.joinToString("\r\n")
        val clipboardManager =
            ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clipData = ClipData.newPlainText("history", text)
        clipboardManager?.setPrimaryClip(clipData)
    }

    private fun clear() {
        binding.itemHistoryText.clearComposingText()
        item.history.clear()
    }

    private fun showOptions() {
        val popup = PopupMenu(context, binding.itemHistoryOptionsBtn)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.item_history_options_menu, popup.menu)
        popup.setOnMenuItemClickListener{item -> onMenuOptionClick(item)}
        popup.show()
    }

    private fun onMenuOptionClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_history_options_copy -> {
                copy()
                true
            }
            R.id.item_history_options_clear -> {
                clear()
                true
            }
            else -> false
        }
    }
}