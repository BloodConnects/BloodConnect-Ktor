package com.raktacare.module.user

import com.raktacare.ifNotEmpty
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
            it[locationKey] = user.locationKey
            it[profilePictureUrl] = user.profilePictureUrl
            it[deviceToken] = user.deviceToken
            it[userToken] = user.userToken
        }
        result.resultedValues?.singleOrNull()?.toUser()
    }

    override suspend fun updateUser(user: User) = dbQuery {
        Users.update({ Users.uid eq user.uid }) {
            user.fullName.ifNotEmpty { value -> it[fullName] = value }
            user.countryCode.ifNotEmpty { value -> it[countryCode] = value }
            user.mobileNumber.ifNotEmpty { value -> it[mobileNumber] = value }
            user.mailAddress.ifNotEmpty { value -> it[mailAddress] = value }
            user.profilePictureUrl.ifNotEmpty { value -> it[profilePictureUrl] = value }
            user.deviceToken.ifNotEmpty { value -> it[deviceToken] = value }
            user.userToken.ifNotEmpty { value -> it[userToken] = value }
            if (user.bloodGroup != User.BloodGroup.unknown) it[bloodGroup] = user.bloodGroup
            if (user.gender != User.Gender.Other) it[gender] = user.gender
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
        this[Users.uid],
        this[Users.fullName],
        this[Users.countryCode],
        this[Users.mobileNumber],
        this[Users.mailAddress],
        this[Users.bloodGroup],
        this[Users.gender],
        this[Users.birthDate],
        this[Users.weight],
        this[Users.locationKey],
        this[Users.profilePictureUrl],
        this[Users.deviceToken],
        this[Users.userToken]
    )

}