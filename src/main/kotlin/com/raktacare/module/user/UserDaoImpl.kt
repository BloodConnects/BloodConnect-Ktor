package com.raktacare.module.user

import com.raktacare.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {

    override suspend fun getUserByUID(uid: String) = dbQuery {
        Users.select { Users.uid eq uid }.singleOrNull()?.toUser()
    }

    override suspend fun addUser(user: User) = dbQuery {
        val result = Users.insert {
            it[uid] = user.uid
            it[fullName] = user.fullName
            it[countryCode] = user.countryCode
            it[mobileNumber] = user.mobileNumber
            it[mailAddress] = user.mailAddress
            it[bloodGroup] = user.bloodGroup
            it[gender] = user.gender
            it[birthDate] = user.birthDate
            it[weight] = user.weight
            it[height] = user.height
            it[locationKey] = user.locationKey
            it[profilePictureUrl] = user.profilePictureUrl
            it[deviceToken] = user.deviceToken
            it[userToken] = user.userToken
        }
        result.resultedValues?.singleOrNull()?.toUser()
    }

    override suspend fun updateUser(user: User) = dbQuery {
        Users.update({ Users.uid eq user.uid }) {
            it[fullName] = user.fullName
            it[countryCode] = user.countryCode
            it[mobileNumber] = user.mobileNumber
            it[mailAddress] = user.mailAddress
            it[bloodGroup] = user.bloodGroup
            it[gender] = user.gender
            it[birthDate] = user.birthDate
            it[weight] = user.weight
            it[height] = user.height
            it[locationKey] = user.locationKey
            it[profilePictureUrl] = user.profilePictureUrl
            it[deviceToken] = user.deviceToken
            it[userToken] = user.userToken
        } > 0
    }

    override suspend fun deleteUser(uid: String) = dbQuery {
        Users.deleteWhere { Users.uid eq uid } > 0
    }

    override suspend fun getUsers() = dbQuery {
        Users.selectAll().map { it.toUser() }
    }

    override suspend fun getUserByMobileNumber(mobileNumber: String, countryCode: String) = dbQuery {
        Users.select { Users.mobileNumber eq mobileNumber and (Users.countryCode eq countryCode) }.singleOrNull()
            ?.toUser()
    }

    override suspend fun checkMobileNumberExists(mobileNumber: String, countryCode: String) = dbQuery {
        Users.select { Users.mobileNumber eq mobileNumber and (Users.countryCode eq countryCode) }.count() > 0
    }

    override suspend fun getUserByBloodGroup(bloodGroup: User.BloodGroup) = dbQuery {
        Users.select { Users.bloodGroup eq bloodGroup }.map { it.toUser() }
    }

    override suspend fun getUserByBloodRequest(bloodGroup: User.BloodGroup) = dbQuery {
        Users.select { bloodGroup.getQuery() }.map { it.toUser() }
    }

    private fun ResultRow.toUser() = User(
        uid = this[Users.uid],
        fullName = this[Users.fullName],
        countryCode = this[Users.countryCode],
        mobileNumber = this[Users.mobileNumber],
        mailAddress = this[Users.mailAddress],
        bloodGroup = this[Users.bloodGroup],
        gender = this[Users.gender],
        birthDate = this[Users.birthDate],
        weight = this[Users.weight],
        height = this[Users.height],
        locationKey = this[Users.locationKey],
        profilePictureUrl = this[Users.profilePictureUrl],
        deviceToken = this[Users.deviceToken],
        userToken = this[Users.userToken]
    )

}