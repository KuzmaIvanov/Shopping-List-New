package com.example.shoppinglistnew.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.FragmentShopListBinding
import com.example.shoppinglistnew.presentation.adapters.ShopListItemActionListener
import com.example.shoppinglistnew.presentation.adapters.SingleShopListItemAdapter
import com.example.shoppinglistnew.presentation.dialogs.AddShopListItemDialog
import com.example.shoppinglistnew.presentation.viewmodels.ShopListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopListFragment : Fragment() {

    private var _binding: FragmentShopListBinding? = null
    private val binding get() = requireNotNull(_binding) { getString(R.string.binding_is_not_init) }
    private val viewModel by viewModels<ShopListViewModel>()
    private val args by navArgs<ShopListFragmentArgs>()
    private val adapter by lazy {
        SingleShopListItemAdapter(
            items = emptyList(),
            actionListener = object : ShopListItemActionListener {
                override fun onShopListItemRemove(itemId: Int) {
                    viewModel.removeFromList(args.listId, itemId)
                }

                override fun onShopListItemCrossOf(itemId: Int) {
                    viewModel.crossOffShopListItem(itemId)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListeners()
        observeShopListUiState()
        viewModel.getShopList(args.listId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.singleShopListRecyclerView.adapter = adapter
    }

    private fun initListeners() {
        binding.addToShopListButton.setOnClickListener {
            showAddShopListItemDialog()
        }
    }

    private fun observeShopListUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shopListUiState.collect { result ->
                    binding.progressIndicator.visibility = GONE
                    result?.let {
                        if (result.isSuccess) {
                            val shopList = result.getOrThrow()
                            binding.singleShopListRecyclerView.visibility = VISIBLE
                            adapter.items = shopList
                            adapter.notifyDataSetChanged()
                        } else {
                            showErrorToast(getString(R.string.single_shop_list_error_message))
                        }
                    }
                }
            }
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showAddShopListItemDialog() {
        val dialogFragment = AddShopListItemDialog { name, quantity ->
            viewModel.addToShopList(args.listId, name, quantity)
        }
        dialogFragment.show(parentFragmentManager, AddShopListItemDialog.ADD_SHOP_LIST_ITEM_DIALOG_TAG)
    }
}