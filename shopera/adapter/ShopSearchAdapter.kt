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
import project.demo.shopera.SearchFragmentDirections
import project.demo.shopera.model.ShopData

class ShopSearchAdapter(options: FirestoreRecyclerOptions<ShopData>)
    : FirestoreRecyclerAdapter<ShopData, ShopSearchAdapter.ShopDataViewHolder>(options) {


    class ShopDataViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val shopImage: ImageView = view.findViewById(R.id.shop_image)
        val shopName: TextView = view.findViewById(R.id.shop_name)
        val shopLocation: TextView = view.findViewById(R.id.shop_location)
        val shopCard: RelativeLayout = view.findViewById(R.id.shop_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopDataViewHolder {
        return ShopDataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shop_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ShopDataViewHolder,
        position: Int,
        model: ShopData
    ) {
        Glide.with(holder.shopImage.context).load(model.shopImageUrl).centerCrop().into(holder.shopImage)
        holder.shopName.text = model.shopName
        holder.shopLocation.text = model.shopLocation
        holder.shopCard.setOnClickListener {
            val navigation = SearchFragmentDirections.actionSearchFragmentToShopProfileFragment(
                shopImageUrl = model.shopImageUrl.toString(),
                shopName = model.shopName.toString(),
                shopId = model.shopId.toString()
            )
            holder.view.findNavController().navigate(navigation)
        }
    }


}