package uz.gita.bookreading_john.data.source.local.sharedpref

interface MySharedPref {
    var bookName: String
    var savedPage: Int
    var totalPage: Int
    var percentage: Int
    fun deleteCurrentBook()
}