package br.com.writeaway.screen.settings

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSettingsBinding =
        FragmentSettingsBinding::inflate
    override val viewModel: SettingsViewModel by viewModels()

    override fun initViews() {

    }

    override fun initObservers() {

    }
}