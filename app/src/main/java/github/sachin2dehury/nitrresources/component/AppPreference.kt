package github.sachin2dehury.nitrresources.component

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager

object AppPreference {
    @SuppressLint("CommitPrefEdits")
    fun saveAppData(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString("Stream", AppCore.stream)
            if (AppCore.firebaseAuth.currentUser != null) {
                putString("Email", AppCore.firebaseAuth.currentUser!!.email!!)
            }
            apply()
        }
    }

    fun loadAppData(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).apply {
            AppCore.stream = getString("Stream", "Trash")!!
            if (AppCore.stream != "Trash") {
                AppCore.streamYrs =
                    AppCore.streamWiseYearList[AppCore.streamList.indexOf(AppCore.stream)]
            }
        }
    }
}