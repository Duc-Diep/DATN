package com.ducdiep.bookmarket.ui.client.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.*
import com.ducdiep.bookmarket.databinding.ActivityMainBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.cart.CartFragment
import com.ducdiep.bookmarket.ui.client.category.CategoryFragment
import com.ducdiep.bookmarket.ui.client.home.HomeFragment
import com.ducdiep.bookmarket.ui.client.userinfor.UserInforFragment

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    lateinit var mainViewModel: MainViewModel

    var idChecked = R.id.homeFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setCurrentFragment(HomeFragment.newInstance())
        mainViewModel.stackFragment.add(KEY_HOME_FRAGMENT)
        binding.bottomNavi.setOnItemSelectedListener { item ->
            selectFragment(item)
            false
        }
    }

    fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_bottom,
                R.anim.exit_to_top,
                R.anim.enter_from_top,
                R.anim.exit_to_bottom
            )
            replace(R.id.nav_host, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun selectFragment(item: MenuItem) {

        when (item.itemId) {
            R.id.homeFragment -> {
                setCurrentFragment(HomeFragment())
                mainViewModel.stackFragment.add(KEY_HOME_FRAGMENT)
            }
            R.id.categoryFragment -> {
                setCurrentFragment(CategoryFragment())
                mainViewModel.stackFragment.add(KEY_CATEGORY_FRAGMENT)
            }
            R.id.cartFragment -> {
                setCurrentFragment(CartFragment())
                mainViewModel.stackFragment.add(KEY_CART_FRAGMENT)
            }
            R.id.userInforFragment -> {
                setCurrentFragment(UserInforFragment())
                mainViewModel.stackFragment.add(KEY_USER_INFOR_FRAGMENT)
            }
            else -> item.itemId
        }
        idChecked = item.itemId
        // uncheck the other items.
        for (i in 0 until binding.bottomNavi.menu.size()) {
            val menuItem = binding.bottomNavi.menu.getItem(i)
            if (menuItem.itemId == idChecked) menuItem.isChecked = true
        }
        Log.d("abcc", "${mainViewModel.stackFragment}")
    }

    override fun onBackPressed() {
        Log.d("abcc", "onBackPressed: ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 1) {
            if (mainViewModel.stackFragment.isNotEmpty()) {
                mainViewModel.stackFragment.removeLast()
                when (mainViewModel.stackFragment.last()) {
                    KEY_HOME_FRAGMENT -> binding.bottomNavi.menu.getItem(0).isChecked = true
                    KEY_CATEGORY_FRAGMENT -> binding.bottomNavi.menu.getItem(1).isChecked = true
                    KEY_CART_FRAGMENT -> binding.bottomNavi.menu.getItem(2).isChecked = true
                    KEY_USER_INFOR_FRAGMENT -> binding.bottomNavi.menu.getItem(3).isChecked = true
                }
                supportFragmentManager.popBackStack()
                Log.d("abcc", "${mainViewModel.stackFragment}")
            }
        } else {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setNegativeButton("Không") { _, _ -> }
                .setPositiveButton("Có") { _, _ -> finish() }
                .create()
            dialog.show()
        }
    }
}