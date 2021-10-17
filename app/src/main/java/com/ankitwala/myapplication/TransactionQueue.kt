package com.ankitwala.myapplication


import androidx.collection.CircularArray
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

object TransactionQueue {

    private val arrayQueue = ConcurrentLinkedQueue<Transaction>()

    fun add(transaction: Transaction){
        if(arrayQueue.size==5){
            arrayQueue.remove()
        }
        arrayQueue.add(transaction)
    }

    fun getAll(): List<Transaction>{
        return arrayQueue.toList()
    }

    fun clearAll(){
        arrayQueue.clear()
    }

    fun convertRawTransactionToTransaction(rawTransaction: RawTransaction, btcUSDRate: Double): Transaction{
        var satoshiValue = 0L
        for(out in rawTransaction.x.out){
            satoshiValue += out.value
        }
        val btcValue = satoshiValue.toDouble() * 0.00000001
        val usdValue = btcValue*btcUSDRate

        return Transaction(rawTransaction.x.hash, convertSecsTOIST(rawTransaction.x.time), usdValue)
    }

    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss z")

    fun convertSecsTOIST(time: Long): String{
        return dateFormat.format(Date(time*1000))
    }

}