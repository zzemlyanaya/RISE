package ru.citadel.rise.main.ui.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import ru.citadel.rise.data.model.Message


class MessageRecyclerAdapter(private val messages: List<Message>, private val cur_user_id: Int)
    : RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = messages[position]
        if(cur_user_id == item.fromm) {
            holder.bubble.setImageResource(R.drawable.cur_user_bubble)
            holder.layout.gravity = Gravity.END
        }
        else {
            holder.bubble.setImageResource(R.drawable.other_user_bubble)
            holder.layout.gravity = Gravity.START
        }
        holder.messText.text = item.text
//        val df = DateFormat()
//        DateFormat.format("dd.MM.yyyy hh:mm:ss a", Date())
        holder.messTime.text = item.time
    }


    override fun getItemCount(): Int = messages.size

    fun addItem(item: Message){
        (messages as ArrayList).add(item)
        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: LinearLayout = view.findViewById(R.id.messLayout)
        val bubble: AppCompatImageView = view.findViewById(R.id.messBubble)
        val messText: MaterialTextView = view.findViewById(R.id.messText)
        val messTime: MaterialTextView = view.findViewById(R.id.messTime)
    }
}