package com.ducdiep.bookmarket.ui.manage.accounts

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_MANAGE_ACCOUNT
import com.ducdiep.bookmarket.databinding.FragmentManageAccountBinding
import com.ducdiep.bookmarket.extensions.getTimeNow
import com.ducdiep.bookmarket.extensions.showAlertDialog
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.manage.ManageActivity

class ManageAccountFragment : BaseFragment(R.layout.fragment_manage_account) {
    private val binding by viewBinding(FragmentManageAccountBinding::bind)
    lateinit var manageAccountViewModel: ManageAccountViewModel
    lateinit var accountAdapter: ManageAccountAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
        initObserve()
    }

    private fun initViews() {
        context?.let {
            accountAdapter = ManageAccountAdapter(it, listOf(), { id, active ->
                if (active == 0) {
                    context?.showAlertDialog(
                        "Xác nhận",
                        "Bạn có muốn mở khóa tài khoản này không"
                    ) {
                        manageAccountViewModel.updateField(id, hashMapOf("active" to 1))
                        manageAccountViewModel.updateField(
                            id,
                            hashMapOf("updated_at" to getTimeNow())
                        )
                    }
                } else {
                    context?.showAlertDialog("Xác nhận", "Bạn có muốn khóa tài khoản này không") {
                        manageAccountViewModel.updateField(id, hashMapOf("active" to 0))
                        manageAccountViewModel.updateField(
                            id,
                            hashMapOf("updated_at" to getTimeNow())
                        )
                    }
                }
            }, {
                (context as ManageActivity).setCurrentFragment(AccountDetailsFragment.newInstance(it))
                (context as ManageActivity).manageViewModel.stackFragment.add(KEY_MANAGE_ACCOUNT)
            })

            binding.rcvManageAccount.apply {
                adapter = accountAdapter
                addItemDecoration(DividerItemDecoration(it, RecyclerView.VERTICAL))
            }
        }
    }

    private fun initListener() {

    }

    private fun initObserve() {
        manageAccountViewModel = ViewModelProvider(this).get(ManageAccountViewModel::class.java)
        manageAccountViewModel.listUser.observe(viewLifecycleOwner) {
            accountAdapter.setData(it)
        }
        manageAccountViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

}