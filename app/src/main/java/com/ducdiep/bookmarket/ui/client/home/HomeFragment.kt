package com.ducdiep.bookmarket.ui.client.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentHomeBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var homeViewModel: HomeViewModel
    private val binding by viewBinding(FragmentHomeBinding::bind)
    var listSlideModel: ArrayList<SlideModel> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObsever()
    }

    private fun initObsever() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.listBooks.observe(viewLifecycleOwner) {
            filterSlide(it)
        }
    }

    private fun filterSlide(arrayList: ArrayList<Book>) {
        if (arrayList.isNotEmpty()) {
            listSlideModel.clear()
            arrayList.find {
                it.name == "Quản Lý Những Điều Cốt Lõi"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide1, it))
            }
            arrayList.find {
                it.name == "Giải Mã Chiến Lược Đông Tây"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide2, it))
            }
            arrayList.find {
                it.name == "Ép Mình Phải Xinh Phải Dữ"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide3, it))
            }
            arrayList.find {
                it.name == "Ra Bờ Suối Ngắm Hoa Kèn Hồng"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide4, it))
            }
            Log.d("abcc", "filterSlide: $listSlideModel")
            setupAdapter()
        }
    }

    private fun setupAdapter() {
        val slideAdapter = SlideAdapter(requireContext(), listSlideModel) {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        }
        binding.vpSlide.adapter = slideAdapter
        binding.ciDot.setViewPager(binding.vpSlide)
    }

    companion object {
        fun newInstance() =
            HomeFragment().apply {

            }
    }
}