package com.ducdiep.bookmarket.ui.client.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseActivity
import com.ducdiep.bookmarket.databinding.ActivityMainBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.cart.CartFragment
import com.ducdiep.bookmarket.ui.client.category.CategoryFragment
import com.ducdiep.bookmarket.ui.client.home.HomeFragment
import com.ducdiep.bookmarket.ui.client.userinfor.UserInforFragment

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    var idChecked = R.id.homeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()

    }

    private fun initViews() {
        setCurrentFragment(HomeFragment.newInstance())
        binding.bottomNavi.setOnItemSelectedListener { item ->
            selectFragment(item)
            false
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
            replace(R.id.nav_host, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun selectFragment(item: MenuItem) {

        when (item.itemId) {
            R.id.homeFragment ->
                setCurrentFragment(HomeFragment())
            R.id.categoryFragment -> {
                setCurrentFragment(CategoryFragment())
            }
            R.id.cartFragment -> {
                setCurrentFragment(CartFragment())
            }
            R.id.userInforFragment -> {
                setCurrentFragment(UserInforFragment())
            }
            else -> item.itemId
        }
        idChecked = item.itemId
        // uncheck the other items.
        for (i in 0 until binding.bottomNavi.menu.size()) {
            val menuItem = binding.bottomNavi.menu.getItem(i)
            if (menuItem.itemId == idChecked) menuItem.isChecked = true
        }
    }
}