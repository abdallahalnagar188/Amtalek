package eramo.amtalek.data.remote.dto.brokersDetails

import android.os.Parcel
import android.os.Parcelable

data class Project(
    val agent_data: AgentData?,
    val bedrooms: String?,
    val city: String?,
    val country: String?,
    val created_at: String?,
    val delivery_date: String?,
    val description: String?,
    val facebook: String?,
    val google_plus: String?,
    val id: Int?,
    val image: String?,
    val listing_number: String?,
    val price_from: String?,
    val region: String?,
    val title: String?,
    val total_units: String?,
    val twitter: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(AgentData::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(agent_data, flags)
        parcel.writeString(bedrooms)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeString(created_at)
        parcel.writeString(delivery_date)
        parcel.writeString(description)
        parcel.writeString(facebook)
        parcel.writeString(google_plus)
        parcel.writeValue(id)
        parcel.writeString(image)
        parcel.writeString(listing_number)
        parcel.writeString(price_from)
        parcel.writeString(region)
        parcel.writeString(title)
        parcel.writeString(total_units)
        parcel.writeString(twitter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}
