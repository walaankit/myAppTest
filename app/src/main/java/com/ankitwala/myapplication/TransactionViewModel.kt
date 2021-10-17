package com.ankitwala.myapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class TransactionViewModel : ViewModel() {

    val transactionStatusLiveData: MutableLiveData<String> = MutableLiveData()

    val transactionLiveData: MutableLiveData<List<Transaction>> = MutableLiveData()

    private val gson = Gson()

    private var webSocket: WebSocket? = null

    private var btcUsd: Double = 0.0

    fun initWebSocket(){

        val usdRequest = Request.Builder().url(Constants.BTCUSD_PATH).build()
                val usdJson = OkHttpClient().newCall(usdRequest).enqueue(
                    object: Callback{
                        override fun onFailure(call: Call, e: IOException) {
                            transactionStatusLiveData.postValue("Btc to USD failed...")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            btcUsd = getUsdRatio(response.body?.string()!!)

                            transactionStatusLiveData.postValue("Initialising...")
                            val request = Request.Builder().url(Constants.WS_PATH).build()

                            webSocket = OkHttpClient().newWebSocket(request, socketListener)
                        }

                    }
                )






    }
    private fun getUsdRatio(usdJson: String): Double{
        val btc2Usd = gson.fromJson(usdJson, BTC2USD::class.java)
        return btc2Usd.USD.last
    }

    private val socketListener = object : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            transactionStatusLiveData.postValue("Opened...Subscribing")

            webSocket.send(
                "{\n" +
                        "  \"op\": \"unconfirmed_sub\"\n" +
                        "}"
            )
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            transactionStatusLiveData.postValue("Closing Socket...")

        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            transactionStatusLiveData.postValue("Closed...")

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            transactionStatusLiveData.postValue("Something went wrong...")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            transactionStatusLiveData.postValue("Subscribed...Receiving")
            val rawTransaction = gson.fromJson(text,RawTransaction::class.java)
            val transaction = TransactionQueue.convertRawTransactionToTransaction(
                rawTransaction, btcUsd
            )
            if(transaction.valueInUsd>100.0) {
                TransactionQueue.add(
                    transaction
                )
                transactionLiveData.postValue(
                    TransactionQueue.getAll()
                )
            }



        }
    }

    private val NORMAL_CLOSURE_STATUS = 1000

    override fun onCleared() {
        super.onCleared()
        webSocket?.send(
            "{\n" +
                    "  \"op\": \"unconfirmed_unsub\"\n" +
                    "}"
        )
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Activity closed")
    }

    fun clearData(){
        TransactionQueue.clearAll()
    }
}