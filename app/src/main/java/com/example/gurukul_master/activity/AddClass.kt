package com.example.gurukul_master.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurukul_master.models.Subjects
import com.example.gurukul_master.R
import com.example.gurukul_master.adapters.SubjectsAdapter
import com.example.gurukul_master.cards.ClassRoomCard
import com.example.gurukul_master.databinding.ActivityAddClassBinding
import com.example.gurukul_master.utils.hide
import com.example.gurukul_master.utils.show
import com.google.firebase.database.*

class AddClass : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
   private var databaseReference2: DatabaseReference? = null
    var subjectsAdapter: SubjectsAdapter? = null
    var s: String? = null
   private var chkSub: ArrayList<Subjects>? = null
    private lateinit var binding: ActivityAddClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add__class)

        binding.addClassBtn.isEnabled = false
        binding.resetBtn.isEnabled = false
        binding.cancelBtn.isEnabled = false
        binding.classCheckText.isEnabled = true
        binding.checkButton.isEnabled = true
        databaseReference2 = FirebaseDatabase.getInstance().reference.child("CLASSES")
        binding.checkButton.setOnClickListener {
            s = binding.classCheckText.text.toString()
            if (s!!.isEmpty()) {
                Toast.makeText(this@AddClass, "please enter class", Toast.LENGTH_SHORT).show()
            } else {
                databaseReference2?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child("CLASS$s").exists()) {
                            Toast.makeText(this@AddClass, "exist", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.addClassBtn.isEnabled = true
                            binding.resetBtn.isEnabled = true
                            binding.cancelBtn.isEnabled = true
                            binding.classCheckText.isEnabled = false
                            binding.checkButton.isEnabled = false
                            binding.ADDSUBJECTS.apply {
                                isEnabled = true
                                show()
                            }
                            loadSubjects()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
        }
        binding.resetBtn.setOnClickListener {
            binding.addClassBtn.isEnabled = false
            binding.resetBtn.isEnabled = false
            binding.cancelBtn.isEnabled = false
            binding.classCheckText.isEnabled = true
            binding.checkButton.isEnabled = true
            binding.ADDSUBJECTS.apply {
                isEnabled = false
                hide()
            }
            binding.classCheckText.setText("")
        }
        binding.cancelBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@AddClass,
                    ClassRoomCard::class.java
                )
            )
        }
        binding.addClassBtn.setOnClickListener {
            getSubject()
            if (chkSub!!.size == 0) {
                Toast.makeText(
                    this@AddClass,
                    "please select at least one subject",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                createClass()
            }
        }
    }

    private fun createClass() {
        val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val classData: HashMap<String, Any> = HashMap()
        for (i in 0 until chkSub!!.size) {
            classData[java.lang.String.valueOf(chkSub!![i].s_NAME)] =
                chkSub!![i].s_CODE!!
        }
        rootRef.child("CLASSES").child("CLASS$s").child("SUBJECTS").updateChildren(classData)
        Toast.makeText(this@AddClass, "Class $s created ", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@AddClass, TeacherHome::class.java))
        finishAffinity()
    }

    private fun getSubject() {
        chkSub = ArrayList()
        for (i in 0 until subjectsAdapter!!.itemCount) {
            if (subjectsAdapter!!.getItem(i).isChk) {
                chkSub!!.add(subjectsAdapter!!.getItem(i))
            }
        }
    }

    private fun loadSubjects() {
        val  myList = ArrayList<Subjects>()

        databaseReference = FirebaseDatabase.getInstance().reference.child("ALL_SUBJECTS")
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    myList.add(Subjects(
                        s_NAME = snapshot.child("S_NAME").value.toString(),
                        s_CODE = snapshot.child("S_CODE").value.toString(),
                        s_PIC = snapshot.child("S_PIC").value.toString()
                    ))
                }
                subjectsAdapter = SubjectsAdapter(applicationContext, myList)
                binding.ADDSUBJECTS.apply {
                    layoutManager = LinearLayoutManager(this@AddClass)
                    adapter = subjectsAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}