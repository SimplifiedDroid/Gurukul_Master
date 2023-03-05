package com.example.gurukul_master.cards

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.activity.AddClass
import com.example.gurukul_master.adapters.ClassesAdapter
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityClassRoomCardBinding
import com.google.firebase.database.*

class ClassRoomCard : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityClassRoomCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_room_card)
        val myList = ArrayList<String>()
        databaseReference = FirebaseDatabase.getInstance().reference.child("CLASSES")
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.key?.let { myList.add(it) }
                }
                val classesAdapter = ClassesAdapter(this@ClassRoomCard, myList, "ClassRoom")
                binding.myclassHolder
                    .apply {
                        adapter = classesAdapter
                        layoutManager = LinearLayoutManager(this@ClassRoomCard)
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        binding.addClass.setOnClickListener {
            startActivity(
                Intent(
                    this@ClassRoomCard,
                    AddClass::class.java
                )
            )
        }
    }
}