package project.demo.shopera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.*
import project.demo.shopera.databinding.FragmentOrderConfirmBinding
import project.demo.shopera.model.*
import java.text.NumberFormat
import java.util.*

class OrderConfirmFragment : Fragment() {

    private var _binding: FragmentOrderConfirmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by activityViewModels()

    var q = 1

    private lateinit var productImage: String
    private lateinit var productPrice: String
    private lateinit var productName: String
    private lateinit var shopTokenId: String
    private lateinit var shopId: String
    private lateinit var productCode: String
    private lateinit var shopImageUri: String
    private lateinit var shopName: String
    private lateinit var shopLocation: String
    private var price: String? = null

//    private var deliveryType: String? = null

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser!!.uid
    private val orderCollectionForShop = db.collection("shop")
    private val orderCollectionForUser = db.collection("users")


    // buyer address data
    private lateinit var buyerName: String
    private lateinit var buyerPlace: String
    private lateinit var buyerArea: String
    private lateinit var buyerLandmark: String
    private lateinit var buyerCity: String
    private lateinit var buyerPincode: String
    private lateinit var buyerMobileNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productCode = it.getString("productCode").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid


        // show products data
        getProductsData()


        // show address
        getAddress()

        binding.orderConfirm.setOnClickListener {

            sendOrder()
        }


        // close page
        binding.closeIcon.setOnClickListener {
            activity?.onBackPressed()
        }


        // quantity text
        binding.quantityText.text = q.toString()


        // quantity increase
        binding.quantityIncrease.setOnClickListener {
            q += 1
            Log.d("PRICE", price!!)
            val price = price!!.toInt()
            val p = price * q
            val formatedPrice =
                NumberFormat.getCurrencyInstance(Locale("en", "in")).format(p).toString()
            binding.orderedItemPrice.text = formatedPrice
            binding.totalPriceText.text = formatedPrice
            binding.quantityText.text = q.toString()
        }


        // quantity decrease
        binding.quantityDecrease.setOnClickListener {
            if (q > 1) {
                q -= 1
                val price = price!!.toInt()
                val p = price * q
                val formatedPrice =
                    NumberFormat.getCurrencyInstance(Locale("en", "in")).format(p).toString()
                binding.orderedItemPrice.text = formatedPrice
                binding.totalPriceText.text = formatedPrice
                binding.quantityText.text = q.toString()
            }
        }
    }

    // notification handle
    private fun notificationHandle(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("TAG", response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }


    // send notification
    private fun sendNotification() {
        val title = productName
        val message = productPrice
        val image = productImage
        val tokenId = shopTokenId
        if (title.isNotEmpty() && message.isNotEmpty()) {
            PushNotification(
                NotificationData(title, message, image),
                tokenId
            ).also {
                notificationHandle(it)
            }
        }
    }


    // send order
    private fun sendOrder() {
        val random = (0..999999).random()

        // set as shoper data
        GlobalScope.launch {
            val productCode = productCode
            val shopId = shopId
            val productImageUrl = productImage
            val productName = productName
            val productPrice = binding.totalPriceText.text.toString()
            val currentTime = System.currentTimeMillis()
            val deliveryType = null
            val orderQuantity = binding.quantityText.text
            val userAddress = UserAddress(
                buyerName,
                buyerMobileNumber,
                buyerPincode,
                buyerPlace,
                buyerArea,
                buyerLandmark,
                buyerCity
            )

            val shopData = ShopData(shopName = shopName,
            shopImageUrl = shopImageUri,
            shopLocation = shopLocation,
            shopId = shopId)

            val orderData = OrderData(
                currentTime,
                productCode,
                shopId,
                productImageUrl,
                productName,
                productPrice,
                random.toString(),
                deliveryType,
                userAddress = userAddress,
                quantity = orderQuantity.toString().trim(),
                shopData = shopData,
                deliveryStatus = "Not Delivered",
                paymentStatus = "UnPaid"
            )

            orderCollectionForShop.document(shopId).collection("order")
                .document(random.toString()).set(orderData).addOnSuccessListener {
                    Toast.makeText(
                        activity,
                        "$productName order successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                .addOnFailureListener {e ->
                    Toast.makeText(
                        activity,
                        e.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            delay(1000)
        }


        // set user data
        GlobalScope.launch {
            val userId = auth.currentUser!!.uid
            val productCode = productCode
            val shopId = shopId
            val productImageUrl = productImage
            val productName = productName
            val productPrice = binding.totalPriceText.text.toString()
            val currentTime = System.currentTimeMillis()
            val orderQuantity = binding.quantityText.text
            val deliveryType = null

            val userAddress = UserAddress(
                buyerName,
                buyerMobileNumber,
                buyerPincode,
                buyerPlace,
                buyerArea,
                buyerLandmark,
                buyerCity
            )

            val shopData = ShopData(shopName = shopName,
                shopImageUrl = shopImageUri,
                shopLocation = shopLocation,
                shopId = shopId)

            val orderData = OrderData(
                currentTime,
                productCode,
                shopId,
                productImageUrl,
                productName,
                productPrice,
                random.toString(),
                deliveryType,
                quantity = orderQuantity.toString().trim(),
                userAddress = userAddress,
                shopData = shopData,
                deliveryStatus = "Not Delivered",
                paymentStatus = "UnPaid"
            )

            orderCollectionForUser.document(userId).collection("order").document(random.toString())
                .set(orderData)
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_orderConfirmFragment_to_successBuyFragment)
            }
        }

        sendNotification()

    }


    // get product data
    private fun getProductsData() {
        GlobalScope.launch {
            db.collection("products").document(productCode).get().addOnSuccessListener { snapshot ->
                val data = snapshot.toObject(ProductData::class.java)
                productImage = data?.productImageUrl.toString()
                productName = data?.productName.toString()
                productPrice = data?.productPrice.toString()
                shopTokenId = data?.token.toString()
                shopId = data?.shopId.toString()
                shopImageUri = data?.shop?.shopImageUrl.toString()
                shopLocation = data?.shop?.shopLocation.toString()
                shopName = data?.shop?.shopName.toString()
                price = data?.price.toString()
                Glide.with(requireActivity()).load(productImage).centerCrop()
                    .into(binding.orderedItemImage)
                binding.orderedItemName.text = productName
                binding.orderedItemPrice.text = productPrice
                binding.totalPriceText.text = productPrice
            }
        }
    }


    // get user address data
    private fun getAddress() {
        GlobalScope.launch {
            // getting user address data
            db.collection("users").document(userId).collection("userAddress")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(context, "Listened Failed", Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val document = snapshot.documents
                        document.forEach {
                            val address = it.toObject(UserAddress::class.java)
                            buyerName = address?.userName.toString()
                            buyerPlace = address?.place.toString()
                            buyerLandmark = address?.landMark.toString()
                            buyerArea = address?.area.toString()
                            buyerCity = address?.city.toString()
                            buyerPincode = address?.pinCode.toString()
                            buyerMobileNumber =
                                "Contact Number : ${address?.mobileNumber.toString()}"
                            binding.buyerName.text = buyerName
                            binding.buyerAddressPlace.text = buyerPlace
                            binding.buyerAddressArea.text = buyerArea
                            binding.buyerAddressLandmark.text = buyerLandmark
                            binding.buyerAddressCity.text = buyerCity
                            binding.buyerAddressPinCode.text = buyerPincode
                            binding.buyerAddressMobileNumber.text = buyerMobileNumber

                        }
                    }
                }

        }
    }

}