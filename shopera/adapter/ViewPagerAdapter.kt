package project.demo.shopera.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import project.demo.shopera.*

class ViewPagerAdapter(supportFragmentManager: FragmentManager)
    : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return HomePageFragment()
            }
            1 -> {
                return ShopsFragment()
            }
            else -> {
                return CategoryFragment.newInstance()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position) {
            0 -> {
                return "HOME"
            }
            1 -> {
                return "SHOPS"
            }
            else -> {
                return "CATEGORY"
            }
        }

        return super.getPageTitle(position)
    }

}