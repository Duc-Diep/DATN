package com.ducdiep.bookmarket.ui.manage

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.*
import com.ducdiep.bookmarket.databinding.ActivityManageBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.manage.accounts.ManageAccountFragment
import com.ducdiep.bookmarket.ui.manage.books.ManageBookFragment
import com.ducdiep.bookmarket.ui.manage.categories.ManageCategoryFragment
import com.ducdiep.bookmarket.ui.manage.orders.ManageOrderFragment
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class ManageActivity : BaseActivity(R.layout.activity_manage),
    NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ActivityManageBinding::inflate)

    lateinit var manageViewModel: ManageViewModel

    var idChecked = R.id.manage_books

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(binding.tbManage)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.dlManage,
            binding.tbManage,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.dlManage.addDrawerListener(toggle)
        toggle.syncState()
        setCurrentFragment(ManageBookFragment())
        manageViewModel.stackFragment.add(KEY_MANAGE_BOOK)

        binding.nvManage.menu.findItem(R.id.manage_books).isChecked = true
        binding.nvManage.setNavigationItemSelectedListener(this)
    }

    private fun initViewModel() {
        manageViewModel = ViewModelProvider(this).get(ManageViewModel::class.java)
        manageViewModel.userDetails.observe(this){
            if (it!=null){
                setData(it)
            }
        }
    }

    private fun setData(user: User) {
        val view = binding.nvManage.getHeaderView(0)
        val image = view.findViewById<CircleImageView>(R.id.civ_avatar)
        Glide.with(this).load(user.avatar).into(image)
        val name = view.findViewById<TextView>(R.id.tv_user_name_header)
        name.text = user.full_name
        val email = view.findViewById<TextView>(R.id.tv_email_header)
        email.text = user.email
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.manage_books -> {
                if (idChecked!=R.id.manage_books){
                    setCurrentFragment(ManageBookFragment())
                    manageViewModel.stackFragment.add(KEY_MANAGE_BOOK)
                    binding.tbManage.title = "Quản lý sách"
                }
            }
            R.id.manage_category -> {
                if (idChecked!=R.id.manage_category){
                    setCurrentFragment(ManageCategoryFragment())
                    manageViewModel.stackFragment.add(KEY_MANAGE_CATEGORY)
                    binding.tbManage.title = "Quản lý danh mục"
                }
            }
            R.id.manage_account -> {
                if(idChecked!=R.id.manage_account){
                    setCurrentFragment(ManageAccountFragment())
                    manageViewModel.stackFragment.add(KEY_MANAGE_ACCOUNT)
                    binding.tbManage.title = "Quản lý tài khoản"
                }
            }
            R.id.manage_order -> {
                if (idChecked!=R.id.manage_order){
                    setCurrentFragment(ManageOrderFragment())
                    manageViewModel.stackFragment.add(KEY_MANAGE_ORDER)
                    binding.tbManage.title = "Quản lý dơn hàng"
                }
            }
        }
        idChecked = item.itemId
        resetChecked()
        binding.dlManage.closeDrawer(GravityCompat.START)
        return true
    }

    private fun resetChecked() {
        for (i in 0 until binding.nvManage.menu.size()) {
            val menuItem = binding.nvManage.menu.getItem(i)
            menuItem.isChecked = menuItem.itemId == idChecked
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
            replace(R.id.nav_host_manage, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onBackPressed() {
        if (binding.dlManage.isDrawerOpen(GravityCompat.START)) {
            binding.dlManage.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                if (manageViewModel.stackFragment.isNotEmpty()) {
                    manageViewModel.stackFragment.removeLast()
                    when (manageViewModel.stackFragment.last()) {
                        KEY_MANAGE_BOOK -> {
                            idChecked = R.id.manage_books
                            resetChecked()
                            binding.tbManage.title = "Quản lý sách"
                        }
                        KEY_MANAGE_CATEGORY -> {
                            idChecked = R.id.manage_category
                            resetChecked()
                            binding.tbManage.title = "Quản lý danh mục"
                        }
                        KEY_MANAGE_ACCOUNT -> {
                            idChecked = R.id.manage_account
                            resetChecked()
                            binding.tbManage.title = "Quản lý tài khoản"
                        }
                        KEY_MANAGE_ORDER -> {
                            idChecked = R.id.manage_order
                            resetChecked()
                            binding.tbManage.title = "Quản lý đơn hàng"
                        }
                    }
                    supportFragmentManager.popBackStack()
                }
            } else {
                finish()
            }
        }
    }
}