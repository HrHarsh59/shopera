package project.demo.shopera.model

data class ShopData(
    val shopName: String? = null,
    val shopLocation: String? = null,
    val shopImageUrl: String? = null,
    val shopId: String? = null,
    val subscriber: ArrayList<String> = ArrayList()
)

data class Shop(
    val shopName: String? = null,
    val shopImageUrl: String? = null,
    val shopId: String? = null
)
