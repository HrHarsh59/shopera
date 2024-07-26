package project.demo.shopera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_search_results.*
import project.demo.shopera.adapter.SearchViewPagerAdapter

class SearchResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = SearchViewPagerAdapter(childFragmentManager)
        search_viewPager.adapter = adapter
        search_tabs.setupWithViewPager(search_viewPager)
    }

}