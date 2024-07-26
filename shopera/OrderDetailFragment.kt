package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import project.demo.shopera.databinding.FragmentOrderDetailBinding
import project.demo.shopera.model.OrderData
import java.text.SimpleDateFormat
import java.util.*


class OrderDetailFragment : Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderId: String
    private lateinit var shopId: String

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            orderId = it.getString("orderId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.toolbarOrderDetail.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        getData()
    }

//    private fun setUserAddress() {
//        GlobalScope.launch {
//            val userId = auth.currentUser!!.uid
//            db.collection("users").document(userId).collection("userAddress")
//                .addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Toast.makeText(context, "Listened Failed", Toast.LENGTH_LONG).show()
//                        return@addSnapshotListener
//                    }
//                    if (snapshot != null) {
//                        val document = snapshot.documents
//                        document.forEach {
//                            val address = it.toObject(UserAddress::class.java)
//
//                        }
//                    }
//                }
//        }
//    }


    private fun getData() {
        GlobalScope.launch {
            val userId = auth.currentUser!!.uid
            db.collection("users").document(userId).collection("order").document(orderId).get().addOnSuccessListener { snapshot ->
                val data = snapshot.toObject(OrderData::class.java)
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val calender = Calendar.getInstance()
                calender.timeInMillis = data?.createdAt?.toLong()!!
                val date = formatter.format(calender.time)
                binding.setOrderDate.text = date
                binding.setOrderId.text = "#${data?.orderId}"
                binding.setOrderQuantity.text = data?.quantity.toString()
                binding.setOrderTotal.text = data?.productPrice.toString()
                binding.productNameOrderDetail.text = data?.productName.toString()
                Glide.with(requireActivity()).load(data?.productImageUrl.toString()).centerCrop().into(binding.productImageOrderDetail)
                binding.orderDetailShopName.text = data?.shopData.shopName.toString()
                binding.orderDetailShopAddress.text = data?.shopData.shopLocation.toString()
                binding.orderDetailBuyerName.text = data?.userAddress.userName.toString()
                binding.orderDetailBuyerAddressDetail.text = data?.userAddress.place.toString()
                binding.orderDetailBuyerAddressArea.text = data?.userAddress.area.toString()
                binding.orderDetailBuyerAddressLandmark.text =
                    data?.userAddress.landMark.toString()
                binding.orderDetailBuyerAddressCity.text =
                    "${data?.userAddress.city.toString()}, ${data?.userAddress.pinCode.toString()}"
            }
        }
    }

}