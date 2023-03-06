package com.example.gurukul_master.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityForgetPasswordBinding
import com.google.firebase.database.*

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_forget_password)

       binding.check.setOnClickListener {
            if (binding.phoneNum.text.toString().isEmpty()) {
                binding.phoneNum.error = "Enter The Registered Number"
            }
            if (binding.phoneNum.text.toString().length < 10) {
                binding.phoneNum.error = "Enter the Full Indian Number"
            }
            if (binding.phoneNum.text.toString().length == 10) {
                binding.check.isEnabled = false
                binding.phoneNum.isEnabled = false
                checkNumber()
            }
        }
    }

    private fun checkNumber() {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("TEACHERS").child(binding.phoneNum.text.toString()).exists()) {
                    val intent = Intent(this@ForgetPassword, ResetPassword::class.java)
                    intent.putExtra("phoneNumber", binding.phoneNum.text.toString())
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@ForgetPassword,
                        "Account with ${binding.phoneNum.text} Doesn't exists",
                        Toast.LENGTH_SHORT
                    ).show()
                 binding.check.isEnabled = true
                  binding.phoneNum.apply {
                      isEnabled = true
                      setText("")
                  }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}