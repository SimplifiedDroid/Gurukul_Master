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
import com.example.gurukul_master.databinding.ActivityHomeworkSubBinding
import com.example.gurukul_master.models.Documents
import com.example.gurukul_master.models.MyStaticClass
import com.google.firebase.database.*

class HomeworkSubActivity : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityHomeworkSubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@HomeworkSubActivity,
            R.layout.activity_homework__sub_
        )
        binding.homeworksubject.text = MyStaticClass.subjectname
        val documents = ArrayList<Documents>()
        val docs2 = ArrayList<Documents>()
        databaseReference =
            MyStaticClass.classname?.let {
                MyStaticClass.subjectname?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("HOMEWORK")
                        .child(it).child(it1)
                }
            }
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var docs: Documents
                if (dataSnapshot.childrenCount <= 0) {
                    binding.addHomeworkRecycler.setPadding(250, 250, 250, 250)
                    binding.addHomeworkRecycler.background = AppCompatResources.getDrawable(
                        this@HomeworkSubActivity,
                        R.drawable.no_available_student
                    )
                } else {
                    binding.addHomeworkRecycler.background = null
                    for (snapshot in dataSnapshot.children) {
                        docs = snapshot.getValue(Documents::class.java)!!
                        documents.add(docs)
                    }
                    for (i in documents.size - 1 downTo 0) {
                        docs2.add(documents[i])
                    }
                    val studentAdapter =
                        DocumentAdapter(this@HomeworkSubActivity, "HOMEWORK", docs2)
                    binding.addHomeworkRecycler.adapter = studentAdapter
                    binding.addHomeworkRecycler.layoutManager =
                        LinearLayoutManager(this@HomeworkSubActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.addHomework.setOnClickListener {
            MyStaticClass.type = "application/pdf"
            MyStaticClass.actname = "homework"
            startActivity(Intent(this@HomeworkSubActivity, AddDocumentActivity::class.java))
        }
    }
}