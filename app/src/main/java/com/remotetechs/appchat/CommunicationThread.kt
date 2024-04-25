package com.remotetechs.appchat

import android.graphics.Color
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

class CommunicationThread(private val activity: MainActivity, clientSocket: Socket) : Runnable {
    private var input: BufferedReader?=null

    init {
        try {
            input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        } catch (e: IOException) {
            e.printStackTrace()
                activity.showMessage("Error Connecting to Client!!", Color.RED)
        }
            activity.showMessage("Connected to Client!!", Color.DKGRAY)
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                var read = input?.readLine()
                if (null == read || "Disconnect" == read) {
                    val interrupted = Thread.interrupted()
                    read = "Client Disconnected: $interrupted"
                    activity.showMessage("Client : $read", Color.DKGRAY)
                    break
                }
                activity.showMessage("Client : $read", Color.DKGRAY)
            } catch (e: IOException) {
                e.printStackTrace()
                    activity.showMessage("errr : ${e.message.toString()}", Color.DKGRAY)
            }
        }
    }
}