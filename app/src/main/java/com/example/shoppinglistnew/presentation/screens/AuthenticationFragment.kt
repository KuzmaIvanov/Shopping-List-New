package com.example.shoppinglistnew.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.FragmentAuthenticationBinding
import com.example.shoppinglistnew.presentation.viewmodels.AuthenticationViewModel

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = requireNotNull(_binding) { getString(R.string.binding_is_not_init) }
    private val viewModel by activityViewModels<AuthenticationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationUiState()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeAuthenticationUiState() {
        viewModel.authenticationUiState.observe(viewLifecycleOwner) { uiState ->
            binding.progressIndicator.visibility = GONE
            binding.authenticationMessageTextView.visibility = VISIBLE
            when (uiState) {
                true -> {
                    showAuthenticatedSuccessMessage()
                    navigateToMyShopLists()
                }

                false -> {
                    bindAuthenticatedErrorViews()
                }
            }
        }
    }

    private fun showAuthenticatedSuccessMessage() {
        binding.tryAgainButton.visibility = GONE
        Toast.makeText(
            requireContext(),
            R.string.authentication_success_message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun bindAuthenticatedErrorViews() {
        binding.authenticationMessageTextView.text =
            getString(R.string.authentication_error_message)
        binding.tryAgainButton.visibility = VISIBLE
    }

    private fun setListeners() {
        binding.tryAgainButton.setOnClickListener {
            viewModel.getAuthentication()
        }
    }

    private fun navigateToMyShopLists() {
        val action = AuthenticationFragmentDirections.actionAuthenticationFragmentToMyShopListsFragment()
        findNavController().navigate(action)
    }
}