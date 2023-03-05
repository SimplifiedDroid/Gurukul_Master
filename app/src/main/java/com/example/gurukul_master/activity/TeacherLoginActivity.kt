package com.example.gurukul_master.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.models.Teachers
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityTeacherLoginBinding
import com.google.firebase.database.*
import io.paperdb.Paper

class TeacherLoginActivity : AppCompatActivity() {
    private var rootReference: DatabaseReference? = null
    private lateinit var binding: ActivityTeacherLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_login)
        Paper.init(this)
        binding.notRegisteredText.setOnClickListener {
            startActivity(
                Intent(
                    this@TeacherLoginActivity,
                    RegisterActivity::class.java
                )
            )
        }
        rootReference = FirebaseDatabase.getInstance().reference.child("CLASSES")
        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this@TeacherLoginActivity, ForgetPassword::class.java)
            startActivity(intent)
        }
        binding.teacherLoginButton.setOnClickListener {
            if (binding.phoneNumber.text.toString().length == 10) {
                if (binding.password.text.toString().isNotEmpty()) {
                    binding.phoneNumber.isEnabled = false
                    binding.password.isEnabled = false
                    binding.teacherLoginButton.isEnabled = false
                    loginUser()
                } else {
                    binding.password.error = "Password can't be Null"
                }
            } else {
                binding.phoneNumber.error = "Please Enter Correct Phone Number"
            }
        }
    }

    private fun loginUser() {
        val rootReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        rootReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (binding.phoneNumber.text.toString()
                        .let { dataSnapshot.child("TEACHERS").child(it).exists() }
                ) {
                    val teachers: Teachers =
                        binding.phoneNumber.text.toString().let {
                            dataSnapshot.child("TEACHERS").child(it).getValue(
                                Teachers::class.java
                            )
                        }!!
                    if (teachers.password.equals(binding.password.text.toString())) {
                        MyStaticClass.currentOnlineTeacher = teachers
                        Paper.book()
                            .write(MyStaticClass.userPhoneKey, binding.phoneNumber.text.toString())
                        Paper.book()
                            .write(MyStaticClass.userPasswordKey, binding.password.text.toString())
                        startActivity(Intent(this@TeacherLoginActivity, TeacherHome::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            this@TeacherLoginActivity,
                            "Wrong Password !!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.phoneNumber.isEnabled = true
                        binding.password.isEnabled = true
                        binding.teacherLoginButton.isEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this@TeacherLoginActivity,
                        "Account with this ${binding.phoneNumber.text} Don't Exists",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.phoneNumber.isEnabled = true
                    binding.password.isEnabled = true
                    binding.teacherLoginButton.isEnabled = true
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }


}