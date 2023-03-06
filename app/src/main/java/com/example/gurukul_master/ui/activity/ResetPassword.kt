package com.example.gurukul_master.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityResetPasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResetPassword : AppCompatActivity() {
   private var reference: DatabaseReference? = null
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        reference = FirebaseDatabase.getInstance().reference.child("TEACHERS")
        binding.passwordResetButton.setOnClickListener {
            if (binding.resetPasswordText.text.toString().isEmpty()) {
                binding.resetPasswordText.error = "Enter the Password"
            } else {
                if (binding.confirmResetPasswordText.text.toString().isEmpty()) {
                    binding.confirmResetPasswordText.error = "Enter Confirm Password"
                } else {
                    if (binding.resetPasswordText.text.toString() != binding.confirmResetPasswordText.text.toString()) {
                        binding.confirmResetPasswordText.error =
                            "Both the passwords did not matched"
                        binding.resetPasswordText.setText("")
                        binding.confirmResetPasswordText.setText("")
                    } else {
                        intent.getStringExtra("phoneNumber")
                            ?.let { it1 -> changePassword(it1) }
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ResetPassword, TeacherLoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    private fun changePassword(phoneNumber: String) {
        val map = HashMap<String, Any?>()
        map["password"] = binding.resetPasswordText.text.toString()
        reference!!.child(phoneNumber).updateChildren(map).addOnCompleteListener {
            Toast.makeText(
                this@ResetPassword,
                "Password Changed,Now You Can Login",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this@ResetPassword, TeacherLoginActivity::class.java))
            finish()
        }.addOnFailureListener { e ->
            Toast.makeText(
                this@ResetPassword,
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}