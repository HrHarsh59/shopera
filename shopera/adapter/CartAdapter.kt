package project.demo.shopera.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.card.MaterialCardView
import project.demo.shopera.R
import project.demo.shopera.menubar.CartFragmentDirections
import project.demo.shopera.model.CartData

class   CartAdapter(option: FirestoreRecyclerOptions<CartData>)
    : FirestoreRecyclerAdapter<CartData, CartAdapter.CartDataViewHolder>(option) {

    class CartDataViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val cartCard: MaterialCardView = view.findViewById(R.id.cart_card)
        val productImage: ImageView = view.findViewById(R.id.cart_product_image)
        val productName: TextView = view.findViewById(R.id.cart_product_name)
        val productPrice: TextView = view.findViewById(R.id.cart_product_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartDataViewHolder {
        return CartDataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: CartDataViewHolder,
        position: Int,
        model: CartData
    ) {
        Glide.with(holder.productImage.context).load(model.productImageUrl).centerCrop().into(holder.productImage)
        holder.productName.text = model.productName
        holder.productPrice.text = model.productPrice
        holder.cartCard.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToProductFragment(
                productCode = model.productCode.toString(),
                productName = model.productName.toString()
            )

            holder.view.findNavController().navigate(action)
        }
    }
}