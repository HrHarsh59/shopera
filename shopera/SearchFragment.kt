package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_search.*
import project.demo.shopera.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        topAppBar_search.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        search_icon.setOnClickListener {
            sharedViewModel.setText(binding.searchText.text.trim().toString())
            search_result.visibility = View.VISIBLE
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.search_result, SearchResultsFragment())
            transaction?.commit()
        }

        apparel_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "apparel")
            findNavController().navigate(action)
        }

        beauty_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "beauty")
            findNavController().navigate(action)
        }

        shoes_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "shoes")
            findNavController().navigate(action)
        }

        electronics_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "electronics")
            findNavController().navigate(action)
        }

        furniture_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "furniture")
            findNavController().navigate(action)
        }

        stationary_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "stationary")
            findNavController().navigate(action)
        }

        home_category.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSpecificCategoryFragment(category = "home")
            findNavController().navigate(action)
        }

    }
}