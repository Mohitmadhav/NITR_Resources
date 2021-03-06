package github.sachin2dehury.nitrresources.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.component.AppCore
import github.sachin2dehury.nitrresources.component.AppLogic
import github.sachin2dehury.nitrresources.component.AppNav
import github.sachin2dehury.nitrresources.fragment.ListFragment
import github.sachin2dehury.nitrresources.fragment.TabFragment
import github.sachin2dehury.nitrresources.viewholder.ListViewHolder
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.math.min

class ListAdapter(private val item: Int, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<ListViewHolder>(), Filterable {

    private val list = AppLogic.listSelector(item)
    private val map = AppLogic.mapSelector(item)
    private var listData = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.apply {
            name.text = listData[position]
            imageText.text = map[list.indexOf(listData[position])]
            imageText.visibility = View.VISIBLE
            if (item == AppCore.NO_LIST) {
                subject.visibility = View.VISIBLE
                size.visibility = View.VISIBLE
                name.setLines(1)
                size.text = "No Data Available!"
                subject.text = "No Data Available!"
            }
            setOnClickListener {
                val newItem = AppLogic.listPredictor(item, position)
                AppLogic.dataSetter(item, position)
                when (item) {
                    AppCore.YEAR_LIST -> AppNav.changeFragment(TabFragment(), fragmentManager)
                    else -> AppNav.changeFragment(ListFragment(newItem), fragmentManager)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (item == AppCore.YEAR_LIST) {
            return min(AppCore.streamYrs, listData.size)
        }
        return listData.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(value: CharSequence?): FilterResults {
                val search = value.toString().toLowerCase()
                val filterResults = FilterResults()
                filterResults.values = if (search.isEmpty()) {
                    list
                } else {
                    list.filter {
                        it.toLowerCase().contains(search)
                    }
                }
                return filterResults
            }

            override fun publishResults(value: CharSequence?, filterResults: FilterResults?) {
                listData = filterResults!!.values as List<String>
                notifyDataSetChanged()
            }
        }
    }
}