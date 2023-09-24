package com.example.shoppinglistnew.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistnew.databinding.DialogFragmentRemoveShopListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RemoveShopListDialog(
    private val onRemoveList: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentRemoveShopListBinding.inflate(layoutInflater)
        with(binding) {
            removeShopListButton.setOnClickListener {
                onRemoveList()
                dismiss()
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
        const val REMOVE_SHOP_LIST_DIALOG_TAG = "REMOVE_SHOP_LIST_DIALOG"
    }
}