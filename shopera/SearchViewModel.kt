package project.demo.shopera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _searchText = MutableLiveData<String>("")
    val searchText: LiveData<String> = _searchText

    fun setText(text: String) {
        _searchText.value = text
    }
}