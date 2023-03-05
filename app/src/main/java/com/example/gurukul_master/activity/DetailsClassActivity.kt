package com.example.gurukul_master.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.models.Student
import com.example.gurukul_master.models.Subjects
import com.example.gurukul_master.R
import com.example.gurukul_master.adapters.DetailSubjectAdapter
import com.example.gurukul_master.adapters.DetailStudentAdapter
import com.example.gurukul_master.databinding.ActivityDetailsClassBinding
import com.example.gurukul_master.utils.hide
import com.example.gurukul_master.utils.show
import com.google.firebase.database.*


class DetailsClassActivity : AppCompatActivity() {
    private var databaseReference: DatabaseReference? = null
    private var databaseReferenceSubjects: DatabaseReference? = null
    private var databaseReferenceDetailsSubjects: DatabaseReference? = null
    private lateinit var binding: ActivityDetailsClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_class)
        binding.studentsTv.setOnClickListener {
            binding.studentsRV.show()
            binding.studentLayout.setBackgroundColor(
                resources.getColor(
                    R.color.border,
                    resources.newTheme()
                )
            )
            binding.studentsTv.setTextColor(resources.getColor(R.color.text, resources.newTheme()))
            binding.subjectsTV.setTextColor(
                resources.getColor(
                    R.color.border,
                    resources.newTheme()
                )
            )
            binding.subjectLayout.setBackgroundColor(
                resources.getColor(
                    R.color.text,
                    resources.newTheme()
                )
            )
            binding.subjectsRV.hide()
        }
        binding.subjectsTV.setOnClickListener {
            binding.subjectsRV.show()
            binding.subjectLayout.setBackgroundColor(
                resources.getColor(
                    R.color.border,
                    resources.newTheme()
                )
            )
            binding.subjectsTV.setTextColor(resources.getColor(R.color.text, resources.newTheme()))
            binding.studentsTv.setTextColor(
                resources.getColor(
                    R.color.border,
                    resources.newTheme()
                )
            )
            binding.studentLayout.setBackgroundColor(
                resources.getColor(
                    R.color.text,
                    resources.newTheme()
                )
            )
            binding.studentsRV.hide()
        }
        val studentArrayList = ArrayList<Student>()
        val subjects = ArrayList<String>()
        val subjectsArrayList = ArrayList<Subjects>()
        binding.classNumber.text = MyStaticClass.classname
        databaseReferenceDetailsSubjects =
            FirebaseDatabase.getInstance().reference.child("ALL_SUBJECTS")
        databaseReferenceSubjects =
            MyStaticClass.classname?.let {
                FirebaseDatabase.getInstance().reference.child("CLASSES")
                    .child(it).child("SUBJECTS")
            }
        databaseReferenceSubjects?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    subjects.add(snapshot.value.toString())
                }
                databaseReferenceDetailsSubjects?.addListenerForSingleValueEvent(object :
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
                            this@DetailsClassActivity,
                            subjectsArrayList,
                            "Classroom"
                        )
                        binding.subjectsRV.apply {
                            adapter = subjectAdapter
                            layoutManager = LinearLayoutManager(this@DetailsClassActivity)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        databaseReference =
            MyStaticClass.classname?.let {
                FirebaseDatabase.getInstance().reference.child("CLASSES")
                    .child(it).child("ROLLNO")
            }
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var student: Student
                if (dataSnapshot.childrenCount == 0L) {
                    binding.studentsRV.apply {
                        setPadding(250, 250, 250, 250)
                        background = AppCompatResources.getDrawable(
                            this@DetailsClassActivity,
                            R.drawable.no_available_student
                        )
                    }
                } else {
                    binding.studentsRV.background = null
                    for (snapshot in dataSnapshot.children) {
                        student = snapshot.getValue(Student::class.java)!!
                        studentArrayList.add(student)
                    }
                    val studentAdapter =
                        DetailStudentAdapter(this@DetailsClassActivity, studentArrayList)
                    binding.studentsRV.apply {
                        adapter = studentAdapter
                        layoutManager = LinearLayoutManager(this@DetailsClassActivity)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}