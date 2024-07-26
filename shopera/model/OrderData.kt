package project.demo.shopera.model

data class OrderData(
    val createdAt: Long? = 0L,
    val productCode: String? = null,
    val shopId: String? = null,
    val productImageUrl: String? = null,
    val productName: String? = null,
    val productPrice: String? = null,
    val orderId: String? = null,
    val deliveryType: String? = null,
    val orderStatus: String? = null,
    val quantity: String? = null,
    val userAddress: UserAddress = UserAddress(),
    val shopData: ShopData = ShopData(),
    val deliveryStatus: String? = null,
    val paymentStatus: String? = null
)
