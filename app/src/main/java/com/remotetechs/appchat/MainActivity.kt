package com.remotetechs.appchat

import android.content.Context
import android.graphics.Color
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.remotetechs.appchat.databinding.ActivityMainBinding
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    companion object {
        const val SERVER_PORT = 8888
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var httpServer: HttpServer
    private lateinit var tempClientSocket: Socket
    private lateinit var serverThread: Thread
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serverThread = Thread()
        tempClientSocket=Socket()
        handler= Handler()
        httpServer = HttpServer(this)

    }

    private fun sendMessage(message: String) {
        try {
            Thread {
                Log.d("dsadsadada",HttpServer.clientIpAddress.toString())
                tempClientSocket = Socket(HttpServer.clientIpAddress, SERVER_PORT)
                var out: PrintWriter? = null
                try {
                    out = PrintWriter(
                        BufferedWriter(OutputStreamWriter(tempClientSocket.getOutputStream())),
                        true
                    )
                    out.println(message)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("AAAAAAAAAA",e.message.toString())
                } finally {
                    out?.close()
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("AAAAAAAAAA",e.message.toString())
        }
    }
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_start_server -> {
                removeAllViews()
                showMessage("Server Started.", Color.RED)
                serverThread = Thread(HttpServer(this))
                serverThread.start()
            }
            R.id.btn_send -> {
                val msg = binding.edMessage.text.toString().trim()
                showMessage("Server : $msg", Color.BLUE)
                sendMessage(msg)
            }
        }
    }

    private fun removeAllViews() {
        handler.post { binding.msgList.removeAllViews() }
    }
    fun showMessage(message: String, color: Int) {
        handler.post { binding.msgList.addView(textView(message, color)) }
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
    fun hideStartServerBtn() {
        handler.post { findViewById<View>(R.id.btn_start_server).visibility = View.GONE }
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