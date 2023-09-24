package com.example.shoppinglistnew.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.FragmentMyShopListsBinding
import com.example.shoppinglistnew.presentation.adapters.ShopListActionListener
import com.example.shoppinglistnew.presentation.adapters.ShopListsItemAdapter
import com.example.shoppinglistnew.presentation.dialogs.CreateShopListDialog
import com.example.shoppinglistnew.presentation.dialogs.RemoveShopListDialog
import com.example.shoppinglistnew.presentation.viewmodels.MyShopListsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyShopListsFragment : Fragment(), MenuProvider {

    private var _binding: FragmentMyShopListsBinding? = null
    private val binding get() = requireNotNull(_binding) { getString(R.string.binding_is_not_init) }
    private val viewModel by viewModels<MyShopListsViewModel>()
    private val adapter by lazy {
        ShopListsItemAdapter(
            shopLists = emptyList(),
            shopListActionListener = object : ShopListActionListener {
                override fun onShopListDetails(listId: Int, listName: String) {
                    navigateToShopList(listId, listName)
                }

                override fun onShopListRemove(listId: Int) {
                    showRemoveShopListDialog(listId)
                }

                override fun onShopListShare(listId: Int) {
                    showShareShopListBottomSheet(listId)
                }
            }
        )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.edit_item) {
            navigateToEditShopList()
            return true
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyShopListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        initRecyclerView()
        initListeners()
        observeShopListsUi()
        observeCreatedListUiState()
        observeRemovedListUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.shopListsRecyclerView.adapter = adapter
    }

    private fun initListeners() {
        binding.createShopListButton.setOnClickListener {
            showCreateShopListDialog()
        }
    }

    private fun observeShopListsUi() {
        viewModel.shopListsUiState.observe(viewLifecycleOwner) { result ->
            binding.progressIndicator.visibility = GONE
            if (result.isSuccess) {
                val shopLists = result.getOrThrow()
                binding.shopListsRecyclerView.visibility = VISIBLE
                adapter.shopLists = shopLists
                adapter.notifyDataSetChanged()
            } else {
                showErrorToast(getString(R.string.shop_lists_error_message))
            }
        }
    }

    private fun observeCreatedListUiState() {
        viewModel.createdListUiState.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                viewModel.getAllMyShopLists()
            } else {
                val errorMessage = result.exceptionOrNull()
                showErrorToast(errorMessage?.message.toString())
            }
        }
    }

    private fun observeRemovedListUiState() {
        viewModel.removedListUiState.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                viewModel.getAllMyShopLists()
            } else {
                showErrorToast(getString(R.string.failed_to_remove))
            }
        }
    }

    private fun showErrorToast(errorMessage: String) {
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showCreateShopListDialog() {
        val dialogFragment = CreateShopListDialog {
            viewModel.createShoppingList(it)
        }
        dialogFragment.show(parentFragmentManager, CreateShopListDialog.CREATE_SHOP_LIST_DIALOG_TAG)
    }

    private fun showRemoveShopListDialog(listId: Int) {
        val dialogFragment = RemoveShopListDialog {
            viewModel.removeShoppingList(listId)
        }
        dialogFragment.show(parentFragmentManager, RemoveShopListDialog.REMOVE_SHOP_LIST_DIALOG_TAG)
    }

    private fun navigateToShopList(listId: Int, listName: String) {
        val action = MyShopListsFragmentDirections.actionMyShopListsFragmentToShopListFragment(listId, listName)
        findNavController().navigate(action)
    }

    private fun showShareShopListBottomSheet(listId: Int) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, listId.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun navigateToEditShopList() {
        val action = MyShopListsFragmentDirections.actionMyShopListsFragmentToEditShopListFragment()
        findNavController().navigate(action)
    }
}