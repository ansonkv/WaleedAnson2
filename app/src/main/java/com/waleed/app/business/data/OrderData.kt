package com.waleed.app.business.data

data class OrderData(
    var id: String,
    var date: String,
    var status: Int,
    var productList: List<OrderProductData>
)

data class OrderProductData(
    var imageUrl: String,
    var name: String,
    var quantity: Int,
    var amount: Double
)