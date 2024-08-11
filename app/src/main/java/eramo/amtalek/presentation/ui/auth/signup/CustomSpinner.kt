import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.widget.AppCompatSpinner
import android.util.AttributeSet

class CustomSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr) {

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        // Save additional state here if necessary
        return if (superState != null) {
            SavedState(superState, selectedItemPosition)
        } else {
            superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            setSelection(state.selectedItemPosition)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        val selectedItemPosition: Int

        constructor(superState: Parcelable, selectedItemPosition: Int) : super(superState) {
            this.selectedItemPosition = selectedItemPosition
        }

        private constructor(parcel: Parcel) : super(parcel) {
            selectedItemPosition = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeInt(selectedItemPosition)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}
