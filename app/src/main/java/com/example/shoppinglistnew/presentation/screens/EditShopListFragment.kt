package com.example.shoppinglistnew.presentation.screens

import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.DialogFragmentEditShopListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditShopListFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentEditShopListBinding.inflate(layoutInflater)
        with(binding) {
            shopListIdTextInputLayout.editText?.doAfterTextChanged {
                shopListIdTextInputLayout.error = null
            }
            editShopListButton.setOnClickListener {
                val editText = shopListIdTextInputLayout.editText
                if (editText != null && editText.text.isNotEmpty()) {
                    navigateToShopList(
                        editText.text.toString().toInt(),
                        getString(R.string.your_friend_shop_list)
                    )
                    dismiss()
                } else {
                    shopListIdTextInputLayout.error = getString(R.string.input_error_enter_list_id)
                }
            }
        }
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
        return builder.create()
    }

    private fun navigateToShopList(listId: Int, listName: String) {
        val action = EditShopListFragmentDirections.actionEditShopListFragmentToShopListFragment(
            listId,
            listName
        )
        findNavController().navigate(action)
    }
}