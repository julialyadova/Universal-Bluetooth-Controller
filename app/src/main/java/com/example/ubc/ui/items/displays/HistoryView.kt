package com.example.ubc.ui.items.displays

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemHistoryBinding
import com.example.ubc.ui.editor.ViewShadowBuilder
import com.example.ubc.ui.items.DisplayView
import com.example.ubc.ui.main.dialogs.ItemDialog
import java.util.*

class HistoryView @JvmOverloads constructor(
        item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DisplayView(item, context, attrs, defStyleAttr) {

    private val binding = ItemHistoryBinding.inflate(LayoutInflater.from(context),this,true)
    private val history = LinkedList<String>() //todo: item args!
    private val maxRecords = 8 //todo: item args!

    init{
        binding.historyCopyButton.setOnClickListener { copy() }
        binding.historyClearButton.setOnClickListener {
            history.clear()
            binding.historyDisplay.text = ""
        }
    }

    override fun getDragHandler() = binding.historyDisplay
    override fun getShadowBuilder() = ViewShadowBuilder(binding.root)
    override fun getCreateDialog() = ItemDialog()
    override fun getEditDialog() = ItemDialog()

    override fun receive(data: ByteArray) {
        history.add(data.toString())
        if (history.size > maxRecords) {
            history.remove()
        }
        binding.historyDisplay.text = history.joinToString("\r\n")
    }

    private fun copy() {
        val text = history.joinToString("\r\n")
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