package com.example.myapplication.adapter// ModuleAdapter.kt
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.modle.TrainingModule

class ModuleAdapter(
    private val context: Context,
    private val modules: List<TrainingModule>,
    private val onItemClick: (TrainingModule) -> Unit
) : RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>() {

    private val prefs: SharedPreferences = context.getSharedPreferences("module_prefs", Context.MODE_PRIVATE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val module = modules[position]
        // Load status from SharedPreferences
        module.isCompleted = prefs.getBoolean("module_${module.id}", false)
        holder.title.text = module.title
        holder.description.text = module.description
        holder.status.text = if (module.isCompleted) "Completed" else "Pending"

        holder.itemView.setOnClickListener {
            onItemClick(module)
        }
    }

    override fun getItemCount(): Int = modules.size

    inner class ModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.moduleTitle)
        val description: TextView = itemView.findViewById(R.id.moduleDescription)
        val status: TextView = itemView.findViewById(R.id.moduleStatus)
    }
}
