package com.example.gurukul_master.ui.activity.cards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.R
import com.example.gurukul_master.ui.adapters.ClassesAdapter
import com.example.gurukul_master.databinding.ActivityHomeworkCardBinding
import com.google.firebase.database.*

class HomeworkCard : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    private lateinit var binding: ActivityHomeworkCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_homework__card)
        val myList = ArrayList<String>()
        databaseReference = FirebaseDatabase.getInstance().reference.child("CLASSES")
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.key?.let { myList.add(it) }
                }
                val classesAdapter = ClassesAdapter(this@HomeworkCard, myList, "Homework")
                binding.myclassHolderHomework.apply {
                    adapter = classesAdapter
                    layoutManager = LinearLayoutManager(this@HomeworkCard)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}