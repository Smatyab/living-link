package com.nothing.societyuser.Adapter

// CustomEditableSpinnerAdapter.kt
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class CustomEditableSpinnerAdapter(
    context: Context,
    resource: Int,
    private val objects: MutableList<String>
) : ArrayAdapter<String>(context, resource, objects) {

    private val filter = CustomFilter()

    override fun getFilter(): Filter {
        return filter
    }

    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint == null || constraint.isEmpty()) {
                filterResults.values = objects
                filterResults.count = objects.size
            } else {
                val filteredList = mutableListOf<String>()
                for (item in objects) {
                    if (item.contains(constraint, ignoreCase = true)) {
                        filteredList.add(item)
                    }
                }
                filterResults.values = filteredList
                filterResults.count = filteredList.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            if (results != null && results.count > 0) {
                val filteredList = results.values as MutableList<String>
                addAll(filteredList)
                notifyDataSetChanged()
            }
        }
    }
}
