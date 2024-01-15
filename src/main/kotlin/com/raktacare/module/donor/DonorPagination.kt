package com.raktacare.module.donor

import com.raktacare.module.BasePagination
import com.raktacare.module.location.Location
import com.raktacare.module.user.User
import kotlinx.serialization.Serializable

@Serializable
data class DonorPagination(
    val bloodGroup: User.BloodGroup? = null,
    val location: Location? = null,
) : BasePagination()