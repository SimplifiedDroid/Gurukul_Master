package com.example.gurukul_master.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.models.Subjects
import com.example.gurukul_master.R
import com.example.gurukul_master.adapters.DetailSubjectAdapter
import com.example.gurukul_master.databinding.ActivityLectureDetailAcitivityBinding
import com.google.firebase.database.*

class LectureDetailActivity : AppCompatActivity() {
    var reference: DatabaseReference? = null
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityLectureDetailAcitivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lecture_detail_acitivity)
        val subjects = ArrayList<String>()
        val subjectsArrayList = ArrayList<Subjects>()
        binding.classNumberLecture.text = MyStaticClass.classname.toString()
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("ALL_SUBJECTS")
        reference =
            FirebaseDatabase.getInstance().reference.child("CLASSES")
                .child(MyStaticClass.classname.toString())
                .child("SUBJECTS")
        reference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {

                    subjects.add(snapshot.value.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        databaseReference?.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                subjects.forEach {
                    dataSnapshot.child(it).let { sub ->
                        subjectsArrayList.add(
                            Subjects(
                                s_NAME = sub.child("S_NAME").value.toString(),
                                s_CODE = sub.child("S_CODE").value.toString(),
                                s_PIC = sub.child("S_PIC").value.toString()
                            )
                        )

                    }
                }

                val subjectAdapter = DetailSubjectAdapter(
                    this@LectureDetailActivity,
                    subjectsArrayList,
                    "Lectures"
                )
                binding.subjectsRecyclerviewLecture.apply {
                    adapter = subjectAdapter
                    layoutManager = LinearLayoutManager(this@LectureDetailActivity)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}