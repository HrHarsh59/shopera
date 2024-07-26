package project.demo.shopera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel: ViewModel() {
    private var _quantity = MutableLiveData(1)
    val quantity: LiveData<Int> = _quantity

    private var q = 1

    fun increaseQuantity() {
        val quantity = q++
        _quantity.value = quantity
    }

    fun decreaseQuantity() {
        if (q>1) {
            val quantity = q--
            _quantity.value = quantity
        }
    }
}