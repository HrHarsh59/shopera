package project.demo.shopera

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu.*
import project.demo.shopera.model.UserData

class MenuFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        close_menu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_mainFragment)
        }

        cart_page.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_cartFragment)
        }

        added_shop_page.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_addedShopFragment)
        }

        order_page.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_orderFragment)
        }

        user_account_image.setOnClickListener {
            db.collection("users").document(userId).get().addOnSuccessListener { snapshot ->
                val data = snapshot?.toObject(UserData::class.java)!!
                val name = data.userName.toString()
                val location = data.userLocation.toString()
                val email = data.userEmail.toString()

                if (name.isNotEmpty() && location.isNotEmpty() && email.isNotEmpty()) {
                    findNavController().navigate(R.id.action_menuFragment_to_accountFragment)
                }
                else {
                    val profileCreatePage = Intent(activity, ProfileCreateActivity::class.java)
                    startActivity(profileCreatePage)
                }
            }

        }
    }
}