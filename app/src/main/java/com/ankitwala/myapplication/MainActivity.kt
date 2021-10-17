package com.ankitwala.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankitwala.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val transactionViewModel : TransactionViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val adapter : TransactionAdapter by lazy {
        TransactionAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transRV.adapter = adapter
        binding.transRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.clearButton.setOnClickListener {
            transactionViewModel.clearData()
        }

        transactionViewModel.transactionLiveData.observe(this, transactionListObserver)
        transactionViewModel.transactionStatusLiveData.observe(this, statusObserver)

        transactionViewModel.initWebSocket()
    }

    val statusObserver = Observer<String>{
        binding.statusTextView.text = it
    }

    val transactionListObserver = Observer<List<Transaction>>{
        adapter.submitList(it)
    }
}