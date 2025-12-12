package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ModuleAdapter
import com.example.myapplication.modle.TrainingModule

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var modules: MutableList<TrainingModule>
    private lateinit var filteredModules: MutableList<TrainingModule>
    private lateinit var adapter: ModuleAdapter
    private lateinit var prefs: SharedPreferences

    private lateinit var btnAll: Button
    private lateinit var btnCompleted: Button
    private lateinit var btnPending: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.homeToolbar)
        setSupportActionBar(toolbar)

        prefs = getSharedPreferences("module_prefs", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAll = findViewById(R.id.btnAll)
        btnCompleted = findViewById(R.id.btnCompleted)
        btnPending = findViewById(R.id.btnPending)

        // Sample data
        modules = mutableListOf(
            TrainingModule(1, "Kotlin Basics", "Learn the basics of Kotlin", false),
            TrainingModule(2, "Android Layouts", "Understand XML layouts", false),
            TrainingModule(3, "RecyclerView", "Implement RecyclerView", false)
        )

        filteredModules = modules.toMutableList()
        adapter = ModuleAdapter(this, filteredModules) { module ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("moduleId", module.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // Button click listeners
        btnAll.setOnClickListener { filterModules("all") }
        btnCompleted.setOnClickListener { filterModules("completed") }
        btnPending.setOnClickListener { filterModules("pending") }
    }

    override fun onResume() {
        super.onResume()
        // Refresh modules from SharedPreferences
        modules.forEach { it.isCompleted = prefs.getBoolean("module_${it.id}", false) }
        filterModules(currentFilter) // Reapply current filter
    }

    private var currentFilter = "all"

    private fun filterModules(filter: String) {
        currentFilter = filter
        filteredModules.clear()
        when (filter) {
            "all" -> filteredModules.addAll(modules)
            "completed" -> filteredModules.addAll(modules.filter { it.isCompleted })
            "pending" -> filteredModules.addAll(modules.filter { !it.isCompleted })
        }
        adapter.notifyDataSetChanged()
    }
}
