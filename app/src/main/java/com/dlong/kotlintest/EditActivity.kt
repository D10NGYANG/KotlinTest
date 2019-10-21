package com.dlong.kotlintest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dlong.kotlintest.databinding.ActivityEditBinding
import com.dlong.kotlintest.entity.BillInfo
import com.dlong.kotlintest.model.EditViewModel
import com.dlong.kotlintest.utils.DateUtils
import com.google.android.material.tabs.TabLayout

class EditActivity : AppCompatActivity(), CalendarView.OnDateChangeListener {

    private lateinit var binding: ActivityEditBinding
    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 声明绑定
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        // 初始化ViewModel
        viewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        // 设置标题栏
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }
        // 获取传递过来的info
        binding.bill = intent.getSerializableExtra("bill") as BillInfo?
        if (binding.bill?.id == (-1) || null == binding.bill) {
            // 新建
            binding.bill?.year = DateUtils.curYear
            binding.bill?.month = DateUtils.curMonth
            binding.bill?.day = DateUtils.curDay
        }
        // 显示账单信息
        showBillInfo()
        // 日期选择
        binding.calendarView.setOnDateChangeListener(this)
        // 支出收入选择
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                binding.bill?.isPay = binding.tabs.selectedTabPosition == 1
            }
        })
        // 监听文本
        binding.tilName.editText!!.addTextChangedListener {
            binding.bill?.name = it.toString()
        }
        binding.tilNumber.editText!!.addTextChangedListener {
            binding.bill?.number = it.toString()
        }
    }

    /**
     * 日期选择
     */
    override fun onSelectedDayChange(p0: CalendarView, p1: Int, p2: Int, p3: Int) {
        binding.bill?.year = p1
        binding.bill?.month = p2 + 1
        binding.bill?.day = p3
    }

    private fun showBillInfo() {
        // 显示日期
        binding.calendarView.setDate(
            DateUtils.getDateFromYMD(binding.bill!!.year, binding.bill!!.month, binding.bill!!.day),
            true, true)
        // 显示支出或收入
        binding.tabs.getTabAt(if (binding.bill!!.isPay) 1 else 0)?.select()
    }

    /**
     * 点击保存
     */
    fun save(v: View) {
        if (binding.bill?.id == (-1)) {
            // 新建
            Log.e("测试", "新建项目名：${binding.bill?.name} ; 项目金额 ：¥ ${binding.bill?.number}")
            binding.bill?.id = 0
            binding.bill?.lastTime = DateUtils.curTime
            viewModel.insertBill(binding.bill!!)
        } else {
            // 更新
            Log.e("测试", "更新项目名：${binding.bill?.name} ; 项目金额 ：¥ ${binding.bill?.number}")
            binding.bill?.lastTime = DateUtils.curTime
            viewModel.updateBill(binding.bill!!)
        }
        val intent = Intent()
        intent.putExtra("year", binding.bill?.year)
        intent.putExtra("month", binding.bill?.month)
        intent.putExtra("day", binding.bill?.day)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
