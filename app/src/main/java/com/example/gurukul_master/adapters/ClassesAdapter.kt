package com.example.gurukul_master.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.R
import com.example.gurukul_master.activity.*
import com.example.gurukul_master.adapters.ClassesAdapter.ClassHolder

class ClassesAdapter(var context: Context, var name: ArrayList<String>, var activity: String) :
    RecyclerView.Adapter<ClassHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_classes, parent, false)
        return ClassHolder(view)
    }

    override fun onBindViewHolder(holder: ClassHolder, position: Int) {
        holder.className.text = name[position]
        holder.classLayout.setOnClickListener {
            when (activity) {
                "Lectures" -> {
                    val intent = Intent(context, LectureDetailActivity::class.java)
                    MyStaticClass.classname = name[position]
                    context.startActivity(intent)
                }
                "ClassRoom" -> {
                    val intent = Intent(context, DetailsClassActivity::class.java)
                    MyStaticClass.classname = name[position]
                    context.startActivity(intent)
                }
                "Notes" -> {
                    val intent = Intent(context, NotesDetailActivity::class.java)
                    MyStaticClass.classname = name[position]
                    context.startActivity(intent)
                }
                "ExamPaper" -> {
                    val intent = Intent(context, ExamPaperDetailActivity::class.java)
                    MyStaticClass.classname = name[position]
                    context.startActivity(intent)
                }
                "Homework" -> {
                    val intent = Intent(context, HomeworkDetailsActivity::class.java)
                    MyStaticClass.classname = name[position]
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class ClassHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var className: TextView
        var classLayout: LinearLayout

        init {
            className = itemView.findViewById(R.id.my_classes_container)
            classLayout = itemView.findViewById(R.id.class_inside_layout)
        }
    }
}