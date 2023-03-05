package com.example.gurukul_master.sub_activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.R
import com.example.gurukul_master.activity.AddDocumentActivity
import com.example.gurukul_master.adapters.DocumentAdapter
import com.example.gurukul_master.databinding.ActivityLectureSubAcitivityBinding
import com.example.gurukul_master.models.Documents
import com.example.gurukul_master.models.MyStaticClass
import com.google.firebase.database.*

class LectureSubActivity : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityLectureSubAcitivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@LectureSubActivity,
            R.layout.activity_lecture__sub__acitivity
        )
        binding.lecturesubject.text = MyStaticClass.subjectname
        val documents = ArrayList<Documents>()
        val docs2 = ArrayList<Documents>()
        databaseReference =
            MyStaticClass.subjectname?.let {
                MyStaticClass.classname?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("LECTURE")
                        .child(it1).child(it)
                }
            }
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var docs: Documents
                if (dataSnapshot.childrenCount <= 0) {
                    binding.addLectureRecycler.setPadding(250, 250, 250, 250)
                    binding.addLectureRecycler.background = AppCompatResources.getDrawable(
                        this@LectureSubActivity,
                        R.drawable.no_available_student
                    )
                } else {
                    binding.addLectureRecycler.background = null
                    for (snapshot in dataSnapshot.children) {
                        docs = snapshot.getValue(Documents::class.java)!!
                        documents.add(docs)
                    }
                    for (i in documents.indices.reversed()) {
                        docs2.add(documents[i])
                    }
                    val studentAdapter =
                        DocumentAdapter(this@LectureSubActivity, "LECTURE", docs2)
                    binding.addLectureRecycler.adapter = studentAdapter
                    binding.addLectureRecycler.layoutManager =
                        LinearLayoutManager(this@LectureSubActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.addLecture.setOnClickListener {
            MyStaticClass.type = "video"
            MyStaticClass.actname = "lecture"
            startActivity(Intent(this@LectureSubActivity, AddDocumentActivity::class.java))
        }
    }
}