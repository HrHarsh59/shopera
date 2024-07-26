package project.demo.shopera.menubar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_cart.*
import project.demo.shopera.R
import project.demo.shopera.adapter.CartAdapter
import project.demo.shopera.model.CartData

class CartFragment : Fragment() {

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isShow = true
        var scrollRange = -1

        appbar_layout_cart.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar_cart.title = "Cart"
                isShow = true
            }
            else if (isShow) {
                collapsingToolbar_cart.title = " "
                isShow = false
            }
        })

        toolbar_cart.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val userId = auth.currentUser!!.uid
        val userCartCollection = db.collection("users").document(userId).collection("cart")
        val query = userCartCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<CartData>().setQuery(query, CartData::class.java).build()
        adapter = CartAdapter(recyclerViewOption)

        recyclerView_cart.adapter = adapter
        recyclerView_cart.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}