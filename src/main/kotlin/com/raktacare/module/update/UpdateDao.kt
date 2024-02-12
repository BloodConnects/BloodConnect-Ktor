package com.raktacare.module.update

import com.raktacare.plugins.dbQuery
import com.raktacare.util.generateRandomStringKey
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UpdateDao {

    suspend fun pushUpdateKey(): String {
        val key = generateRandomStringKey()
        if (checkUpdateKey(key)) {
            return pushUpdateKey()
        }
        return key
    }

    private suspend fun checkUpdateKey(updateKey: String) = dbQuery {
        !Updates.select { Updates.updateKey eq updateKey }.empty()
    }

    suspend fun addUpdate(update: Update) = dbQuery {
        val result = Updates.insert {
            it[updateKey] = update.updateKey
            it[title] = update.title
            it[description] = update.description
            it[updateDate] = update.updateDate
            it[updateImage] = update.updateImage
            it[referenceKey] = update.referenceKey
            it[updateType] = update.updateType
            it[userUid] = update.userUid
        }
        result.resultedValues?.singleOrNull()?.toUpdate()
    }

    suspend fun updateUpdate(update: Update) = dbQuery {
        Updates.update({ Updates.updateKey eq update.updateKey }) {
            it[title] = update.title
            it[description] = update.description
            it[updateDate] = update.updateDate
            it[updateImage] = update.updateImage
            it[referenceKey] = update.referenceKey
            it[updateType] = update.updateType
        } > 0
    }

    suspend fun deleteUpdate(updateKey: String) = dbQuery {
        Updates.deleteWhere { Updates.updateKey eq updateKey } > 0
    }

    suspend fun getUpdates() = dbQuery {
        Updates.selectAll().map { it.toUpdate() }
    }

    suspend fun getUpdateByKey(updateKey: String) = dbQuery {
        Updates.select { Updates.updateKey eq updateKey }.mapNotNull { it.toUpdate() }.singleOrNull()
    }

    suspend fun getUpdatesByUserUid(userUid: String) = dbQuery {
        Updates.select { Updates.userUid eq userUid }.map { it.toUpdate() }
    }

    suspend fun getUpdatesByUpdateType(uid: String, updateType: Update.UpdateType) = dbQuery {
        Updates.select { Updates.userUid eq uid and (Updates.updateType eq updateType) }.map { it.toUpdate() }
    }

    private fun ResultRow.toUpdate() = Update(
        updateKey = this[Updates.updateKey],
        userUid = this[Updates.userUid],
        title = this[Updates.title],
        description = this[Updates.description],
        updateDate = this[Updates.updateDate],
        updateImage = this[Updates.updateImage],
        referenceKey = this[Updates.referenceKey],
        updateType = this[Updates.updateType]
    )

}
