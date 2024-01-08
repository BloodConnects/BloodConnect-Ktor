package com.raktacare.module.user

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val uid = varchar("uid", 128)
    val fullName = varchar("full_name", 128)
    val countryCode = varchar("country_code", 5)
    val mobileNumber = varchar("mobile_number", 15)
    val mailAddress = varchar("mail_address", 128)
    val bloodGroup = enumeration("blood_group", User.BloodGroup::class)
    val gender = enumeration("gender", User.Gender::class)
    val profilePictureUrl = varchar("profile_picture_url", 512)
    val deviceToken = varchar("device_token", 512)
    val userToken = varchar("user_token", 320)
    override val primaryKey = PrimaryKey(uid)
}