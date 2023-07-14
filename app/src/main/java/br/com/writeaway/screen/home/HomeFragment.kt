package br.com.writeaway.screen.home

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding = FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()

    override fun initViews() {

    }

    override fun initObservers() {

    }
}