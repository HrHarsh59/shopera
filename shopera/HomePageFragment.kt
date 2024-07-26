package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home_page.*
import project.demo.shopera.adapter.ProductAdapter
import project.demo.shopera.databinding.FragmentHomePageBinding
import project.demo.shopera.model.ProductData

class HomePageFragment : Fragment() {

    private lateinit var _binding: FragmentHomePageBinding
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth

    private lateinit var adapter: ProductAdapter

    private lateinit var allShop: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finishAffinity()
            System.exit(0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val productCollections = db.collection("products")
        val query = productCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ProductData>().setQuery(query, ProductData::class.java).build()
        adapter = ProductAdapter(recyclerViewOption)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        fun newInstance() = HomePageFragment()
    }
}