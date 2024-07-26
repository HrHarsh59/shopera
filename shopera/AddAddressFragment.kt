package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import project.demo.shopera.databinding.FragmentAddAddressBinding
import project.demo.shopera.model.UserAddress

class AddAddressFragment : Fragment() {

    private lateinit var _binding: FragmentAddAddressBinding
    private val binding get() = _binding!!

    private lateinit var userName: String
    private lateinit var userMobileNumber: String
    private lateinit var pinCode: String
    private lateinit var place: String
    private lateinit var area: String
    private lateinit var landMark: String
    private lateinit var city: String

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val addressCollection = db.collection("users")

    private lateinit var productCode: String

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
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // upload address
        binding.addAddressButton.setOnClickListener {
            userName = binding.addNameEditText.text.toString()
            userMobileNumber = binding.addMobileNumberEditText.text.toString()
            pinCode = binding.addPinCodeEditText.text.toString()
            place = binding.addFlatEditText.text.toString()
            area = binding.addAreaEditText.text.toString()
            landMark = binding.addLandMarkEditText.text.toString()
            city = binding.addTownEditText.text.toString()

            when {
                userName.isNullOrEmpty() -> {
                    binding.addNameEditText.error = "Enter Name"
                    return@setOnClickListener
                }
                userMobileNumber.isNullOrEmpty() -> {
                    binding.addMobileNumberEditText.error = "Enter Mobile Number"
                    return@setOnClickListener
                }
                pinCode.isNullOrEmpty() -> {
                    binding.addPinCodeEditText.error = "Enter PinCode"
                    return@setOnClickListener
                }
                place.isNullOrEmpty() -> {
                   binding.addFlatEditText.error = "Enter Place"
                    return@setOnClickListener
                }
                area.isNullOrEmpty() -> {
                    binding.addAreaEditText.error = "Enter Area"
                    return@setOnClickListener
                }
                landMark.isNullOrEmpty() -> {
                    binding.addLandMarkEditText.error = "Enter Land Mark"
                    return@setOnClickListener
                }
                city.isNullOrEmpty() -> {
                    binding.addTownEditText.error = "Enter City"
                    return@setOnClickListener
                }
            }

            val userAddress = UserAddress(
                userName,
                userMobileNumber,
                pinCode,
                place,
                area,
                landMark,
                city
            )

            val userId = auth.currentUser!!.uid

            addressCollection.document(userId).collection("userAddress").document("default").set(userAddress).addOnSuccessListener {
                val action = AddAddressFragmentDirections.actionOrderDetailFragmentToOrderConfirmFragment(
                    productCode = productCode
                )
                findNavController().navigate(action)
            }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Sorry Some Problem : $error", Toast.LENGTH_SHORT).show()
                }
        }
    }

}