package uz.gita.bookreading_john.data.model

data class CategoryData(
    val id: String = "",
    val title: String = "",
    val books: List<BookData>
) {
    fun toTabItem(): CategoryItemData {
        return CategoryItemData(title = title, checked = false)
    }
}