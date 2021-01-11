package com.android.app.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import javax.net.ssl.SSLContext

class ChatActivity : AppCompatActivity(), TextWatcher {

    private lateinit var name: String;
    private lateinit var webSocket: WebSocket
    private lateinit var messageEdit: EditText
    private lateinit var sendBtn: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        name = intent.getStringExtra("name").toString()
        Log.d("DEBUGS", name)

        initiateSocketConnection()

    }

    private fun initiateSocketConnection() {
        val client = OkHttpClient()
        val request = Request.Builder().url(SERVER_PATH).build()
        webSocket = client.newWebSocket(request, SocketListener())


    }

    private inner class SocketListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            runOnUiThread {

                try {
                    val jsonObject = JSONObject(text)
                    jsonObject.put("isSent", false)
                    adapter.addItem(jsonObject)

                    recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                }catch (e: JSONException){
                    e.printStackTrace()
                }

            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            runOnUiThread {
                Toast.makeText(this@ChatActivity,
                    "Socket Connection Success",
                    Toast.LENGTH_SHORT).show()

                initializeView()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)

            runOnUiThread {
                Log.d("DEBUGS", t.message.toString())
                Toast.makeText(this@ChatActivity,
                        t.message,
                        Toast.LENGTH_LONG).show()

                initializeView()
            }
        }
    }

    private fun initializeView() {

        Log.d("DEBUGSA", name)

        messageEdit = findViewById(R.id.messageEdit)
        sendBtn = findViewById(R.id.sendBtn)
        recyclerView = findViewById(R.id.recyclerView)

        messageEdit.addTextChangedListener(this)
        sendBtn.setOnClickListener {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("name",name)
                jsonObject.put("message",messageEdit.text.toString().trim())

                webSocket.send(jsonObject.toString())


                jsonObject.put("isSent", true)
                adapter.addItem(jsonObject)

                resetMessage()
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }

        adapter = MessageAdapter(layoutInflater)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(s: Editable) {

        val string : String = s.toString().trim()
        Log.d("DEBUGS", "Syakala")
        Log.d("DEBUGS", string)
        if (string.isEmpty()){
            resetMessage()
        }else {
            sendBtn.visibility = View.VISIBLE
        }

    }

    private fun resetMessage() {
        messageEdit.removeTextChangedListener(this)
        messageEdit.setText("")
        sendBtn.visibility = View.INVISIBLE

        messageEdit.addTextChangedListener(this)

    }

    companion object {
        private const val SERVER_PATH = "IP WEBSOCKET"

    }
}