package project.demo.shopera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import project.demo.shopera.model.ShopData

class IdsViewModel: ViewModel() {
    private val _ids: MutableLiveData<ArrayList<String>> = MutableLiveData()
//    val ids: MutableLiveData<ArrayList<String>> = _ids
    private val db = FirebaseFirestore.getInstance()
    private lateinit var allShop: ArrayList<String>

    fun setIds() {
        db.collection("shop").addSnapshotListener { snapshot, e ->

            if (e != null) {
                Log.d("TESTING", "LISTEN FAILED", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allShop = ArrayList<String>()
                val document = snapshot.documents
                document.forEach {
                    val shop = it.toObject(ShopData::class.java)
                    if (shop != null) {
                        val id = shop.shopId
                        allShop.add(id!!)
                    }
                }
                _ids.value = allShop
                Log.d("TESTING", "ID IS : ${_ids.value}")
            }
        }
    }

    fun getIds(): LiveData<ArrayList<String>> = _ids
}