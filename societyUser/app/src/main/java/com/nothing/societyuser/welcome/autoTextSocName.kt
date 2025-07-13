package com.nothing.societyuser.welcome

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class AutoTextSocName(
    context: Context,
    resource: Int,
    objects: MutableList<String>,
    private val societies: HashMap<String, String>
) : ArrayAdapter<String>(context, resource, objects), Filterable {

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val suggestions: MutableList<Pair<String, String>> = mutableListOf()

                if (constraint != null) {
                    for (entry in societies) {
                        if (entry.value.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(Pair(entry.key, entry.value))
                        }
                    }
                }

                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    clear()
                    addAll(
                        (results.values as List<Pair<String, String>>)
                            .map { it.second }
                    )
                    notifyDataSetChanged()
                } else {
                    clear()
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItem(position: Int): String? {
        return super.getItem(position)
    }
}