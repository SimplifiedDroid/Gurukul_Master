package com.example.gurukul_master.data.models

object MyStaticClass {
    const val userPhoneKey = "userphone"
    const val userPasswordKey = "userpassword"

    @JvmField
    var currentOnlineTeacher: Teachers? = null

    @JvmField
    var classname: String? = null

    @JvmField
    var subjectname: String? = null

    @JvmField
    var type: String? = null

    @JvmField
    var actname: String? = null
}