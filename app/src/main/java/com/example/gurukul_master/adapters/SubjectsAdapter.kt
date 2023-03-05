package com.example.gurukul_master.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.gurukul_master.models.Subjects
import com.example.gurukul_master.R
import com.squareup.picasso.Picasso

class SubjectsAdapter(var context: Context, subjects: ArrayList<Subjects>) :
    RecyclerView.Adapter<SubjectsAdapter.SubjectsHolder?>() {
    var subs: ArrayList<Subjects>

    init {
        subs = subjects
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.subjects_row, parent, false)
        return SubjectsHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectsHolder, position: Int) {
        holder.subjectName.text = subs[position].s_NAME
        holder.subCode.text = subs[position].s_CODE
        Picasso.get().load(subs[position].s_PIC).into(holder.subjectImage)
        holder.subjectLayout.setOnClickListener {
            holder.selectedSubject.isChecked = !holder.selectedSubject.isChecked
        }
    }


    fun getItem(position: Int): Subjects {
        return subs[position]
    }

    inner class SubjectsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subjectName: TextView
        var selectedSubject: CheckBox
        var subjectImage: ImageView
        var subjectLayout: ConstraintLayout
        var subCode: TextView

        init {
            subjectName = itemView.findViewById(R.id.name_of_sub)
            subjectImage = itemView.findViewById(R.id.image_of_sub)
            subjectLayout = itemView.findViewById(R.id.subjects_raw_linear)
            selectedSubject = itemView.findViewById(R.id.box_of_sub)
            subCode = itemView.findViewById(R.id.code_of_sub)
            selectedSubject.setOnCheckedChangeListener { _, isChecked ->
                subs[adapterPosition].isChk = isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return subs.size
    }
}