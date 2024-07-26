package project.demo.shopera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_specific_category.*
import project.demo.shopera.adapter.CategoryProductAdapter
import project.demo.shopera.model.ProductData
import java.util.*

class SpecificCategoryFragment : Fragment() {

    private lateinit var category: String

    private val db = FirebaseFirestore.getInstance()

    private lateinit var adapter: CategoryProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString("category").toString()
            Log.d("CATEGORY", category)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specific_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collapsingToolbar.title = category.uppercase(Locale.getDefault())

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        if (category == "apparel") {
            category_image.setImageResource(R.drawable.apparel)
        }
        if (category == "beauty") {
            category_image.setImageResource(R.drawable.beauty)
        }
        if (category == "shoes") {
            category_image.setImageResource(R.drawable.shoes)
        }
        if (category == "electronics") {
            category_image.setImageResource(R.drawable.electronics)
        }
        if (category == "furniture") {
            category_image.setImageResource(R.drawable.furniture)
        }
        if (category == "stationary") {
            category_image.setImageResource(R.drawable.stationary)
        }
        if (category == "home") {
            category_image.setImageResource(R.drawable.home)
        }

        val productCollections = db.collection("products")
        val query = productCollections.orderBy("productName", Query.Direction.ASCENDING)
            .whereEqualTo("productCategory", category)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ProductData>().setQuery(query, ProductData::class.java).build()
        adapter = CategoryProductAdapter(recyclerViewOption)

        recyclerView_category_product.adapter = adapter
        recyclerView_category_product.layoutManager = LinearLayoutManager(requireContext())
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