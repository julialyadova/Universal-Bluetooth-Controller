package com.example.ubc.ui.panel.items.ubcarduino

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
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
        binding.historyCopyButton.setOnClickListener { copy() }
        binding.historyClearButton.setOnClickListener {
            item.history.clear()
            binding.historyDisplay.text = ""
        }
    }

    override fun onDataReceived(data: ByteArray) {
        item.onDataReceived(data)
        binding.historyDisplay.text = item.history.joinToString("\r\n")
    }

    private fun copy() {
        val text = item.history.joinToString("\r\n")
        val clipboardManager =
            ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clipData = ClipData.newPlainText("history", text)
        clipboardManager?.setPrimaryClip(clipData)
    }

    private fun share() {
        val intent= Intent()
        intent.action= Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TITLE, "Connection logs")
        intent.type="text/plain"
        context.startActivity(Intent.createChooser(intent, "Share To:"))
    }
}