package com.example.shoppinglistnew.presentation.adapters

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.ItemShopListBinding
import com.example.shoppinglistnew.domain.models.ShopList

interface ShopListActionListener {
    fun onShopListDetails(listId: Int, listName: String)
    fun onShopListRemove(listId: Int)
    fun onShopListShare(listId: Int)
}

class ShopListsItemAdapter(
    var shopLists: List<ShopList>,
    private val shopListActionListener: ShopListActionListener
) : RecyclerView.Adapter<ShopListsItemAdapter.ShopListItemViewHolder>(), View.OnClickListener {

    class ShopListItemViewHolder(
        val binding: ItemShopListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemShopListBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.moreImageView.setOnClickListener(this)
        return ShopListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListItemViewHolder, position: Int) {
        val shopList = shopLists[position]
        with(holder.binding) {
            holder.itemView.tag = shopList
            moreImageView.tag = shopList
            nameTextView.text = shopList.name
            dateTextView.text = shopList.date
        }
    }

    override fun getItemCount(): Int = shopLists.size

    override fun onClick(v: View) {
        when (v.id) {
            R.id.more_image_view -> {
                showPopupMenu(v)
            }

            else -> {
                val shopList = v.tag as ShopList
                shopListActionListener.onShopListDetails(shopList.id, shopList.name)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val shopList = view.tag as ShopList
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove_text))
        popupMenu.menu.add(0, ID_SHARE, Menu.NONE, context.getString(R.string.share_text))
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_REMOVE -> {
                    shopListActionListener.onShopListRemove(shopList.id)
                }

                ID_SHARE -> {
                    shopListActionListener.onShopListShare(shopList.id)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_REMOVE = 1
        private const val ID_SHARE = 2
    }
}