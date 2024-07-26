package project.demo.shopera.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import project.demo.shopera.OrdersBuyingFragment
import project.demo.shopera.OrdersDeliveredFragment

class OrderViewPagerAdapter(supportFragmentManager: FragmentManager)
    : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return OrdersBuyingFragment()
            }

            else -> {
                return OrdersDeliveredFragment()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position) {
            0 -> {
                return "Orders"
            }

            else -> {
                return "Delivered"
            }
        }

        return super.getPageTitle(position)
    }

}