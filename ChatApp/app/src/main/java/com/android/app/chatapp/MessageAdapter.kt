package com.android.app.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject

class MessageAdapter(inflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = inflater
    private val messages : ArrayList<JSONObject> = ArrayList()

    class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTxt = itemView.findViewById<TextView>(R.id.sentTxt)
    }

    class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTxt = itemView.findViewById<TextView>(R.id.nameTxt)
        val messageTxt = itemView.findViewById<TextView>(R.id.receivedTxt)

    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        try {
            if (message.getBoolean("isSent")){
                if (message.has("message")){
                    return TYPE_MESSAGE_SENT
                }
            }else {
                if (message.has("message")){
                    return TYPE_MESSAGE_RECEIVED
                }
            }
        }catch (e : JSONException){
            e.printStackTrace()
        }

        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View?

        return when (viewType) {
            TYPE_MESSAGE_SENT -> {
                view = inflater.inflate(R.layout.item_sent_message, parent, false)
                SentMessageHolder(view)
            }
            else -> {
                view = inflater.inflate(R.layout.item_received_message, parent, false)
                ReceivedMessageHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        try {
            if (message.getBoolean("isSent")){
                if (message.has("message")){
                    val viewHolder : SentMessageHolder = holder as SentMessageHolder
                    viewHolder.messageTxt.text = message.getString("message")
                }
            }else {
                if (message.has("message")){
                    val viewHolder : ReceivedMessageHolder = holder as ReceivedMessageHolder
                    viewHolder.nameTxt.text = message.getString("name")
                    viewHolder.messageTxt.text = message.getString("message")
                }
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    public fun addItem(item: JSONObject){
        messages.add(item)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_MESSAGE_SENT: Int = 0
        private const val TYPE_MESSAGE_RECEIVED: Int = 1
    }
}