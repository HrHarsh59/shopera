package project.demo.shopera.model

data class CartData(
    val createdAt: Long? = 0L,
    val productCode: String? = null,
    val shopId: String? = null,
    val productImageUrl: String? = null,
    val productName: String? = null,
    val productPrice: String? = null,
    val shopName: String? = null,
    val shopImageUrl: String? = null,
    val token: String? = null,
    val productDescription: String? = null
)
