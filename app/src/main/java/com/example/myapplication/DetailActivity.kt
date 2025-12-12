package com.example.myapplication

// DetailActivity.kt
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.modle.TrainingModule

class DetailActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var module: TrainingModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        val moduleId = intent.getIntExtra("moduleId", -1)
        if (moduleId == -1) finish()

        prefs = getSharedPreferences("module_prefs", Context.MODE_PRIVATE)

        // For simplicity, same data as HomeActivity
        val modules = listOf(
            TrainingModule(1, "Task 1", "task 1 des", false),
            TrainingModule(2, "Task 2", "task 2 des", false),
            TrainingModule(3, "Task 3", "task 3 des", false)
        )

        module = modules.first { it.id == moduleId }
        module.isCompleted = prefs.getBoolean("module_${module.id}", false)

        val titleText: TextView = findViewById(R.id.detailTitle)
        val descText: TextView = findViewById(R.id.detailDescription)
        val statusText: TextView = findViewById(R.id.detailStatus)
        val button: Button = findViewById(R.id.toggleButton)

        titleText.text = module.title
        descText.text = module.description
        statusText.text = if (module.isCompleted) "Completed" else "Pending"
        updateButtonText(button)

        button.setOnClickListener {
            module.isCompleted = !module.isCompleted
            statusText.text = if (module.isCompleted) "Completed" else "Pending"

            prefs.edit().putBoolean("module_${module.id}", module.isCompleted).apply()
            updateButtonText(button)
        }
    }

    private fun updateButtonText(button: Button) {
        button.text = if (module.isCompleted) "Mark as Pending" else "Mark as Completed"
    }
}
