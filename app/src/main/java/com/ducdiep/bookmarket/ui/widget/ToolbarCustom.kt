package com.ducdiep.bookmarket.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.databinding.LayoutToolbarBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.main.MainActivity

class ToolbarCustom : RelativeLayout {
    private val binding by viewBinding(LayoutToolbarBinding::bind)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar, this)
        initViews(attrs)
    }

    private fun initViews(attrs: AttributeSet?) {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ToolbarCustom).apply {
                try {
                    initUI(this)
                    initListener()
                } finally {
                    recycle()
                }
            }
        }
    }

    private fun initListener() {
        binding.imgBack.setOnClickListener {
            (context as MainActivity).onBackPressed()
        }
    }

    private fun initUI(typedArray: TypedArray) {
        binding.imgBack.visibility = if (typedArray.getBoolean(
                R.styleable.ToolbarCustom_is_show_back_button,
                false
            )
        ) View.VISIBLE else View.GONE
        binding.tvTitleToolbar.text = typedArray.getString(R.styleable.ToolbarCustom_title)
    }

    fun setTitle(str: String) {
        binding.tvTitleToolbar.text = str
    }
}