package com.example.gurukul_master.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gurukul_master.models.MyStaticClass
import com.example.gurukul_master.R
import com.example.gurukul_master.cards.*
import com.example.gurukul_master.databinding.ActivityTeacherHomeBinding
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class TeacherHome : AppCompatActivity() {

    var backPress = 1
    private lateinit var binding: ActivityTeacherHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_home)

        Picasso.get().load(MyStaticClass.currentOnlineTeacher?.picUrl)
            .into(binding.teacherHomeProfileImage)
        binding.teacherHomeName.text = MyStaticClass.currentOnlineTeacher?.name
        binding.teacherHomePhone.text = MyStaticClass.currentOnlineTeacher?.phone


        //cards click
        binding.lectures.setOnClickListener {
            MyStaticClass.actname = ""
            MyStaticClass.type = ""
            MyStaticClass.subjectname = ""
            MyStaticClass.classname = ""
            startActivity(Intent(this@TeacherHome, LecturesCard::class.java))
        }
        binding.homework.setOnClickListener {
            startActivity(
                Intent(
                    this@TeacherHome,
                    HomeworkCard::class.java
                )
            )
        }
        binding.notes.setOnClickListener {
            startActivity(
                Intent(
                    this@TeacherHome,
                    NotesCard::class.java
                )
            )
        }
        binding.classroom.setOnClickListener {
            startActivity(
                Intent(
                    this@TeacherHome,
                    ClassRoomCard::class.java
                )
            )
        }
        binding.questionPaper.setOnClickListener {
            startActivity(
                Intent(
                    this@TeacherHome,
                    ExamPaperCard::class.java
                )
            )
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPress > 1) {
                    finishAffinity()
                } else {
                    backPress += 1
                    Toast.makeText(
                        applicationContext,
                        " Press Back again to Exit ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        binding.teacherHomeProfileImage.setOnClickListener {
            startActivity(
                Intent(
                    this@TeacherHome,
                    UpdateActivity::class.java
                )
            )
        }
        binding.logoutBtn.setOnClickListener {
            Paper.book().destroy()
            MyStaticClass.currentOnlineTeacher = null
            val intent = Intent(this@TeacherHome, TeacherLoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

}