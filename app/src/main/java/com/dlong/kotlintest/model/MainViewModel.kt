package com.dlong.kotlintest.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dlong.kotlintest.database.BillDatabase
import com.dlong.kotlintest.entity.BillInfo
import kotlinx.coroutines.launch

/**
 * 页面数据管理器
 *
 * @author D10NG
 * @date on 2019-10-17 16:25
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db : BillDatabase = BillDatabase.getDatabase(application)

    private var allDayBill : LiveData<List<BillInfo>>

    init {
        allDayBill = db.getBillDao().getDayAll(2019, 10, 18)
    }

    fun getAllDayBill() : LiveData<List<BillInfo>> = allDayBill

    fun updateAllDayBill(year : Int, month : Int, day : Int) {
        allDayBill = db.getBillDao().getDayAll(year, month, day)
    }

    fun insertBill(info : BillInfo) = viewModelScope.launch{
        db.getBillDao().insert(info)
    }

    fun updateBill(info: BillInfo) = viewModelScope.launch {
        db.getBillDao().update(info)
    }

    fun deleteBill(info: BillInfo) = viewModelScope.launch {
        db.getBillDao().delete(info)
    }
}