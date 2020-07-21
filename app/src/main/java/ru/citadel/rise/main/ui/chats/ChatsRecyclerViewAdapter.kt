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
class ChatsRecyclerViewAdapter(private val onCardClickListener: (ChatShortView) -> Unit)
    : RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder>() {

    private var values: List<ChatShortView> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.from.text = item.toName
        holder.lastMessage.text = item.lastMessage
        holder.itemView.setOnClickListener { onCardClickListener(item) }
    }

    override fun getItemCount(): Int = values.size

    internal fun setData(data: List<ChatShortView>){
        values = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ShapeableImageView = view.findViewById(R.id.chatImage)
        val from: TextView = view.findViewById(R.id.textFrom)
        val lastMessage: MaterialTextView = view.findViewById(R.id.textLastMes)
        val status: ShapeableImageView = view.findViewById(R.id.userStatus)

        init {
            if (Random.nextInt() % 2 == 0)
                image.setImageResource(R.drawable.im_robot_with_idea)
            else
                image.setImageResource(R.drawable.im_robot_manager)

            if(Random.nextInt() % 3 == 0)
                status.visibility = View.VISIBLE
            else
                status.visibility = View.INVISIBLE
        }

    }
}