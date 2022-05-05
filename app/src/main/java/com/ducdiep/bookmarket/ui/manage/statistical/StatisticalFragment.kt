package com.ducdiep.bookmarket.ui.manage.statistical

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentStatisticalBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.extensions.viewBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import java.util.*
import kotlin.math.roundToInt

class StatisticalFragment : BaseFragment(R.layout.fragment_statistical) {
    var currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private val listTime: ArrayList<Int> = arrayListOf()
    private val binding by viewBinding(FragmentStatisticalBinding::bind)
    lateinit var statisticalViewModel: StatisticalViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
        initObserve()
    }

    private fun initListener() {
        binding.imgPrev.setOnClickListener {
            if (currentMonth == 1) {

            } else {
                currentMonth -= 1
                binding.tvMonth.text = "Tháng $currentMonth"
                statisticalViewModel.getListDataChart(currentMonth)
            }
        }
        binding.imgNext.setOnClickListener {
            if (currentMonth == 12) {

            } else {
                currentMonth += 1
                binding.tvMonth.text = "Tháng $currentMonth"
                statisticalViewModel.getListDataChart(currentMonth)
            }
        }
    }

    private fun initViews() {
        binding.tvMonth.text = "Tháng $currentMonth"
    }

    private fun initObserve() {
        statisticalViewModel = ViewModelProvider(this).get(StatisticalViewModel::class.java)
        statisticalViewModel.listOrder.observe(viewLifecycleOwner) {
                statisticalViewModel.getListDataChart(currentMonth)
        }
        statisticalViewModel.listDataChart.observe(viewLifecycleOwner){
            initChart(it)
        }
    }

    private fun initChart(listData: LinkedHashMap<Int, Long>) {
        binding.bcData.apply {
            data = generateBarData(listData)
            animateXY(1000,1000)
            onChartGestureListener = object :OnChartGestureListener{
                override fun onChartGestureStart(
                    me: MotionEvent?,
                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                ) {

                }

                override fun onChartGestureEnd(
                    me: MotionEvent?,
                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                ) {

                }

                override fun onChartLongPressed(me: MotionEvent?) {

                }

                override fun onChartDoubleTapped(me: MotionEvent?) {
                }

                override fun onChartSingleTapped(me: MotionEvent?) {
                }

                override fun onChartFling(
                    me1: MotionEvent?,
                    me2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ) {
                }

                override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
                }

                override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                }

            }

            isScaleYEnabled = false
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            val xAxisValueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.roundToInt()
                    return listTime[index].toString()
                }
            }
            xAxis.apply {
                valueFormatter = xAxisValueFormatter
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                labelCount = 30
                isCenterAxisLabelsEnabled
                granularity = 1F
            }
            setScaleMinima(1.3F,1F)

            val yAxisValueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return getFormatMoney(value.toLong())
                }
            }
            axisLeft.apply {
                valueFormatter = yAxisValueFormatter
                spaceBottom = 0f
                setDrawAxisLine(false)
                axisMinimum = 0F
            }
            invalidate()
        }
    }

    private fun generateBarData(listData: LinkedHashMap<Int, Long>): BarData {
        listTime.apply {
            clear()
            addAll(listData.keys)
        }
        val listEntry: ArrayList<BarEntry> = arrayListOf()
        listData.values.forEachIndexed { index, l ->
            listEntry.add(BarEntry(index.toDouble().toFloat(), l.toFloat()))
        }
        val barDataSet = BarDataSet(listEntry, "")
        val barData = BarData(barDataSet)
        barData.setDrawValues(false)
        barData.barWidth = 0.5f
        return barData
    }
}