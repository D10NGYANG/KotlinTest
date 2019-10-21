package com.dlong.kotlintest.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dlong.kotlintest.database.BillDatabase
import com.dlong.kotlintest.entity.BillInfo
import kotlinx.coroutines.launch

/**
 * 编辑页面
 *
 * @author D10NG
 * @date on 2019-10-21 10:15
 */
class EditViewModel(application: Application) : AndroidViewModel(application) {

    private val db : BillDatabase = BillDatabase.getDatabase(application)

    fun insertBill(info : BillInfo) = viewModelScope.launch{
        db.getBillDao().insert(info)
    }

    fun updateBill(info: BillInfo) = viewModelScope.launch {
        db.getBillDao().update(info)
    }
}