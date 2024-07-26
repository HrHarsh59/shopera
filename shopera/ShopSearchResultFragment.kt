package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_shop_search_result.*
import project.demo.shopera.adapter.ShopSearchAdapter
import project.demo.shopera.databinding.FragmentShopSearchResultBinding
import project.demo.shopera.model.ShopData

class ShopSearchResultFragment : Fragment() {

    private var _binding: FragmentShopSearchResultBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()

    private val db = FirebaseFirestore.getInstance()

    private lateinit var adapter: ShopSearchAdapter

    private lateinit var result: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel.searchText.observe(requireActivity()) { value ->
            result = value
        }

        val shopCollections = db.collection("shop").whereArrayContains("keywords", result)
        val query = shopCollections.orderBy("shopName", Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ShopData>().setQuery(query, ShopData::class.java).build()
        adapter = ShopSearchAdapter(recyclerViewOption)

        shop_result_recyclerview.adapter = adapter
        shop_result_recyclerview.layoutManager = LinearLayoutManager(context)
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