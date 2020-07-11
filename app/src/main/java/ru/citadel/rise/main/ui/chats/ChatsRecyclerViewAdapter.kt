package ru.citadel.rise.main.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import kotlin.random.Random


/**
 * [RecyclerView.Adapter] that can display a [ChatShortView].
 * TODO: Replace the implementation with code for your data type.
 */
class ChatsRecyclerViewAdapter(
    private val values: List<ChatShortView>
) : RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.from.text = item.from
        holder.time.text = item.lastMessageTime
        holder.lastMessage.text = item.lastMessage
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ShapeableImageView = view.findViewById(R.id.chatImage)
        val from: TextView = view.findViewById(R.id.textFrom)
        val time: MaterialTextView = view.findViewById(R.id.textTime)
        val lastMessage: MaterialTextView = view.findViewById(R.id.textLastMes)

        init {
            if (Random.nextInt() % 2 == 0)
                image.setImageResource(R.drawable.im_robot_with_idea)
            else
                image.setImageResource(R.drawable.im_robot_manager)
        }

    }
}