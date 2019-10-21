package com.dlong.kotlintest

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlong.kotlintest.adapter.BillListAdapter
import com.dlong.kotlintest.databinding.ActivityMainBinding
import com.dlong.kotlintest.entity.BillInfo
import com.dlong.kotlintest.model.MainViewModel
import com.dlong.kotlintest.utils.DateUtils
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), BaseHandler.BaseHandlerCallBack {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : BillListAdapter
    private lateinit var viewModel : MainViewModel

    companion object {
        const val EDIT_BILL_OK = 101
    }

    private val mHandler = BaseHandler(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 声明绑定
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // 初始化ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // 显示当前年月
        binding.year = DateUtils.curYear
        binding.month = DateUtils.curMonth
        binding.day = DateUtils.curDay
        // 更新账单列表数据
        updateAllBill()
        // 显示日期选项
        selectDay()
        // tab选择
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.day = binding.tabLayout.selectedTabPosition + 1
                updateAllBill()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) { }
            override fun onTabReselected(tab: TabLayout.Tab) {
                updateAllBill()
            }
        })
        // 初始化adapter
        adapter = BillListAdapter(this, mHandler)
        // 初始化rcv
        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            EDIT_BILL_OK -> {
                if (null == data) return
                binding.year = data.getIntExtra("year", DateUtils.curYear)
                binding.month = data.getIntExtra("month", DateUtils.curMonth)
                binding.day = data.getIntExtra("day", DateUtils.curDay)
                selectDay()
                // 延迟500毫秒执行，避免执行不成功
                mHandler.sendEmptyMessageDelayed(EDIT_BILL_OK, 500)
            }
        }
    }

    override fun callBack(msg: Message) {
        when(msg.what) {
            BillListAdapter.CLICK_MENU_EDIT -> {
                // 编辑
                val info : BillInfo = msg.obj as BillInfo
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra("bill", info)
                startActivityForResult(intent, EDIT_BILL_OK)
            }
            BillListAdapter.CLICK_MENU_DELETE -> {
                // 删除
                val info : BillInfo = msg.obj as BillInfo
                viewModel.deleteBill(info)
            }
            EDIT_BILL_OK -> {
                updateAllBill()
            }
        }
    }

    private fun updateAllBill() {
        Log.e("测试", "更新日：${binding.day}")
        // 更新账单列表数据
        viewModel.updateAllDayBill(binding.year, binding.month, binding.day)
        // 监听列表变化
        viewModel.getAllDayBill().observe(this, Observer { list ->
            list?.let {
                adapter.update(it)
                var earn = 0f
                var pay = 0f
                for (i in it.listIterator()) {
                    if (i.isPay) {
                        pay += i.number.toFloat()
                    } else {
                        earn += i.number.toFloat()
                    }
                }
                binding.dayAllEarnValue = earn
                binding.dayAllPayValue = pay
            }
        })
    }

    private fun selectDay() {
        // 移除旧日期选项
        binding.tabLayout.removeAllTabs()
        // 填入新的日期选项
        for(i in 1..DateUtils.getDaysOfMonth(binding.year, binding.month)) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("${i}日"), false)
        }
        // 选中
        binding.tabLayout.getTabAt(binding.day - 1)?.select()
        // 转到对应位置
        binding.tabLayout.post {
            Thread.sleep(500)
            binding.tabLayout.setScrollPosition(0, (binding.day - 1).toFloat(), false)
        }
    }

    /**
     * 打开日历弹窗
     */
    fun openCalenderDialog(v: View) {
        val dialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                binding.year = y
                binding.month = m + 1
                binding.day = d
                selectDay()
                updateAllBill()
            }, binding.year, binding.month -1, binding.day)
        dialog.show()
    }

    /**
     * 添加账单
     */
    fun addBill(v: View) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("bill", BillInfo())
        startActivityForResult(intent, EDIT_BILL_OK)
    }
}
