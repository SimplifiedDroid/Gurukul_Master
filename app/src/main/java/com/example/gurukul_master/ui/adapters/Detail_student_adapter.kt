package com.example.gurukul_master.ui.activity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gurukul_master.data.models.Student
import com.example.gurukul_master.R
import com.squareup.picasso.Picasso


class DetailStudentAdapter(var context: Context, std: ArrayList<Student>) :
    RecyclerView.Adapter<DetailStudentAdapter.DetailStudentHolder?>() {
    var students: ArrayList<Student>

    init {
        students = std
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailStudentHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.class_student_row, parent, false)
        return DetailStudentHolder(view)
    }

    override fun onBindViewHolder(holder: DetailStudentHolder, position: Int) {
        Picasso.get().load(students[position].picUrl).into(holder.studPic)
        holder.studPhone.text = students[position].pHONE
        holder.studRoll.text = students[position].rollNo
        holder.studName.text = students[position].nAME
    }


    inner class DetailStudentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var studPic: ImageView
        var studName: TextView
        var studRoll: TextView
        var studPhone: TextView

        init {
            studPic = itemView.findViewById(R.id.details_student_iamge)
            studName = itemView.findViewById(R.id.details_student_name)
            studPhone = itemView.findViewById(R.id.details_student_phone)
            studRoll = itemView.findViewById(R.id.details_student_rollno)
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }
}