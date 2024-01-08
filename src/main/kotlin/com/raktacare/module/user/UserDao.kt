package com.raktacare.module.user

interface UserDao {
    suspend fun getUserByUID(uid: String): User?
    suspend fun addUser(user: User): User?
    suspend fun updateUser(user: User): Boolean
    suspend fun deleteUser(uid: String): Boolean
    suspend fun getUsers(): List<User>
    suspend fun getUserByMobileNumber(mobileNumber: String, countryCode: String): User?
    suspend fun checkMobileNumberExists(mobileNumber: String, countryCode: String): Boolean
    suspend fun getUserByBloodGroup(bloodGroup: User.BloodGroup): List<User>
    suspend fun getUserByBloodRequest(bloodGroup: User.BloodGroup): List<User>
}