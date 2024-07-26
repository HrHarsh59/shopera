package project.demo.shopera.model

data class ProductData(
    val createdAt: Long? = 0L,
    val token: String? = null,
    val productCode: String? = null,
    val shopId: String? = null,
    val productImageUrl: String? = null,
    val productName: String? = null,
    val productPrice: String? = null,
    val price: String? = null,
    val productCategory: String? = null,
    val productDescription: String? = null,
    val shop: ShopData = ShopData(),
    val likes: ArrayList<String> = ArrayList()
)
