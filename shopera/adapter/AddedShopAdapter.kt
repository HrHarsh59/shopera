package project.demo.shopera.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import project.demo.shopera.R
import project.demo.shopera.menubar.AddedShopFragmentDirections
import project.demo.shopera.model.Shop

class AddedShopAdapter(option: FirestoreRecyclerOptions<Shop>)
    : FirestoreRecyclerAdapter<Shop, AddedShopAdapter.AddedShopDataViewHolder>(option) {

    class AddedShopDataViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val shopImage: ImageView = view.findViewById(R.id.added_shop_image)
        val shopName: TextView = view.findViewById(R.id.added_shop_name)
        val shopCard: RelativeLayout = view.findViewById(R.id.shop_added_card)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddedShopDataViewHolder {
        return AddedShopDataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.added_shop_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: AddedShopDataViewHolder,
        position: Int,
        model: Shop
    ) {
        Glide.with(holder.shopImage.context).load(model.shopImageUrl).centerCrop().into(holder.shopImage)
        holder.shopName.text = model.shopName
        holder.shopCard.setOnClickListener {
            val action = AddedShopFragmentDirections.actionAddedShopFragmentToShopProfileFragment(
                shopImageUrl = model.shopImageUrl,
                shopName = model.shopName,
                shopId = model.shopId
            )
            holder.view.findNavController().navigate(action)
        }
    }
}