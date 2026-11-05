package com.example.vision.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vision.R
import com.example.vision.data.Product
import com.example.vision.data.Store
import com.google.android.material.imageview.ShapeableImageView
import android.widget.ImageButton
import android.widget.TextView

class ProductAdapter(
    private var items: List<Product>,
    private val onClick: (Product) -> Unit,
    private val onFavouriteChanged: () -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)) {
        val image: ShapeableImageView = itemView.findViewById(R.id.productImage)
        val name: TextView = itemView.findViewById(R.id.productName)
        val category: TextView = itemView.findViewById(R.id.productCategory)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val favourite: ImageButton = itemView.findViewById(R.id.favouriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.name.text = p.title
        holder.category.text = p.category.replaceFirstChar { it.uppercase() }
        holder.price.text = "R %.2f".format(p.price * 18.0)
        if (p.thumbnail.isNotBlank()) holder.image.load(p.thumbnail) { crossfade(true); placeholder(R.drawable.product_placeholder); error(R.drawable.product_placeholder) }
        else holder.image.setImageResource(R.drawable.product_placeholder)
        holder.favourite.setImageResource(if (Store.favourites.contains(p.id)) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
        holder.favourite.setOnClickListener { Store.toggleFavourite(p.id); onFavouriteChanged(); notifyItemChanged(position) }
        holder.itemView.setOnClickListener { onClick(p) }
    }

    fun submit(newItems: List<Product>) { items = newItems; notifyDataSetChanged() }
}
