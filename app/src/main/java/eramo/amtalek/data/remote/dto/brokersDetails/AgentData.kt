package eramo.amtalek.data.remote.dto.brokersDetails

import android.os.Parcel
import android.os.Parcelable

data class AgentData(
    val broker_type: String?,
    val description: String?,
    val email: String?,
    val id: Int?,
    val logo: String?,
    val name: String?,
    val phone: String?,
    val projects_count: Int?,
    val properties_count: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(broker_type)
        parcel.writeString(description)
        parcel.writeString(email)
        parcel.writeValue(id)
        parcel.writeString(logo)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeValue(projects_count)
        parcel.writeValue(properties_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AgentData> {
        override fun createFromParcel(parcel: Parcel): AgentData {
            return AgentData(parcel)
        }

        override fun newArray(size: Int): Array<AgentData?> {
            return arrayOfNulls(size)
        }
    }
}
