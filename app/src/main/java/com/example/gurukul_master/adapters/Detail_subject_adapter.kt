package com.example.gurukul_master.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.models.Subjects
import com.example.gurukul_master.R
import com.example.gurukul_master.sub_activity.ExamPaperSub
import com.example.gurukul_master.sub_activity.HomeworkSubActivity
import com.example.gurukul_master.sub_activity.LectureSubActivity
import com.example.gurukul_master.sub_activity.NotesSubActivity
import com.squareup.picasso.Picasso

class DetailSubjectAdapter(var context: Context, subs: ArrayList<Subjects>, act: String) :
    RecyclerView.Adapter<DetailSubjectAdapter.DetailStudentHolder?>() {
    private var actName: String
    var subjects: ArrayList<Subjects>

    init {
        subjects = subs
        actName = act
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailStudentHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.class_subject_row, parent, false)
        return DetailStudentHolder(view)
    }

    override fun onBindViewHolder(holder: DetailStudentHolder, position: Int) {
        Picasso.get().load(subjects[position].s_PIC).into(holder.subjectsImage)
        holder.subName.text = subjects[position].s_NAME
        holder.subCode.text = subjects[position].s_CODE
        if (actName == "Homework") {
            holder.subLayout.setOnClickListener {
                MyStaticClass.subjectname = subjects[position].s_NAME
                context.startActivity(Intent(context, HomeworkSubActivity::class.java))
            }
        }
        if (actName == "Notes") {
            holder.subLayout.setOnClickListener {
                MyStaticClass.subjectname = subjects[position].s_NAME
                context.startActivity(Intent(context, NotesSubActivity::class.java))
            }
        }
        if (actName == "Lectures") {
            holder.subLayout.setOnClickListener {
                MyStaticClass.subjectname = subjects[position].s_NAME
                context.startActivity(Intent(context, LectureSubActivity::class.java))
            }
        }
        if (actName == "ExamPaper") {
            holder.subLayout.setOnClickListener {
                MyStaticClass.subjectname = subjects[position].s_NAME
                context.startActivity(Intent(context, ExamPaperSub::class.java))
            }
        }
    }


    inner class DetailStudentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subjectsImage: ImageView
        var subName: TextView
        var subCode: TextView
        var subLayout: LinearLayout

        init {
            subCode = itemView.findViewById(R.id.subject_row_subjectcode)
            subName = itemView.findViewById(R.id.subject_row_subjectname)
            subjectsImage = itemView.findViewById(R.id.subject_row_subjectimage)
            subLayout = itemView.findViewById(R.id.subjects_holder_layout)
        }
    }

    override fun getItemCount(): Int {
        return subjects.size
    }
}