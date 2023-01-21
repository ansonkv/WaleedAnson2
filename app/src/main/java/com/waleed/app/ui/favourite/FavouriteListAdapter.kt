package com.waleed.app.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.FavoriteListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.business.data.newdata.getProductName
import com.waleed.app.business.data.newdata.getProductPrice
import com.waleed.app.util.loadImg
import com.waleed.app.util.makeGone
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.product_single_item.view.*

class FavouriteListAdapter(
    private var listData: ArrayList<FavoriteListResponse.Favourite>
) : RecyclerView.Adapter<FavouriteListAdapter.ProductListVH>() {
    var onItemClick: ((FavoriteListResponse.Favourite) -> Unit)? = null
    var onFavItemClick: ((productData: FavoriteListResponse.Favourite, position: Int) -> Unit)? = null
    var onCartClick: ((productData: FavoriteListResponse.Favourite) -> Unit)? = null
    inner class ProductListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FavoriteListResponse.Favourite) {
            itemView.img_product.loadImg(data.thumbImage)
            itemView.tv_product_name.setLanguageString(
                data.productName,data.productNameAr
            )
            itemView.tv_product_price.setLanguageString(
                data.unitPrice,data.unitPriceAr
            )

            itemView.img_fav_btn.makeGone()

            itemView.img_product.setOnClickListener {
                onItemClick?.invoke(data)
            }
            itemView.img_fav_btn.setOnClickListener {
                onFavItemClick?.invoke(data, adapterPosition)
            }
            itemView.img_cart_btn.setOnClickListener {
                onCartClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_single_item, parent, false)
        return ProductListVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ProductListVH, position: Int) {
        holder.bind(listData[position])
        holder.itemView.setOnClickListener {

        }
    }


}