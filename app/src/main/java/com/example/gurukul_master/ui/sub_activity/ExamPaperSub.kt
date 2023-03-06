package com.example.gurukul_master.ui.activity.sub_activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.R
import com.example.gurukul_master.ui.activity.AddDocumentActivity
import com.example.gurukul_master.ui.activity.adapters.DocumentAdapter
import com.example.gurukul_master.databinding.ActivityExampaperSubBinding
import com.example.gurukul_master.data.models.Documents
import com.example.gurukul_master.data.models.MyStaticClass
import com.google.firebase.database.*


class ExamPaperSub : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityExampaperSubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exampaper__sub)
        binding.exampapersubject.text = MyStaticClass.subjectname
        val documents = ArrayList<Documents>()
        val docs2 = ArrayList<Documents>()
        databaseReference =
            MyStaticClass.classname?.let {
                MyStaticClass.subjectname?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("EXAMPAPER")
                        .child(it).child(it1)
                }
            }
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var docs: Documents
                if (dataSnapshot.childrenCount == 0L) {
                    binding.addExampaperRecycler.setPadding(250, 250, 250, 250)
                    binding.addExampaperRecycler.background = AppCompatResources.getDrawable(
                        this@ExamPaperSub,
                        R.drawable.no_available_student
                    )
                } else {
                    binding.addExampaperRecycler.background = null
                    for (snapshot in dataSnapshot.children) {
                        docs = snapshot.getValue(Documents::class.java)!!
                        documents.add(docs)
                    }
                    for (i in documents.indices.reversed()) {
                        docs2.add(documents[i])
                    }
                    val studentAdapter = DocumentAdapter(this@ExamPaperSub, "EXAM-PAPER", docs2)
                    binding.addExampaperRecycler.adapter = studentAdapter
                    binding.addExampaperRecycler.layoutManager =
                        LinearLayoutManager(this@ExamPaperSub)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        binding.addExampaper.setOnClickListener {
            MyStaticClass.type = "application/pdf"
            MyStaticClass.actname = "exampaper"
            startActivity(Intent(this@ExamPaperSub, AddDocumentActivity::class.java))
        }
    }
}