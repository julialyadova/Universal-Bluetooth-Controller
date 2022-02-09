package com.example.ubc.ui.main.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.FragmentControlPanelBinding
import com.example.ubc.ui.editable.Editor
import com.example.ubc.ui.items.DataSender
import com.example.ubc.ui.main.dialogs.ControlDialog
import com.example.ubc.ui.main.dialogs.RenamePanelDialog
import com.example.ubc.ui.main.viewmodels.ConnectionViewModel
import com.example.ubc.ui.main.viewmodels.ItemsViewModel
import com.example.ubc.ui.main.viewmodels.PanelViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ControlPanelFragment : Fragment(), DataSender {

    private lateinit var _binding : FragmentControlPanelBinding
    private val _panelViewModel: PanelViewModel by activityViewModels()
    private val _itemsViewModel: ItemsViewModel by activityViewModels()
    private val _connectionViewModel: ConnectionViewModel by activityViewModels()



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlPanelBinding.inflate(layoutInflater)
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
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

        _panelViewModel.panel.observe(viewLifecycleOwner) { panel ->
            _binding.title.text = panel.name
            _itemsViewModel.loadForPanel(panel.id)
        }

        _itemsViewModel.items.observe(viewLifecycleOwner) { items ->
            addControls(LayoutInflater.from(context), items)
        }

        var editor = Editor(parentFragmentManager, _binding.canvas, this)
        _itemsViewModel.items.observe(viewLifecycleOwner) { items ->
            editor.update(items)
        }
        _connectionViewModel.log.observe(viewLifecycleOwner) {log ->
            editor.notifyItems(log)
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

        //_itemsViewModel.loadForPanel(_panelViewModel.panel.value?.id ?: 0)
        _connectionViewModel.loadLogs()
    }

    override fun send(data: String) {
        _connectionViewModel.send(data)
    }

    private fun addControls(inflater: LayoutInflater, items: List<Item>) {
        /*
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
        */
    }

    private fun showMenu(parent: View, @MenuRes resourceId: Int) {
        PopupMenu(context, parent).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener() { item ->
                when (item.itemId) {
                    R.id.menu_logs_copy -> {
                        //copyLogs()
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
}