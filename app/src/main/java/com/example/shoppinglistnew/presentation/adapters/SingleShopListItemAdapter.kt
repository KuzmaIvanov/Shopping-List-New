package com.example.shoppinglistnew.presentation.adapters

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.ItemShopListItemBinding
import com.example.shoppinglistnew.domain.models.ShopListItem

interface ShopListItemActionListener {
    fun onShopListItemRemove(itemId: Int)
    fun onShopListItemCrossOf(itemId: Int)
}

class SingleShopListItemAdapter(
    var items: List<ShopListItem>,
    private val actionListener: ShopListItemActionListener
) : RecyclerView.Adapter<SingleShopListItemAdapter.SingleShopListItemViewHolder>(),
    View.OnClickListener {

    class SingleShopListItemViewHolder(
        val binding: ItemShopListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleShopListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemShopListItemBinding.inflate(inflater, parent, false)
        binding.moreImageView.setOnClickListener(this)
        return SingleShopListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleShopListItemViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            moreImageView.tag = item
            itemTextView.text = getItemText(item.name, item.quantity, item.isCrossed)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onClick(v: View) {
        if (v.id == R.id.more_image_view) {
            showPopupMenu(v)
        }
    }

    private fun getItemText(name: String, quantity: Int, isCrossed: Boolean): SpannableString {
        val text = StringBuilder(name)
        with(text) {
            append(" (")
            append(quantity)
            append(")")
        }
        if (isCrossed) {
            val textCrossed = SpannableString(text)
            textCrossed.setSpan(StrikethroughSpan(), 0, text.length, 0)
            return textCrossed
        }
        return SpannableString(text)
    }

    private fun showPopupMenu(view: View) {
        val shopListItem = view.tag as ShopListItem
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove_text))
        popupMenu.menu.add(
            0,
            ID_CROSS_OFF,
            Menu.NONE,
            if (shopListItem.isCrossed) context.getString(R.string.undo_cross_text_off)
            else context.getString(R.string.cross_text_off)
        )
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_REMOVE -> {
                    actionListener.onShopListItemRemove(shopListItem.id)
                }

                ID_CROSS_OFF -> {
                    actionListener.onShopListItemCrossOf(shopListItem.id)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_REMOVE = 1
        private const val ID_CROSS_OFF = 2
    }
}