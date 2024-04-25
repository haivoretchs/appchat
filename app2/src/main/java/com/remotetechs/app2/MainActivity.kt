package com.remotetechs.app2

import android.content.Context
import android.graphics.Color
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.remotetechs.app2.databinding.ActivityMainBinding
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object {
        const val SERVER_PORT = 8888
    }
    private lateinit var httpClient: HttpClient
    private lateinit var handler: android.os.Handler
    private var clientTextColor: Int = Color.GREEN
    private lateinit var thread: Thread
    private var socket: Socket?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        httpClient = HttpClient(this)
        thread=Thread()
        handler = android.os.Handler()
    }
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnConnectServer -> {
                removeAllViews()
                showMessage("Connecting to Server...", clientTextColor)
                thread = Thread(HttpClient(this))
                thread.start()
                hideConnectServerBtn()
            }
            R.id.send_data -> {
                val clientMessage = binding.edMessage.text.toString().trim()
                showMessage(clientMessage, Color.BLUE)
                if (::httpClient.isInitialized) {
                    sendMessage(clientMessage)
                }
            }
        }
    }
    private fun sendMessage(message: String) {
        Thread {
            try {
                val serverAddr = InetAddress.getByName(getLocalIpAddress(this))
                socket = Socket(serverAddr, SERVER_PORT)
                    val out = PrintWriter(
                        BufferedWriter(
                            OutputStreamWriter(socket!!.getOutputStream())
                        ), true
                    )
                    out.println(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
    private fun textView(message: String?, color: Int): TextView {
        val formattedMessage = message ?: getString(R.string.empty_message)
        val formattedTime = getTime()
        val formattedText = getString(R.string.text_view_format, formattedMessage, formattedTime)
        val tv = TextView(this)
        tv.setTextColor(color)
        tv.text = formattedText
        tv.textSize = 20f
        tv.setPadding(0, 5, 0, 0)
        return tv
    }
    private fun getTime(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }
    private fun hideConnectServerBtn() {
        handler.post { findViewById<View>(R.id.btnConnectServer).visibility = View.GONE }
    }

    fun showMessage(message: String, color: Int) {
        handler.post { binding.msgList.addView(textView(message, color)) }
    }

    private fun removeAllViews() {
        handler.post {  binding.msgList.removeAllViews() }
    }
    fun getLocalIpAddress(context: Context): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        val ipAddress: Int = wifiInfo.ipAddress

        // Chuyển đổi địa chỉ IP từ dạng số nguyên thành chuỗi
        val ipAddressString = String.format(
            "%d.%d.%d.%d",
            (ipAddress and 0xff),
            (ipAddress shr 8 and 0xff),
            (ipAddress shr 16 and 0xff),
            (ipAddress shr 24 and 0xff)
        )

        return ipAddressString
    }
}