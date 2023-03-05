package com.example.gurukul_master.cards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.R
import com.example.gurukul_master.adapters.ClassesAdapter
import com.example.gurukul_master.databinding.ActivityNotesCardBinding
import com.google.firebase.database.*


class NotesCard : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityNotesCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes__card)
        val myList = ArrayList<String>()
        databaseReference = FirebaseDatabase.getInstance().reference.child("CLASSES")
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.key?.let { myList.add(it) }
                }
                val classesAdapter = ClassesAdapter(this@NotesCard, myList, "Notes")
                binding.myclassHolderNotes.apply {
                    adapter = classesAdapter
                    layoutManager = LinearLayoutManager(this@NotesCard)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}