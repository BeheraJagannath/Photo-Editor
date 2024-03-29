package com.example.neweditor.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neweditor.R
import com.example.neweditor.data.FontItemData
import com.example.neweditor.databinding.FontRecyclerviewItemBinding

class FontTextStickerAdapter(var clickListener: FontTextStickerListener?=null) :
    ListAdapter<FontItemData, FontTextStickerAdapter.ViewHolder>(TextStickerDiffUtilCallback()) {


    private var selectedPosition=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        /**
         * Binding Creating
         */

        val bindView = FontRecyclerviewItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(bindView)


    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item!!, clickListener)
        holder.binding.textView.typeface  = item.font

        if (selectedPosition==position){
            holder.binding.fontCardView.apply {
                strokeWidth = 4
                strokeColor=    ContextCompat.getColor(context,R.color.seekbar_progress_color)

            }

        }else{
            holder.binding.fontCardView.apply {
                strokeWidth = 0
                strokeColor=android.R.color.transparent

            }
        }

    }


    fun  setSingleSelection(position: Int) {

        if (position==RecyclerView.NO_POSITION) return

        notifyItemChanged(selectedPosition)
        selectedPosition=position
        notifyItemChanged(selectedPosition)

    }

    class ViewHolder(val binding: FontRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemData: FontItemData, clickListener: FontTextStickerListener?) {

            binding.fontItemData = itemData
            binding.clickListener = clickListener
            binding.position=adapterPosition

            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()

        }


    }


}


/**
 * Diff Util
 */

class TextStickerDiffUtilCallback : DiffUtil.ItemCallback<FontItemData>() {
    override fun areItemsTheSame(oldItem: FontItemData, newItem: FontItemData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FontItemData, newItem: FontItemData): Boolean {
        return oldItem == newItem
    }
}


/**
 * Click-Listener
 */
class FontTextStickerListener(val clickListener: (typeface: Typeface,fontName:String,position: Int) -> Unit) {
    fun onClick(fontItemData: FontItemData,position: Int) = clickListener(fontItemData.font,fontItemData.name,position)

}