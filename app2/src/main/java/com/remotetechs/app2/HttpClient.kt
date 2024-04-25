package com.remotetechs.app2

import android.graphics.Color
import android.util.Log
import com.remotetechs.app2.MainActivity.Companion.SERVER_PORT
import kotlinx.coroutines.Runnable
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException

class HttpClient(private val activity: MainActivity) : Runnable {
    private var socket: Socket? = null
    private var input: BufferedReader? = null

    override fun run() {
        try {
            val serverAddr = InetAddress.getByName(activity.getLocalIpAddress(activity))
            socket = Socket(serverAddr, SERVER_PORT)
            while (!Thread.currentThread().isInterrupted) {
                input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                var message = input!!.readLine()
                if (null == message || "Disconnect" == message) {
                    val interrupted = Thread.interrupted()
                    message = "Server Disconnected: $interrupted"
                    activity.showMessage(message, Color.RED)
                    break
                }
                activity.showMessage("Server: $message", Color.RED)
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}