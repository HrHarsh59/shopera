package project.demo.shopera.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import project.demo.shopera.R
import project.demo.shopera.menubar.OrderFragmentDirections
import project.demo.shopera.model.OrderData

class OrderAdapter(option: FirestoreRecyclerOptions<OrderData>)
    : FirestoreRecyclerAdapter<OrderData, OrderAdapter.OrderDataViewHolder>(option) {

    class OrderDataViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.order_product_image)
        val productName: TextView = view.findViewById(R.id.order_product_name)
        val productPrice: TextView = view.findViewById(R.id.order_product_price)
        val viewDetailButton: AppCompatButton = view.findViewById(R.id.view_detail_button)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDataViewHolder {
        return OrderDataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.order_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: OrderDataViewHolder,
        position: Int,
        model: OrderData
    ) {
        Glide.with(holder.productImage.context).load(model.productImageUrl).centerCrop().into(holder.productImage)
        holder.productName.text = model.productName
        holder.productPrice.text = model.productPrice
        holder.viewDetailButton.setOnClickListener {
            val action = OrderFragmentDirections.actionOrderFragmentToOrderDetailFragment2(
                orderId = model.orderId
            )
            holder.view.findNavController().navigate(action)
        }
    }
}