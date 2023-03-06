package com.example.gurukul_master.ui.activity.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gurukul_master.data.models.Documents
import com.example.gurukul_master.data.models.MyStaticClass
import com.example.gurukul_master.R
import com.example.gurukul_master.ui.activity.TeacherHome
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DocumentAdapter(
    var context: Context,
    private var activityName: String,
    documents: ArrayList<Documents>
) :
    RecyclerView.Adapter<DocumentAdapter.DocumentHolder?>() {
    private var documents: ArrayList<Documents>

    init {
        this.documents = documents
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.document_viewer, parent, false)
        return DocumentHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentHolder, position: Int) {
        holder.topic.text = documents[position].topic
        holder.date.text = documents[position].date
        holder.deleteBtn.setOnClickListener {
            val databaseReference: DatabaseReference =
                MyStaticClass.subjectname?.let { it1 ->
                    MyStaticClass.classname?.let { it2 ->
                        FirebaseDatabase.getInstance().reference.child(
                            activityName
                        ).child(it2).child(it1)
                    }
                }!!
            documents[position].time?.let { it1 -> databaseReference.child(it1).removeValue() }
            Toast.makeText(context, "File Removed From Database", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, TeacherHome::class.java))
        }
        when (activityName) {
            "LECTURE" -> {
                holder.documentImage.setImageResource(R.drawable.video)
            }
            "NOTES" -> {
                holder.documentImage.setImageResource(R.drawable.pdf)
            }
            "EXAM-PAPER" -> {
                holder.documentImage.setImageResource(R.drawable.pdf)
            }
            "HOMEWORK" -> {
                holder.documentImage.setImageResource(R.drawable.pdf)
            }
        }
    }


    inner class DocumentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deleteBtn: Button
        var topic: TextView
        var date: TextView
        var documentImage: ImageView

        init {
            deleteBtn = itemView.findViewById(R.id.document_delete_btn)
            topic = itemView.findViewById(R.id.document_topic)
            date = itemView.findViewById(R.id.document_date)
            documentImage = itemView.findViewById(R.id.document_image)
        }
    }

    override fun getItemCount(): Int {
        return documents.size
    }
}