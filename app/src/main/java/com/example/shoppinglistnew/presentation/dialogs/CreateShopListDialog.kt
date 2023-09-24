package com.example.shoppinglistnew.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.DialogFragmentCreateShopListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreateShopListDialog(
    private val onCreateList: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentCreateShopListBinding.inflate(layoutInflater)
        with(binding) {
            shopListNameTextInputLayout.editText?.doAfterTextChanged {
                shopListNameTextInputLayout.error = null
            }
            createShopListButton.setOnClickListener {
                val editText = shopListNameTextInputLayout.editText
                if (editText != null && editText.text.isNotEmpty()) {
                    onCreateList(editText.text.toString())
                    dismiss()
                } else {
                    shopListNameTextInputLayout.error = getString(R.string.input_error_enter_name)
                }
            }
            cancelShopListButton.setOnClickListener {
                dismiss()
            }
        }
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
        return builder.create()
    }

    companion object {
        const val CREATE_SHOP_LIST_DIALOG_TAG = "CREATE_SHOP_LIST_DIALOG"
    }
}