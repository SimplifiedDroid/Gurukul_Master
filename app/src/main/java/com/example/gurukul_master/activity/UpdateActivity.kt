package com.example.gurukul_master.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.models.Teachers
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityUpdateBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class UpdateActivity : AppCompatActivity() {
    private var imgUri: Uri? = null
    private var storageReference: StorageReference? = null
    private var reference: DatabaseReference? = null
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update)
        binding.updateName.setText(MyStaticClass.currentOnlineTeacher?.name)
        binding.updatePassword.setText(MyStaticClass.currentOnlineTeacher?.password)
        binding.updateConfirmPassword.setText(MyStaticClass.currentOnlineTeacher?.password)
        Picasso.get().load(MyStaticClass.currentOnlineTeacher?.picUrl)
            .into(binding.updateProfilePicAdd)
        storageReference = FirebaseStorage.getInstance().reference
        reference = FirebaseDatabase.getInstance().reference
        binding.updateInfoBtn.setOnClickListener {

            if (check()) {
                if (binding.updatePassword.text.toString() == binding.updateConfirmPassword.text.toString()) {
                    binding.updateName.isEnabled = false
                    binding.updatePassword.isEnabled = false
                    binding.updateConfirmPassword.isEnabled = false
                    binding.updateProfileAddLayout.isEnabled = false
                    if (MyStaticClass.currentOnlineTeacher?.picUrl
                            .equals("https://firebasestorage.googleapis.com/v0/b/gurukul-f0593.appspot.com/o/USER%2Fstudent.png?alt=media&token=b6196674-18d7-4899-9dab-a33eddab93ae")
                    ) {
                        Toast.makeText(
                            this@UpdateActivity,
                            "Please Add Profile Picture",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.updateProfileAddLayout.isEnabled = true
                    } else {
                        savePicInDatabase()
                    }
                } else {
                    binding.updatePassword.setText("")
                    binding.updateConfirmPassword.setText("")
                    binding.updateConfirmPassword.error = "BOTH PASSWORDS MUST BE SAME"
                }
            }
        }
        binding.updateProfileAddLayout.setOnClickListener { openGallery() }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@UpdateActivity, TeacherHome::class.java))
                finishAffinity()
            }
        })
    }

    private fun savePicInDatabase() {
        val storageReference: StorageReference =
            MyStaticClass.currentOnlineTeacher?.phone?.let {
                storageReference?.child("TEACHERS")?.child(it)
            }!!
        if (imgUri == null) {
            profilePicUrl = MyStaticClass.currentOnlineTeacher!!.picUrl
            updateAccount()
        } else {
            storageReference.putFile(imgUri!!)
                .addOnSuccessListener {
                    storageReference.downloadUrl
                        .addOnSuccessListener { uri ->
                            profilePicUrl = uri.toString()
                            updateAccount()
                        }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@UpdateActivity,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun updateAccount() {
        val teachers = Teachers(
            binding.updateName.text.toString(),
            MyStaticClass.currentOnlineTeacher?.phone,
            profilePicUrl,
            binding.updatePassword.text.toString()
        )
        MyStaticClass.currentOnlineTeacher?.phone?.let {
            reference?.child("TEACHERS")?.child(it)
                ?.setValue(teachers)?.addOnCompleteListener {
                    Toast.makeText(
                        this@UpdateActivity,
                        "Account info Updated",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Paper.book().destroy()
                    MyStaticClass.currentOnlineTeacher = null
                    startActivity(Intent(this@UpdateActivity, TeacherLoginActivity::class.java))
                    finishAffinity()
                }?.addOnFailureListener { e ->
                    Toast.makeText(this@UpdateActivity, e.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UpdateActivity, TeacherHome::class.java))
                    finish()
                }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                uri?.let { galleryUri ->
                    imgUri = galleryUri
                    binding.updateProfilePicAdd.setImageURI(galleryUri)
                    MyStaticClass.currentOnlineTeacher?.picUrl = "okay"
                }
            }
        }

    private fun openGallery() {
        ImagePicker.with(this)
            .crop()
            .cropFreeStyle()
            .provider(ImageProvider.BOTH)
            .createIntentFromDialog { launcher.launch(it) }
    }

    private fun check(
    ): Boolean {
        var result = false
        var n = 0
        if (binding.updateName.text.toString().isEmpty()) {
            binding.updateName.error = "PLEASE ENTER NAME"
            n = 1
        }
        if (binding.updatePassword.text.toString().isEmpty()) {
            binding.updatePassword.error = "PLEASE ENTER PASSWORD"
            n += 1
        }
        if (binding.updateConfirmPassword.text.toString().isEmpty()) {
            binding.updateConfirmPassword.error = "PLEASE ENTER PASSWORD AGAIN"
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