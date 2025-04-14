package com.example.ailawyer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.dataclasses.Message
import com.example.ailawyer.databinding.ItemMessageSentBinding
import com.example.ailawyer.databinding.ItemMessageReceivedBinding


class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Define constants for view types
    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    // ViewHolder for sent messages
    class SentMessageViewHolder(val binding: ItemMessageSentBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder for received messages
    class ReceivedMessageViewHolder(val binding: ItemMessageReceivedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return messages.size  // Fix: return the correct count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]  // Fix: get message at correct position
        when (holder) {
            is SentMessageViewHolder -> {
                holder.binding.messageText.text = message.text
                holder.binding.messageTime.text = message.time
            }
            is ReceivedMessageViewHolder -> {
                holder.binding.messageText.text = message.text
                holder.binding.messageTime.text = message.time
            }
        }
    }
}
