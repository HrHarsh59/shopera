package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_shop_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import project.demo.shopera.adapter.ShopProductAdapter
import project.demo.shopera.databinding.FragmentShopProfileBinding
import project.demo.shopera.model.ProductData
import project.demo.shopera.model.Shop
import project.demo.shopera.model.ShopData


class ShopProfileFragment : Fragment() {

    private var _binding: FragmentShopProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var shopImageUrl: String
    private lateinit var shopName: String
    private lateinit var shopId: String

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth

    private lateinit var adapter: ShopProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopImageUrl = it.getString("shopImageUrl").toString()
            shopName = it.getString("shopName").toString()
            shopId = it.getString("shopId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var isShow = true
        var scrollRange = -1

        appbar_layout_shop.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar_shop.title = shopName
                isShow = true
            }
            else if (isShow) {
                collapsingToolbar_shop.title = " "
                isShow = false
            }
        })

        toolbar_shop.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        productCount()
        subscribeCount()
        Glide.with(this).load(shopImageUrl).centerCrop().into(binding.shopProfileImage)
        binding.shopProfileNameText.text = shopName

        val productCollections = db.collection("products")
        val query = productCollections.orderBy("productName", Query.Direction.ASCENDING)
            .whereEqualTo("shopId", shopId)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ProductData>().setQuery(query, ProductData::class.java).build()
        adapter = ShopProductAdapter(recyclerViewOption)

        recyclerView_shop_profile.adapter = adapter
        recyclerView_shop_profile.layoutManager = LinearLayoutManager(requireContext())

        updateSubscribeButton()

        binding.subscribe.setOnClickListener {
            subscribe()
        }

        binding.unSubscribe.setOnClickListener {
            unSubscribe()
        }


    }

    private fun productCount() {
        var count = 0
        db.collection("shop").document(shopId).collection("product").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                count = task.result!!.size()
                binding.shopProfileProductCountText.text = count.toString()
            }
        }
    }

    private fun subscribeCount() {
        GlobalScope.launch {
            val subscribe = getshop(shopId).await().toObject(ShopData::class.java)
            val allSubscriber = subscribe!!.subscriber.size.toString()

            withContext(Dispatchers.Main) {
                binding.shopProfileCustomerCountText.text = allSubscriber
            }
        }
    }

    fun getshop(shopId: String): Task<DocumentSnapshot> {
        return db.collection("shop").document(shopId).get()
    }

    private fun subscribe() {
        GlobalScope.launch {
            val currentUser = auth.currentUser!!.uid
            val subscribe = getshop(shopId).await().toObject(ShopData::class.java)
            val isSubscribe = subscribe!!.subscriber.contains(currentUser)

            if (!isSubscribe) {
                subscribe.subscriber.add(currentUser)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Shop Added", Toast.LENGTH_SHORT).show()
                    binding.subscribe.visibility = View.INVISIBLE
                    binding.unSubscribe.visibility = View.VISIBLE
                }
            }
            db.collection("shop").document(shopId).set(subscribe)
            addShop(shopId)
        }
    }

    private fun unSubscribe() {
        GlobalScope.launch {
            val currentUser = auth.currentUser!!.uid
            val subscribe = getshop(shopId).await().toObject(ShopData::class.java)
            val isSubscribe = subscribe!!.subscriber.contains(currentUser)

            if (isSubscribe) {
                subscribe.subscriber.remove(currentUser)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Shop Removed", Toast.LENGTH_SHORT).show()
                    binding.unSubscribe.visibility = View.INVISIBLE
                    binding.subscribe.visibility = View.VISIBLE
                }
            }
            db.collection("shop").document(shopId).set(subscribe)
            deleteShop(shopId)
        }
    }

    private fun updateSubscribeButton() {
        GlobalScope.launch {
            val currentUser = auth.currentUser!!.uid
            val subscribe = getshop(shopId).await().toObject(ShopData::class.java)
            val isSubscribe = subscribe!!.subscriber.contains(currentUser)
            if (isSubscribe) {
                withContext(Dispatchers.Main) {
                    binding.unSubscribe.visibility = View.VISIBLE
                    binding.subscribe.visibility = View.INVISIBLE
                }
            }
            else {
                withContext(Dispatchers.Main) {
                    binding.unSubscribe.visibility = View.INVISIBLE
                    binding.subscribe.visibility = View.VISIBLE
                }
            }
        }
    }

    private suspend fun addShop(shopId: String) {
        val userId = auth.currentUser!!.uid
        val shop = Shop(
            shopName,
            shopImageUrl,
            shopId
        )
        db.collection("users").document(userId).collection("addedShop").document(shopId)
            .set(shop)
    }

    private suspend fun deleteShop(shopId: String) {
        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).collection("addedShop").document(shopId)
            .delete()
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ShopProfileFragment()
    }
}