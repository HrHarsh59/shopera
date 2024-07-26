package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_produt_search_result.*
import project.demo.shopera.adapter.ProductSearchAdapter
import project.demo.shopera.databinding.FragmentProdutSearchResultBinding
import project.demo.shopera.model.ProductData

class ProductSearchResultFragment : Fragment() {
    private var _binding: FragmentProdutSearchResultBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var result: String

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var adapter: ProductSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProdutSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel.searchText.observe(requireActivity()) { value ->
            result = value
        }


        val productCollections = db.collection("products").whereArrayContains("keywords", result)
        val query = productCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ProductData>().setQuery(query, ProductData::class.java).build()
        adapter = ProductSearchAdapter(recyclerViewOption)

        product_search_recyclerview.adapter = adapter
        product_search_recyclerview.layoutManager = LinearLayoutManager(requireContext())
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