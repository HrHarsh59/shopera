package project.demo.shopera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import project.demo.shopera.databinding.FragmentProductBinding
import project.demo.shopera.model.CartData
import project.demo.shopera.model.OrderData
import project.demo.shopera.model.ProductData


class ProductFragment : Fragment() {

    val TAG = "MainActivity"

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val orderCollection = db.collection("shop")
    private val shop = db.collection("shop")
    private val auth = FirebaseAuth.getInstance()

    private lateinit var productImageUrl: String
    private lateinit var productName: String
    private lateinit var productPrice: String
    private lateinit var shopName: String
    private lateinit var shopImageUrl: String
    private lateinit var productCode: String
    private lateinit var shopId: String
    private lateinit var token: String
    private lateinit var productDescription: String
    private var clicked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            productName = it.getString("productName").toString()
            productCode = it.getString("productCode").toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.topAppBarProduct.title = productName

        topAppBar_product.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        getProductsData()
        handleLikeIcon()


        // cart
        binding.cartButton.setOnClickListener {
            GlobalScope.launch {
                val userId = auth.currentUser!!.uid
                val productCode = productCode
                val cart = getCart(userId, productCode).get().await().toObject(OrderData::class.java)

                val isCartAdded = cart?.productCode.toString()
                Log.d("PRODUCTCODE", isCartAdded)
                if (isCartAdded == productCode) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    addCart()
                }
            }
        }


        // buy now
        binding.buyNowButton.setOnClickListener {
            val userId = auth.currentUser!!.uid
            val docRef = db.collection("users").document(userId).collection("userAddress").document("default")
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.data == null) {
                            Log.d("JUSTKNOW", "DATA IS : ${document.data}")
                            val action = ProductFragmentDirections.actionProductFragmentToOrderDetailFragment(
                                productCode = productCode
                            )
                            findNavController().navigate(action)
                        }
                        else {
                            val action = ProductFragmentDirections.actionProductFragmentToOrderConfirmFragment(
                                productCode = productCode
                            )
                            findNavController().navigate(action)
                        }
                    }
                    else {
                        Toast.makeText(context, "Fetching Problem", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        binding.likeHandle.setOnClickListener {
            onLikeClick()
        }
    }

    // get cart
    private fun getCart(userId: String, productCode: String): DocumentReference {
       return db.collection("users").document(userId).collection("cart").document(productCode)
    }

    // cart
    private fun addCart() {
        GlobalScope.launch {
            val shopId = shopId
            val productImageUrl = productImageUrl
            val productName = productName
            val productPrice = productPrice
            val productCode = productCode
            val currentTime = System.currentTimeMillis()

            val cartData = CartData(
                currentTime,
                productCode,
                shopId,
                productImageUrl,
                productName,
                productPrice,
                shopName,
                shopImageUrl,
                token,
                productDescription
            )
            val userId = auth.currentUser!!.uid
            getCart(userId, productCode).set(cartData)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Add To Cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // like click handle
    private fun onLikeClick() {
        if (clicked) {
            binding.likeHandle.setImageResource(R.drawable.like_icon)
            GlobalScope.launch {
                val time = System.currentTimeMillis()
                val user = auth.currentUser!!.uid
                val data = ProductData(
                productCode = productCode,
                shopId = shopId,
                createdAt = time
                )
                val like = db.collection("shop").document(shopId).collection("product").document(productCode).get().await().toObject(ProductData::class.java)
                like?.likes?.add(user)
                db.collection("users").document(user).collection("liked_products").document(productCode).set(data)
                db.collection("shop").document(shopId).collection("product").document(productCode).set(like!!)
            }
            clicked = false
        }
        else {
            clicked = true
            val user = auth.currentUser!!.uid
            GlobalScope.launch {
                val like = db.collection("shop").document(shopId).collection("product").document(productCode).get().await().toObject(ProductData::class.java)
                like?.likes?.remove(user)
                db.collection("users").document(user).collection("liked_products").document(productCode).delete()
                db.collection("shop").document(shopId).collection("product").document(productCode).set(like!!)
            }
            binding.likeHandle.setImageResource(R.drawable.dislike_icon)
        }
    }

    // like handle
    private fun handleLikeIcon() {
        GlobalScope.launch {
            val userId = auth.currentUser!!.uid
            db.collection("users").document(userId).collection("liked_products").document(productCode).get().addOnCompleteListener { task ->
                if (task.isSuccessful)  {
                    val document = task.result
                    if (document != null) {
                        if (document.exists()) {
                            binding.likeHandle.setImageResource(R.drawable.like_icon)
                        }
                        else {
                            binding.likeHandle.setImageResource(R.drawable.dislike_icon)
                        }
                    }
                    else {k
                        Log.d("TAG", "Error: ", task.exception)
                    }
                }
            }
        }
    }

    // get product data
    private fun getProductsData() {
        GlobalScope.launch {
            db.collection("products").document(productCode).get().addOnSuccessListener { snapshot ->
                val data = snapshot.toObject(ProductData::class.java)
                productImageUrl = data?.productImageUrl.toString()
                productName = data?.productName.toString()
                productPrice = data?.productPrice.toString()
                productDescription = data?.productDescription.toString()
                shopName = data?.shop?.shopName.toString()
                shopImageUrl = data?.shop?.shopImageUrl.toString()
                shopId = data?.shopId.toString()
                token = data?.token.toString()
                Glide.with(requireActivity()).load(productImageUrl).centerCrop().into(binding.productShowImage)
                binding.productShowNameText.text =  productName
                binding.productShowPriceText.text = productPrice
                binding.shopNameShowTextView.text = shopName
                binding.description.text = productDescription
                Glide.with(requireActivity()).load(shopImageUrl).centerCrop().into(binding.shopShowImage)
            }
        }
    }
}