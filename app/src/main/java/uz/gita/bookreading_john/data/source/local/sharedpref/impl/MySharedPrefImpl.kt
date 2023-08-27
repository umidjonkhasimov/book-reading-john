package uz.gita.bookreading_john.data.source.local.sharedpref.impl

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.bookreading_john.data.source.local.sharedpref.MySharedPref
import javax.inject.Inject

class MySharedPrefImpl @Inject constructor(
    @ApplicationContext context: Context
) : MySharedPref {
    private var pref: SharedPreferences

    init {
        pref = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)
    }

    companion object {
        private const val MY_PREFS = "my_prefs"
        private const val BOOK_NAME = "book_name"
        private const val SAVED_PAGE = "saved_page"
        private const val TOTAL_PAGE = "total_page"
        private const val PERCENTAGE = "percentage"
    }

    override var bookName: String
        set(value) = pref.edit().putString(BOOK_NAME, value).apply()
        get() = pref.getString(BOOK_NAME, "").toString()

    override var savedPage: Int
        set(value) = pref.edit().putInt(SAVED_PAGE, value).apply()
        get() = pref.getInt(SAVED_PAGE, 0)

    override var totalPage: Int
        set(value) = pref.edit().putInt(TOTAL_PAGE, value).apply()
        get() = pref.getInt(TOTAL_PAGE, 0)

    override var percentage: Int
        set(value) = pref.edit().putInt(PERCENTAGE, value).apply()
        get() = pref.getInt(PERCENTAGE, 0)

    override fun deleteCurrentBook() {
        bookName = ""
        savedPage = 0
        totalPage = 0
        percentage = 0
    }
}