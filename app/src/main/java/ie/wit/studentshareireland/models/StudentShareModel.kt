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
    var image: Uri = Uri.EMPTY
) : Parcelable

