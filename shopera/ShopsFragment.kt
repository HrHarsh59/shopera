package project.demo.shopera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_shops.*
import project.demo.shopera.adapter.ShopAdapter
import project.demo.shopera.databinding.FragmentShopsBinding
import project.demo.shopera.model.ShopData

class ShopsFragment : Fragment() {

    private var _binding: FragmentShopsBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    private lateinit var adapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setData()
    }

    private fun setData() {
        val shopCollections = db.collection("shop")
        val query = shopCollections.orderBy("shopName", Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ShopData>().setQuery(query, ShopData::class.java).build()
        adapter = ShopAdapter(recyclerViewOption)

        recyclerView_shop.adapter = adapter
        recyclerView_shop.layoutManager = LinearLayoutManager(context)
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
        fun newInstance() = ShopsFragment()
    }
}