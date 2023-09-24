package com.example.shoppinglistnew.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.DialogFragmentAddShopListItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddShopListItemDialog(
    private val addShopListItem: (name: String, quantity: Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentAddShopListItemBinding.inflate(layoutInflater)
        with(binding) {
            shopListItemNameTextInputLayout.editText?.doAfterTextChanged {
                shopListItemNameTextInputLayout.error = null
            }
            shopListItemQuantityTextInputLayout.editText?.doAfterTextChanged {
                shopListItemQuantityTextInputLayout.error = null
            }
            addShopListItemButton.setOnClickListener {
                val name = shopListItemNameTextInputLayout.editText?.text.toString()
                val quantity = shopListItemQuantityTextInputLayout.editText?.text.toString()
                if (name.isEmpty()) {
                    shopListItemNameTextInputLayout.error =
                        getString(R.string.input_error_enter_name)
                }
                if (quantity.isEmpty()) {
                    shopListItemQuantityTextInputLayout.error =
                        getString(R.string.input_error_enter_quantity)
                }
                if (name.isNotEmpty() && quantity.isNotEmpty()) {
                    addShopListItem(name, quantity.toInt())
                    dismiss()
                }
            }
            cancelShopListItemButton.setOnClickListener {
                dismiss()
            }
        }
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
        return builder.create()
    }

    companion object {
        const val ADD_SHOP_LIST_ITEM_DIALOG_TAG = "ADD_SHOP_LIST_ITEM_DIALOG"
    }
}