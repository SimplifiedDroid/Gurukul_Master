package com.example.gurukul_master.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.data.models.Teachers
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityRegisterBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    var imgUri: Uri? = null
    private var storageReference = FirebaseStorage.getInstance().getReference("TEACHERS")
    private var reference = FirebaseDatabase.getInstance().reference.child("TEACHERS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.profileAddLayout.setOnClickListener { openGallery() }
        binding.register.setOnClickListener {
            if (check(
                )
            ) {
                //if all are filled then here
                if (binding.phone.text.toString().length != 10) {
                    binding.phone.error = "ENTER CORRECT PHONE"
                } else {
                    if (binding.confirmPassword.text.toString() == binding.regPassword.text.toString()) {
                        binding.name.isEnabled = false
                        binding.phone.isEnabled = false
                        binding.confirmPassword.isEnabled = false
                        binding.regPassword.isEnabled = false
                        binding.profileAddLayout.isEnabled = false
                        checkAccount()
                    } else {
                        binding.regPassword.setText("")
                        binding.confirmPassword.setText("")
                        binding.confirmPassword.error = "BOTH PASSWORDS MUST BE SAME"
                    }
                }
            }
        }
    }

    private fun checkAccount() {
        val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (binding.phone.text.toString()
                        .let { dataSnapshot.child("TEACHERS").child(it).exists() }
                ) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Account Already Exists,Redirecting to Login Page",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, TeacherLoginActivity::class.java))
                } else {
                    if (imgUri == null) {
                        profilePicUrl =
                            "https://firebasestorage.googleapis.com/v0/b/gurukul-f0593.appspot.com/o/USER%2Fstudent.png?alt=media&token=b6196674-18d7-4899-9dab-a33eddab93ae"
                        addUserData()
                    } else {
                        savePicInDatabase()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun addUserData() {
        val teachers =
            Teachers(
                binding.name.text.toString(),
                binding.phone.text.toString(),
                profilePicUrl,
                binding.regPassword.text.toString()
            )
        binding.phone.text.toString().let { reference.child(it).setValue(teachers) }
        Toast.makeText(this, "NOW U CAN LOGIN", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@RegisterActivity, TeacherLoginActivity::class.java))
        finish()
    }

    private fun savePicInDatabase() {
        val storageReference: StorageReference =
            binding.phone.text.toString().let { storageReference.child(it) }
        imgUri?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {
                    storageReference.downloadUrl
                        .addOnSuccessListener { uri ->
                            profilePicUrl = uri.toString()
                            addUserData()
                        }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@RegisterActivity,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                uri?.let { galleryUri ->
                    imgUri = galleryUri
                    binding.profilePicAdd.setImageURI(galleryUri)
                }
            }
        }

    private fun openGallery() {
        ImagePicker.with(this)
            .crop()
            .cropOval()
            .maxResultSize(512,512,true)
            .provider(ImageProvider.BOTH)
            .createIntentFromDialog { launcher.launch(it) }
    }

    private fun check(
    ): Boolean {
        var result = false
        var n = 0
        if (binding.name.text.toString().isEmpty()) {
            binding.name.error = "PLEASE ENTER NAME"
            n = 1
        }
        if (binding.regPassword.text.toString().isEmpty()) {
            binding.regPassword.error = "PLEASE ENTER PASSWORD"
            n += 1
        }
        if (binding.phone.text.toString().isEmpty()) {
            binding.phone.error = "PLEASE ENTER NUMBER"
            n += 1
        }

        if (binding.confirmPassword.text.toString().isEmpty()) {
            binding.confirmPassword.error = "PLEASE ENTER PASSWORD AGAIN"
            n += 1
        }
        if (n == 0) {
            result = true
        }
        return result
    }

    companion object {
        var profilePicUrl: String? = null
    }
}