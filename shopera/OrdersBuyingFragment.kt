package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_orders_buying.*
import project.demo.shopera.adapter.OrderAdapter
import project.demo.shopera.model.OrderData

class OrdersBuyingFragment : Fragment() {

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_buying, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = auth.currentUser!!.uid
        val userCartCollection = db.collection("users").document(userId).collection("order")
        val query = userCartCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<OrderData>().setQuery(query, OrderData::class.java).build()
        adapter = OrderAdapter(recyclerViewOption)

        recyclerView_order_page.adapter = adapter
        recyclerView_order_page.layoutManager = LinearLayoutManager(requireContext())
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