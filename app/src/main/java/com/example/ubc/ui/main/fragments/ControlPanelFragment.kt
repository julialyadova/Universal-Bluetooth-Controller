package com.example.ubc.ui.main.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.data.entities.Control
import com.example.ubc.databinding.FragmentControlPanelBinding
import com.example.ubc.databinding.ItemControlBinding
import com.example.ubc.ui.main.dialogs.ControlDialog
import com.example.ubc.ui.main.dialogs.RenamePanelDialog
import com.example.ubc.ui.main.viewmodels.ConnectionViewModel
import com.example.ubc.ui.main.viewmodels.ControlPanelViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ControlPanelFragment : Fragment() {

    private lateinit var _binding : FragmentControlPanelBinding
    private val _viewModel: ControlPanelViewModel by activityViewModels()
    private val _connectionViewModel: ConnectionViewModel by activityViewModels()



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlPanelBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_controlPanelFragment_to_menuFragment)
        }

        _binding.optionsButton.setOnClickListener {
            findNavController().navigate(R.id.action_controlPanelFragment_to_settingsFragment)
        }

        _binding.title.setOnLongClickListener {
            RenamePanelDialog().show(parentFragmentManager, "dialog")
            true
        }

        _binding.addControlButton.setOnClickListener {
            ControlDialog(null).show(parentFragmentManager, "dialog")
        }

        _binding.log.setOnLongClickListener {
            showMenu(it, R.menu.menu_logs)
            true
        }

        _viewModel.panelData.observe(viewLifecycleOwner) { controlPanel ->
            _binding.title.text = controlPanel.name
        }

        _viewModel.controlsData.observe(viewLifecycleOwner) { controls ->
            addControls(LayoutInflater.from(context), controls)
        }

        _connectionViewModel.log.observe(viewLifecycleOwner) { log ->
            addLog(log)
            _binding.logsScroll.fullScroll(ScrollView.FOCUS_DOWN)
        }

        _connectionViewModel.connected.observe(viewLifecycleOwner) { connected ->
            if (connected) {
                _binding.imageView.setImageResource(R.drawable.ic_bluetooth_on)
            } else {
                _binding.imageView.setImageResource(R.drawable.ic_baseline_bluetooth_24)
            }
        }

        _connectionViewModel.activeDevice.observe(viewLifecycleOwner) { device ->
            _binding.panelDevice.text = device?.name ?: getString(R.string.not_connected)
        }

        _connectionViewModel.history.observe(viewLifecycleOwner) { history ->
            _binding.log.removeAllViews()
            history.forEach(this::addLog)
            _binding.logsScroll.fullScroll(ScrollView.FOCUS_DOWN)
        }

        _connectionViewModel.loadLogs()
    }

    private fun addLog(log: String) {
        val textView = TextView(context)
        textView.text = log
        _binding.log.addView(textView)
    }

    private fun addControls(inflater: LayoutInflater, controls: List<Control>) {
        _binding.controls.removeAllViews()

        for (control in controls) {
            val controlBinding = ItemControlBinding.inflate(inflater)

            controlBinding.controlText.text = control.name
            controlBinding.controlData.text = control.data

            controlBinding.controlText.setOnClickListener {
                _connectionViewModel.send(control.data)
            }

            controlBinding.controlText.setOnLongClickListener {
                ControlDialog(control).show(parentFragmentManager, "dialog")
                true
            }

            _binding.controls.addView(controlBinding.root)
        }
    }

    private fun showMenu(parent: View, @MenuRes resourceId: Int) {
        PopupMenu(context, parent).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener() { item ->
                when (item.itemId) {
                    R.id.menu_logs_copy -> {
                        copyLogs()
                        true
                    }
                    R.id.menu_logs_share -> {
                        shareLogs()
                        true
                    }
                    R.id.menu_logs_clear -> {
                        _connectionViewModel.clearLogs()
                        true
                    }
                    else -> false
                }
            }
            inflate(resourceId)
            show()
        }
    }

    private fun shareLogs() {
        val intent= Intent()
        intent.action= Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TITLE, "Connection logs")
        intent.putExtra(Intent.EXTRA_TEXT, _connectionViewModel.getLogsAsText())
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    private fun copyLogs() {
        val textToCopy = _connectionViewModel.getLogsAsText()
        val clipboardManager = getSystemService(requireContext(), ClipboardManager::class.java)
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager?.setPrimaryClip(clipData)
    }
}