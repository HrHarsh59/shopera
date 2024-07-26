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
import project.demo.shopera.R
import project.demo.shopera.SpecificCategoryFragmentDirections
import project.demo.shopera.model.ProductData

class CategoryProductAdapter(
    options: FirestoreRecyclerOptions<ProductData>
) : FirestoreRecyclerAdapter<ProductData, CategoryProductAdapter.ProductDataViewHolder>(options) {

    class ProductDataViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val productImageView: ImageView = view.findViewById(R.id.product_image_view)
        val productName: TextView = view.findViewById(R.id.product_name_text)
        val productPrice: TextView = view.findViewById(R.id.product_price_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDataViewHolder {
        return ProductDataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ProductDataViewHolder,
        position: Int,
        model: ProductData
    ) {
        Glide.with(holder.productImageView.context).load(model.productImageUrl).centerCrop()
            .into(holder.productImageView)
        holder.productName.text = model.productName
        holder.productPrice.text = model.productPrice
        holder.productImageView.setOnClickListener {
            val navigation = SpecificCategoryFragmentDirections.actionSpecificCategoryFragmentToProductFragment(
                productCode = model.productCode.toString(),
                productName = model.productName.toString()
            )
            holder.view.findNavController().navigate(navigation)
        }
    }


}