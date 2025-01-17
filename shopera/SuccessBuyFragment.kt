package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_success_buy.*

class SuccessBuyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        more_explore_button.setOnClickListener {
            findNavController().navigate(R.id.action_successBuyFragment_to_mainFragment)
        }
    }
}