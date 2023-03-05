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
import com.example.gurukul_master.databinding.ActivityNotesSubBinding
import com.example.gurukul_master.models.Documents
import com.example.gurukul_master.models.MyStaticClass
import com.google.firebase.database.*


class NotesSubActivity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityNotesSubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes__sub_)
        binding.notesubjects.text = MyStaticClass.subjectname
        val documents = ArrayList<Documents>()
        val docs2 = ArrayList<Documents>()
        databaseReference =
            MyStaticClass.classname?.let {
                MyStaticClass.subjectname?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("NOTES")
                        .child(it).child(it1)
                }
            }
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var docs: Documents
                if (dataSnapshot.childrenCount == 0L) {
                    binding.addNotesRecycler.setPadding(250, 250, 250, 250)
                    binding.addNotesRecycler.background = AppCompatResources.getDrawable(
                        this@NotesSubActivity,
                        R.drawable.no_available_student
                    )
                } else {
                    binding.addNotesRecycler.background = null
                    for (snapshot in dataSnapshot.children) {
                        docs = snapshot.getValue(Documents::class.java)!!
                        documents.add(docs)
                    }
                    for (i in documents.indices.reversed()) {
                        docs2.add(documents[i])
                    }
                    val studentAdapter = DocumentAdapter(
                        this@NotesSubActivity, "NOTES",
                        docs2
                    )
                    binding.addNotesRecycler.adapter = studentAdapter
                    binding.addNotesRecycler.layoutManager =
                        LinearLayoutManager(this@NotesSubActivity)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        binding.addNotes.setOnClickListener {
            MyStaticClass.type = "application/pdf"
            MyStaticClass.actname = "notes"
            startActivity(Intent(this@NotesSubActivity, AddDocumentActivity::class.java))
        }
    }
}