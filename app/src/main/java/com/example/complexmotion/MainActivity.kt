package com.example.complexmotion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.complexmotion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        val names = mutableListOf<Name>()
        for (i in 1..40) {
            names.add(Name("Name $i"))
        }
        val adapter = NameAdapter()
        adapter.updateList(names)
        binding.recyclerView.adapter = adapter
        val listener = SwipeAndDragItemHelper(adapter)
        val itemTouchHelperAdapter = ItemTouchHelper(listener)
        itemTouchHelperAdapter.attachToRecyclerView(binding.recyclerView)

    }
}

data class Name(val text: String)
