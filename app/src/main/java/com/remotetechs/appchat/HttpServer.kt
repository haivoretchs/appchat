package com.remotetechs.appchat
import android.util.Log
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class HttpServer(private val mainActivity: MainActivity):Runnable {
    companion object{
        var clientIpAddress :String?=null
        var clientPort=0
    }
    val TAG = "##HttpServer"
    private val port = 8888
    private var serverSocket: ServerSocket? = null
    private var socket: Socket? = null
    override fun run() {

        try {
            mainActivity.hideStartServerBtn()
            serverSocket = ServerSocket(port)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("AAAAAAAAAA",e.message.toString())
        }
        if (serverSocket!=null) {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    socket = serverSocket!!.accept()?:return
                    val commThread = CommunicationThread(mainActivity, socket!!)
                    clientIpAddress = socket!!.inetAddress.hostAddress
                    clientPort = socket!!.port
                    Thread(commThread).start()
                } catch (e: IOException) {
                    e.printStackTrace()

                }
            }
        }
    }

}