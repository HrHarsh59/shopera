package project.demo.shopera.menubar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_order.*
import project.demo.shopera.R
import project.demo.shopera.adapter.OrderViewPagerAdapter

class OrderFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isShow = true
        var scrollRange = -1

        appbar_layout_order.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar_order.title = "My Orders"
                isShow = true
            }
            else if (isShow) {
                collapsingToolbar_order.title = " "
                isShow = false
            }
        })

        toolbar_order.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        setUpTabs()

    }

    private fun setUpTabs() {
        val adapter = OrderViewPagerAdapter(childFragmentManager)
        viewPager_order.adapter = adapter
        tabs_order.setupWithViewPager(viewPager_order)
    }
}