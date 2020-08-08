package ru.citadel.rise.main.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import ru.citadel.rise.data.model.Project

class ProjectRecyclerViewAdapter(
    private val onCardClickListener: (Project) -> Unit,
    private val values: List<Project>
)
    : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_short, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.name
        holder.itemView.setOnClickListener { onCardClickListener(item) }
    }

    override fun getItemCount(): Int = values.size
//
//    fun setData(data: List<Project>){
//        values.clear()
//        values.addAll(data)
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: MaterialTextView = view.findViewById(R.id.projTitle)
    }
}