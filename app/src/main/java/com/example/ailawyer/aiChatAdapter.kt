package com.example.ailawyer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val messageTime: TextView = itemView.findViewById(R.id.messageTime)
        //val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer) // Container for alignment
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT) {
            R.layout.item_message_sent
            // Use separate layouts
        } else {
            R.layout.item_message_received

        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text
        holder.messageTime.text = message.time

        // No need to set layout direction here
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}
