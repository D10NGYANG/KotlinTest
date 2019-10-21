package com.dlong.kotlintest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dlong.kotlintest.dao.BillDao
import com.dlong.kotlintest.entity.BillInfo

/**
 * 数据库管理
 *
 * @author D10NG
 * @date on 2019-10-17 16:04
 */
@Database(entities = [BillInfo::class], version = 1)
abstract class BillDatabase : RoomDatabase() {

    abstract fun getBillDao() : BillDao

    companion object {

        // 单例
        @Volatile
        private var INSTANCE : BillDatabase? = null

        fun getDatabase(context : Context) : BillDatabase {
            val temp = INSTANCE
            if (null != temp) {
                return temp
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BillDatabase::class.java,
                    "bill_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}