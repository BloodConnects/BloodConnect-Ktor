package com.raktacare.module.user

import com.raktacare.module.location.Location
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.or

@Serializable
data class User(
    val uid: String = "",
    val fullName: String = "",
    val countryCode: String = "",
    val mobileNumber: String = "",
    val mailAddress: String = "",
    val bloodGroup: BloodGroup = BloodGroup.unknown,
    val gender: Gender = Gender.Other,
    val birthDate: Long = 0,
    val weight: Double = 0.0,
    val locationKey: String = "",
    val location: Location? = null,
    val profilePictureUrl: String = "",
    val deviceToken: String = "",
    val userToken: String = ""
) {


    @Serializable
    enum class Gender {
        Male, Female, Other
    }

    @Serializable
    enum class BloodGroup {
        oPositive, oNegative, aPositive, aNegative, bPositive, bNegative, abPositive, abNegative, unknown;

        override fun toString(): String {
            return when (this) {
                oPositive -> "O+"
                oNegative -> "O-"
                aPositive -> "A+"
                aNegative -> "A-"
                bPositive -> "B+"
                bNegative -> "B-"
                abPositive -> "AB+"
                abNegative -> "AB-"
                unknown -> "Unknown"
            }
        }

        fun getQuery(): Op<Boolean> {
            return when (this) {
                oPositive -> Users.bloodGroup eq oPositive or
                        (Users.bloodGroup eq oNegative)

                oNegative -> Users.bloodGroup eq oNegative

                aPositive -> Users.bloodGroup eq aPositive or
                        (Users.bloodGroup eq oPositive) or
                        (Users.bloodGroup eq aNegative) or
                        (Users.bloodGroup eq oNegative)

                aNegative -> Users.bloodGroup eq aNegative or
                        (Users.bloodGroup eq oNegative)

                bPositive -> Users.bloodGroup eq bPositive or
                        (Users.bloodGroup eq oPositive) or
                        (Users.bloodGroup eq bNegative) or
                        (Users.bloodGroup eq oNegative)

                bNegative -> Users.bloodGroup eq bNegative or
                        (Users.bloodGroup eq oNegative)

                abPositive -> Users.bloodGroup eq abPositive or
                        (Users.bloodGroup eq aPositive) or
                        (Users.bloodGroup eq bPositive) or
                        (Users.bloodGroup eq oPositive) or
                        (Users.bloodGroup eq abNegative) or
                        (Users.bloodGroup eq aNegative) or
                        (Users.bloodGroup eq bNegative) or
                        (Users.bloodGroup eq oNegative)

                abNegative -> Users.bloodGroup eq abNegative or
                        (Users.bloodGroup eq aNegative) or
                        (Users.bloodGroup eq bNegative) or
                        (Users.bloodGroup eq oNegative)

                unknown -> Users.bloodGroup eq unknown
            }
        }
    }

}