package project.demo.shopera.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import project.demo.shopera.ProductSearchResultFragment
import project.demo.shopera.ShopSearchResultFragment

class SearchViewPagerAdapter(supportFragmentManager: FragmentManager)
    : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return ProductSearchResultFragment()
            }
            else -> {
                return ShopSearchResultFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position) {
            0 -> {
                return "PRODUCT"
            }
            else -> {
                return "SHOP"
            }
        }

        return super.getPageTitle(position)
    }

}