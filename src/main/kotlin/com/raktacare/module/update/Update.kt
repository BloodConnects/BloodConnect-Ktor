package com.raktacare.module.update

import kotlinx.serialization.Serializable

@Serializable
data class Update(
    val updateKey: String = "",
    val userUid: String = "",
    val title: String = "",
    val description: String = "",
    val updateDate: Long = 0,
    val updateImage: String = "",
    val referenceKey: String = "",
    val updateType: UpdateType = UpdateType.REQUEST,
) {
    @Serializable
    enum class UpdateType {
        REQUEST, ARTICAL, EVENT
    }
}