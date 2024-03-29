package com.example.neweditor.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neweditor.R
import com.example.neweditor.data.FiltersNavItemData
import com.example.neweditor.databinding.FiltersRecyclerItemBinding
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter



class FiltersNavItemsAdapter(var clickListener: FiltersNavItemListener?=null ) :
    ListAdapter<FiltersNavItemData, FiltersNavItemsAdapter.ViewHolder>(FiltersNavbarDiffCallback()) {

    private val TAG = FiltersNavItemsAdapter::class.java.simpleName

    private var selectedPosition=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Binding Created
        val bindView = FiltersRecyclerItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(bindView)

    }

    fun  setSingleSelection(position: Int) {

        if (position===RecyclerView.NO_POSITION) return

        notifyItemChanged(selectedPosition)
        selectedPosition=position
        notifyItemChanged(selectedPosition)

    }




    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
        holder.binding.imageView2.setImageBitmap(item.bitmap)


        if (selectedPosition==position){
            holder.binding.filterItemCardViewId.apply {
                strokeWidth = 6
                strokeColor = ContextCompat.getColor(context, R.color.teal_200)
            }

        }else{
            holder.binding.filterItemCardViewId.apply {
                strokeWidth = 0
                strokeColor = android.R.color.transparent
            }

        }

    }




    class ViewHolder(val binding: FiltersRecyclerItemBinding ) :
        RecyclerView.ViewHolder(binding.root) {



        fun bind(
            itemData: FiltersNavItemData,
            clickListner: FiltersNavItemListener?,
        ) {
            binding.filtersNavItem = itemData
            binding.clickListner = clickListner
            binding.position=adapterPosition


            // When You Using 'Binding Adapter' inside 'Recycler-View' must execute -> "binding.executePendingBindings()"
            binding.executePendingBindings()
        }


    }
}

/**
 * DiffUtil Class
 */

class FiltersNavbarDiffCallback : DiffUtil.ItemCallback<FiltersNavItemData>() {
    override fun areItemsTheSame(
        oldItem: FiltersNavItemData,
        newItem: FiltersNavItemData
    ): Boolean {
        return oldItem.uniqueId == newItem.uniqueId
    }

    override fun areContentsTheSame(
        oldItem: FiltersNavItemData,
        newItem: FiltersNavItemData
    ): Boolean {
        return oldItem == newItem
    }
}


/**
 * CLickListener class
 */

class FiltersNavItemListener(val clickListener: (filter: GPUImageFilter, position:Int) -> Unit) {

    fun onClick(navItem: FiltersNavItemData,position:Int) = clickListener(navItem.filter,position)

}