package eramo.amtalek.data.remote.dto.adons


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("monthly_price")
    var monthlyPrice: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("quantity")
    var quantity: Int?,
    @SerializedName("yearly_price")
    var yearlyPrice: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(monthlyPrice)
        parcel.writeString(name)
        parcel.writeValue(quantity)
        parcel.writeString(yearlyPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}


