package br.com.writeaway.screen.settings

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.writeaway.R
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentSettingsBinding
import br.com.writeaway.util.getOrderTypeLabel
import br.com.writeaway.util.getOrderTypeValue
import br.com.writeaway.util.getTextSizeLabel
import br.com.writeaway.util.getTextSizeValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSettingsBinding =
        FragmentSettingsBinding::inflate
    override val viewModel: SettingsViewModel by viewModels()

    override fun initViews() {
        viewModel.fetchTextSize()
        viewModel.fetchOrderType()
        viewModel.fetchLayoutManager()

        configSettingsStyle()
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun initObservers() {
        viewModel.textSize.observe(viewLifecycleOwner) { textSize ->
            binding.includeSettingsStyle.tvTextSizeStatus.text =
                textSize.getTextSizeLabel(requireContext())
        }

        viewModel.orderType.observe(viewLifecycleOwner) { orderType ->
            binding.includeSettingsStyle.tvOrderStatus.text =
                orderType.getOrderTypeLabel(requireContext())
        }

        viewModel.layoutManager.observe(viewLifecycleOwner) { layoutManager ->
            binding.includeSettingsStyle.tvLayoutStatus.text = layoutManager
        }
    }

    private fun configSettingsStyle() {
        with(binding.includeSettingsStyle) {
            vTextSize.setOnClickListener { onTextSizeCLicked(vTextSize) }
            vOrder.setOnClickListener { onOrderTypeClicked(vOrder) }
            vLayout.setOnClickListener { onListViewClicked(vLayout) }
        }
    }

    private fun onTextSizeCLicked(vTextSize: View) {
        showPopupMenu(vTextSize) { popupMenu ->
            generateMenuItem(R.string.small, popupMenu)
            generateMenuItem(R.string.medium, popupMenu)
            generateMenuItem(R.string.large, popupMenu)
            generateMenuItem(R.string.extraLarge, popupMenu)

            popupMenu.setOnMenuItemClickListener {
                viewModel.setTextSize(
                    it.title.toString().getTextSizeValue(requireContext())
                )
                true
            }
        }
    }

    private fun onOrderTypeClicked(vOrderType: View) {
        showPopupMenu(vOrderType) { popupMenu ->
            generateMenuItem(R.string.createDate, popupMenu)
            generateMenuItem(R.string.modificationDate, popupMenu)

            popupMenu.setOnMenuItemClickListener {
                viewModel.setOrderType(it.title.toString().getOrderTypeValue(requireContext()))
                true
            }
        }
    }

    private fun onListViewClicked(vLayout: View) {
        showPopupMenu(vLayout) { popupMenu ->
            generateMenuItem(R.string.viewInList, popupMenu)
            generateMenuItem(R.string.viewInGrid, popupMenu)

            popupMenu.setOnMenuItemClickListener {
                viewModel.setLayoutManager(it.title.toString())
                true
            }
        }
    }

    private fun generateMenuItem(srcTitle: Int, menu: PopupMenu) =
        menu.menu.add(ContextCompat.getString(requireContext(), srcTitle))

}