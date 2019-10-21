package com.dlong.kotlintest.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dlong.kotlintest.entity.BillInfo

/**
 * 数据表操作接口
 *
 * @author D10NG
 * @date on 2019-10-17 15:20
 */
@Dao
interface BillDao {

    // 查询当日所有账单
    @Query("SELECT * FROM bill_table WHERE year = (:year) AND month = (:month) AND day = (:day) ORDER BY lastTime desc")
    fun getDayAll(year : Int, month : Int, day : Int) : LiveData<List<BillInfo>>

    // 插入新账单
    @Insert
    suspend fun insert(info : BillInfo)

    // 修改账单
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(info: BillInfo)

    // 删除账单
    @Delete
    suspend fun delete(info: BillInfo)

    // 查询一个
    @Query("SELECT * FROM bill_table WHERE id = (:id) LIMIT 0,1")
    fun getInfoById(id : Int) : LiveData<BillInfo>
}