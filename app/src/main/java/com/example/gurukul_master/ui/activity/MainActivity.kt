package com.example.gurukul_master.ui.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.gurukul_master.R
import com.example.gurukul_master.data.models.MyStaticClass
import com.example.gurukul_master.data.models.Teachers
import com.google.firebase.database.*
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {
    var rocketAnimation: AnimationDrawable? = null
    var phoneNumber: String? = null
    var password: String? = null
    var rocketImage: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rocketImage = findViewById(R.id.imageView)
        rocketImage!!.setBackgroundResource(R.drawable.loading)
        if (rocketImage != null) {
            rocketAnimation = rocketImage!!.background as AnimationDrawable
            rocketAnimation?.start()
            Paper.init(this)
            phoneNumber = Paper.book().read(MyStaticClass.userPhoneKey)
            password = Paper.book().read(MyStaticClass.userPasswordKey)
            if (TextUtils.isEmpty(phoneNumber)) {
                Handler(Looper.getMainLooper()).postDelayed({
                    rocketAnimation?.stop()
                    val intent = Intent(this@MainActivity, TeacherLoginActivity::class.java)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@MainActivity,
                            rocketImage!!,
                            "splash_animation"
                        )
                    startActivity(intent, optionsCompat.toBundle())
                    finishAffinity()
                }, 7500)
            } else {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        val rootReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        rootReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (phoneNumber?.let {
                        dataSnapshot.child("TEACHERS").child(it).exists()
                    } == true) {
                    val teachers: Teachers =
                        dataSnapshot.child("TEACHERS").child(phoneNumber!!).getValue(
                            Teachers::class.java
                        )!!
                    if (teachers.password.equals(password)) {
                        MyStaticClass.currentOnlineTeacher = teachers
                        Paper.book().write(MyStaticClass.userPhoneKey, phoneNumber!!)
                        Paper.book().write(MyStaticClass.userPasswordKey, password!!)
                        startActivity(Intent(this@MainActivity, TeacherHome::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Password was changed!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        rocketAnimation?.stop()
                        val intent = Intent(this@MainActivity, TeacherLoginActivity::class.java)
                        val optionsCompat: ActivityOptionsCompat =
                            rocketImage?.let {
                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    this@MainActivity,
                                    it,
                                    "splash_animation"
                                )
                            }!!
                        startActivity(intent, optionsCompat.toBundle())
                        finishAffinity()
                    }, 6000)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}