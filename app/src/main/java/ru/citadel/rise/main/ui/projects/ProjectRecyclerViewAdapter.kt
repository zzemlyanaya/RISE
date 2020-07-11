package ru.citadel.rise.main.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import ru.citadel.rise.model.Project

/**
 * [RecyclerView.Adapter] that can display a [Project].
 * TODO: Replace the implementation with code for your data type.
 */
class ProjectRecyclerViewAdapter(
    private val values: List<Project>
) : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.name
        holder.description.text = item.description
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: MaterialTextView = view.findViewById(R.id.projTitle)
        val description: MaterialTextView = view.findViewById(R.id.projShortDesr)

    }
}