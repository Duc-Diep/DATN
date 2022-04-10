package com.ducdiep.bookmarket.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {
}