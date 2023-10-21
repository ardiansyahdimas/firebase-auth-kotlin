package com.test.firebase_auth.ui.screen.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.test.firebase_auth.R
import com.test.firebase_auth.databinding.RvUserBinding
import com.test.firebase_auth.model.User
import java.util.Locale

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>(), Filterable {
    var listFiltered: ArrayList<User>  = java.util.ArrayList()
    private var listData = ArrayList<User>()

    fun setData(newListData: List<User>?) {
        if (newListData == null) return
        listData.clear()
        listFiltered.clear()
        listData.addAll(newListData.sortedBy { it.name })
        listFiltered.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_user, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RvUserBinding.bind(itemView)
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(data: User) {
            with(binding) {
                tvName.text  = data.name
                tvEmail.text = data.email
            }
        }
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    private val contactFilter: Filter = object : Filter() {
        @SuppressLint("DefaultLocale")
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<User> = java.util.ArrayList()
            if (constraint.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(listFiltered)
            } else {
                filteredList.clear()
                val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in listFiltered) {
                    if (item.name?.lowercase()?.contains(filterPattern) == true || item.email?.lowercase()?.contains(filterPattern) == true) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            listData.clear()
            listData.addAll(results.values as ArrayList<User>)
            notifyDataSetChanged()
        }
    }
}