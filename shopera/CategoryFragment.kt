package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_category.*
import project.demo.shopera.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private lateinit var _binding: FragmentCategoryBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        apparel_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "apparel")
            findNavController().navigate(action)
        }

        beauty_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "beauty")
            findNavController().navigate(action)
        }

        shoes_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "shoes")
            findNavController().navigate(action)
        }

        electronics_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "electronics")
            findNavController().navigate(action)
        }

        furniture_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "furniture")
            findNavController().navigate(action)
        }

        stationary_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "stationary")
            findNavController().navigate(action)
        }

        home_card.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category = "home")
            findNavController().navigate(action)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragment()
    }

}