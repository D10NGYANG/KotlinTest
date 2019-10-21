package com.dlong.kotlintest.adapter

import android.content.Context
import android.os.Handler
import android.os.Message
import android.os.Message.obtain
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dlong.kotlintest.R
import com.dlong.kotlintest.databinding.ItemBillBinding
import com.dlong.kotlintest.entity.BillInfo

/**
 * 列表适配器
 *
 * @author D10NG
 * @date on 2019-10-18 09:35
 */
class BillListAdapter internal constructor(
    private val context : Context,
    private val handler : Handler
) : RecyclerView.Adapter<BillListAdapter.ViewHolder>() {

    companion object {
        const val CLICK_MENU_EDIT = 1001
        const val CLICK_MENU_DELETE = 1002
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mList : List<BillInfo> = emptyList()

    internal fun update(list : List<BillInfo>) {
        this.mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemBillBinding = DataBindingUtil.inflate(inflater, R.layout.item_bill, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
        holder.getBind().btnMenu.setOnClickListener { view ->
            // 显示弹窗
            val pop = PopupMenu(context, view)
            pop.menuInflater.inflate(R.menu.menu_bill, pop.menu)
            pop.setOnMenuItemClickListener {
                val m : Message = obtain()
                if (it.itemId == R.id.edit) {
                    m.what = Companion.CLICK_MENU_EDIT
                } else{
                    m.what = CLICK_MENU_DELETE
                }
                m.arg1 = position
                m.obj = mList[position]
                handler.sendMessage(m)
                true
            }
            pop.show()
        }
    }

    override fun getItemCount(): Int = mList.size

    inner class ViewHolder(private val binding: ItemBillBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data : BillInfo) {
            binding.bill = data
            binding.executePendingBindings()
        }

        fun getBind() : ItemBillBinding = binding
    }
}