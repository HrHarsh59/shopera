package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import project.demo.shopera.adapter.ViewPagerAdapter

class MainFragment : Fragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            activity?.onBackPressed()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (context as AppCompatActivity).supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpTabs()

        navigation_menu.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_menuFragment)
        }

        search_bar.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }


}