package uz.gita.bookreading_john.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun toasting(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun logging(msg: String) {
    Log.d("GGG", msg)
}