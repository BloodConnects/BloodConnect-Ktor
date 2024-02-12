package com.raktacare.module.update

import org.jetbrains.exposed.sql.Table

object Updates:Table() {
    val updateKey = varchar("update_key", 128)
    val userUid = varchar("user_uid", 128)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val updateDate = long("update_date")
    val updateImage = varchar("update_image", 128)
    val referenceKey = varchar("reference_key", 128)
    val updateType = enumeration("update_type", Update.UpdateType::class)
    override val primaryKey = PrimaryKey(updateKey)
}