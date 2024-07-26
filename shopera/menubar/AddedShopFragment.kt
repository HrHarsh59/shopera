package project.demo.shopera.menubar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_added_shop.*
import project.demo.shopera.R
import project.demo.shopera.adapter.AddedShopAdapter
import project.demo.shopera.model.Shop

class AddedShopFragment : Fragment() {

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: AddedShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_added_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isShow = true
        var scrollRange = -1

        appbar_layout_added_shop.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar_added_shop.title = "Added Shop"
                isShow = true
            }
            else if (isShow) {
                collapsingToolbar_added_shop.title = " "
                isShow = false
            }
        })

        toolbar_added_shop.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val userId = auth.currentUser!!.uid
        val addedShopCollection = db.collection("users").document(userId).collection("addedShop")
        val query = addedShopCollection.orderBy("shopName", Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Shop>().setQuery(query, Shop::class.java).build()
        adapter = AddedShopAdapter(recyclerViewOption)

        recyclerView_added_shop.adapter = adapter
        recyclerView_added_shop.layoutManager = GridLayoutManager(requireContext(), 2)
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