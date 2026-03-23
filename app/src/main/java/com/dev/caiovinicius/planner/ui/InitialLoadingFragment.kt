package com.dev.caiovinicius.planner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.caiovinicius.planner.R
import com.dev.caiovinicius.planner.databinding.FragmentInitialLoadingBinding

class InitialLoadingFragment : Fragment() {

    private var _binding: FragmentInitialLoadingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitialLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Todo: lógica da tela de laoding inicial (ir para cadastro de usuário ou home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}