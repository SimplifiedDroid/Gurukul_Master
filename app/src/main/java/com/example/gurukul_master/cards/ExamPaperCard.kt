package com.example.gurukul_master.cards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.R
import com.example.gurukul_master.adapters.ClassesAdapter
import com.example.gurukul_master.databinding.ActivityExampaperCardBinding
import com.google.firebase.database.*

class ExamPaperCard : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityExampaperCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exampaper__card)
        val myList = ArrayList<String>()
        databaseReference = FirebaseDatabase.getInstance().reference.child("CLASSES")
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.key?.let { myList.add(it) }
                }
                val classesAdapter = ClassesAdapter(this@ExamPaperCard, myList, "ExamPaper")
                binding.myclassHolderExampaper.apply {
                    adapter = classesAdapter
                    layoutManager = LinearLayoutManager(this@ExamPaperCard)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}