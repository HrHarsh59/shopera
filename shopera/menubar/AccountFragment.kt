package project.demo.shopera.menubar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import project.demo.shopera.SignInActivity
import project.demo.shopera.adapter.LikeProductAdapter
import project.demo.shopera.databinding.FragmentAccountBinding
import project.demo.shopera.model.ProductData
import project.demo.shopera.model.UserData

class AccountFragment : Fragment() {

    private lateinit var _binding: FragmentAccountBinding
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth

    private lateinit var adapter: LikeProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        setLikeData()

        binding.closeAccount.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val singOutIntent = Intent(activity, SignInActivity::class.java)
            singOutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(singOutIntent)
            activity?.finish()
        }
    }

    private fun getData() {
        GlobalScope.launch {
            val userId = auth.currentUser!!.uid
            val data = db.collection("users").document(userId).get().await().toObject(UserData::class.java)
            val username = data!!.userName.toString()
            val userEmail = data!!.userEmail.toString()
            binding.userAccountName.text = username
            binding.userAccountEmail.text = userEmail
        }
    }

    private fun setLikeData() {
        val userId = auth.currentUser!!.uid
        val productCollections = db.collection("users").document(userId).collection("liked_products")
        val query = productCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<ProductData>().setQuery(query, ProductData::class.java).build()
        adapter = LikeProductAdapter(recyclerViewOption)

        likedProductRecyclerview.adapter = adapter
        likedProductRecyclerview.layoutManager = LinearLayoutManager(requireContext())
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