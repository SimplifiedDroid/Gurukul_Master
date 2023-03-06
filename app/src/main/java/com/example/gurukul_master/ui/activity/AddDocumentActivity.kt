package com.example.gurukul_master.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.data.models.Documents
import com.example.gurukul_master.data.models.MyStaticClass
import com.example.gurukul_master.R
import com.example.gurukul_master.databinding.ActivityAddDocumentBinding
import com.example.gurukul_master.utils.hide
import com.example.gurukul_master.utils.show
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*


class AddDocumentActivity : AppCompatActivity() {
    private var imgUri: Uri? = null
    private var storageReference1: StorageReference? = null
    private var databaseReference: DatabaseReference? = null
    private var docUrl: String? = null
    private val myDate = System.currentTimeMillis().toString()
    private lateinit var binding: ActivityAddDocumentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_document)
        binding.addLogo.setOnClickListener {
            if (imgUri == null)
                openGallery()
        }
        binding.addDocs.setOnClickListener {
            if (imgUri == null) {
                binding.fileName.error = "Please add file"
            } else {
                if (binding.topicName.text.toString().length <= 3) {
                    binding.topicName.error = "Please add topic"
                } else {
                    myFunction()
                }
            }
        }
    }

    private fun myFunction() {
        when (MyStaticClass.actname) {
            "exampaper" -> {
                binding.addLogo.setImageResource(R.drawable.pdf)
                binding.fileName.hide()
                binding.addLogo.isEnabled = false
                binding.topicName.isEnabled = false
                storageReference1 =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseStorage.getInstance().reference.child("EXAMPAPER")
                                .child(it).child(it1)
                        }
                    }
                databaseReference =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("EXAMPAPER")
                                .child(it).child(it1)
                        }
                    }
                saveDocInDatabase()
            }
            "notes" -> {
                binding.addLogo.setImageResource(R.drawable.pdf)
                binding.fileName.hide()
                binding.addLogo.isEnabled = false
                binding.topicName.isEnabled = false
                storageReference1 =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseStorage.getInstance().reference.child("NOTES")
                                .child(it).child(it1)
                        }
                    }
                databaseReference =
                    MyStaticClass.subjectname?.let {
                        MyStaticClass.classname?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("NOTES")
                                .child(it1).child(it)
                        }
                    }
                saveDocInDatabase()
            }
            "homework" -> {
                binding.addLogo.setImageResource(R.drawable.pdf)
                binding.fileName.hide()
                binding.addLogo.isEnabled = false
                binding.topicName.isEnabled = false
                storageReference1 =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseStorage.getInstance().reference.child("HOMEWORK")
                                .child(it).child(it1)
                        }
                    }
                databaseReference =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("HOMEWORK")
                                .child(it).child(it1)
                        }
                    }
                saveDocInDatabase()
            }
            "lecture" -> {
                binding.addLogo.setImageResource(R.drawable.video)
                binding.fileName.hide()
                binding.addLogo.isEnabled = false
                binding.topicName.isEnabled = false
                storageReference1 =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseStorage.getInstance().reference.child("LECTURE")
                                .child(it).child(it1)
                        }
                    }
                databaseReference =
                    MyStaticClass.classname?.let {
                        MyStaticClass.subjectname?.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("LECTURE")
                                .child(it).child(it1)
                        }
                    }
                saveDocInDatabase()
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                uri?.let { galleryUri ->
                    imgUri = galleryUri
                    val file = File(URLDecoder.decode(galleryUri.toString(), "UTF-8"))
                    val filename: String = file.name
                    binding.fileName.text = filename.removePrefix("primary:").lowercase(Locale.ROOT)
                    binding.addLogo.setImageResource(R.drawable.pdf)
                    binding.changeFile.show()
                }
            }
        }

    private fun openGallery() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = MyStaticClass.type + "/*"
        launcher.launch(galleryIntent)
    }


    private fun saveDocInDatabase() {
        val storageReference: StorageReference = storageReference1?.child(myDate)!!
        imgUri?.let {
            storageReference.putFile(it)
                .addOnSuccessListener {
                    storageReference.downloadUrl
                        .addOnSuccessListener { p1 ->
                            docUrl = p1.toString()
                            addUserData()
                        }
                }.addOnFailureListener { e ->
                    Toast.makeText(this@AddDocumentActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun addUserData() {
        val docs = Documents(binding.topicName.text.toString(), date, myDate)
        myDate.let { databaseReference?.child(it)?.setValue(docs) }
        Toast.makeText(this, "File Uploaded", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@AddDocumentActivity, TeacherHome::class.java))
        finish()
    }

    private val date: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val date = Date()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return simpleDateFormat.format(date)
        }

}