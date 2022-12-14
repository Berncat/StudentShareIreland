package ie.wit.studentshareireland.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentShareModel(
    var id: Long = 0,
    var street: String = "",
    var cost: String = "",
    var details: String = "",
    var phone: String = "",
    var image: Uri = Uri.EMPTY,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var userId: Long = 0,
) : Parcelable

@Parcelize
data class Coordinates(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var radius: Double = 0.0
) : Parcelable

@Parcelize
data class InstitutionModel(
    var title: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0
) : Parcelable {
    override fun toString(): String {
        return title
    }
}

@Parcelize
data class UserModel(
    var id: Long = 0,
    var firstName: String = "",
    var surname: String = "",
    var email: String = "",
    var password: String = "",
    var active: Boolean = false,
    var studentShares: MutableList<StudentShareModel> = mutableListOf()
) : Parcelable

