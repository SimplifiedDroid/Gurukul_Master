package com.example.gurukul_master.ui.activity.cards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.R
import com.example.gurukul_master.ui.adapters.ClassesAdapter
import com.example.gurukul_master.databinding.ActivityLecturesCardBinding
import com.google.firebase.database.*

class LecturesCard : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityLecturesCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lectures__card)
        val myList = ArrayList<String>()
        databaseReference = FirebaseDatabase.getInstance().reference.child("CLASSES")
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.key?.let { myList.add(it) }
                }
                val classesAdapter = ClassesAdapter(this@LecturesCard, myList, "Lectures")
                binding.myclassHolderLecture.apply {
                    adapter = classesAdapter
                    layoutManager = LinearLayoutManager(this@LecturesCard)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}