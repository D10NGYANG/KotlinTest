package com.dlong.kotlintest.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 账单信息
 *
 * @author D10NG
 * @date on 2019-10-17 14:58
 */
@Entity(tableName = "bill_table")
data class BillInfo(
    // 账单ID，主键，自增长
    @PrimaryKey(autoGenerate = true)
    var id : Int = -1 ,
    // 账单项目名称
    var name : String = "",
    // 是否为支出
    var isPay : Boolean = false,
    // 金额
    var number : String = "0",
    // 年
    var year : Int = 0,
    // 月
    var month : Int = 0,
    // 日
    var day : Int = 0,
    // 上一次修改时间
    var lastTime : Long = 0
) : Serializable