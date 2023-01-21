package com.waleed.app.business.data.newdata

data class GuestAddressData(
    var addressType: Int,
    var title: String,
    var govtId: Int,
    var areaId: Int,
    var street: String,
    var flatNumber: String,
    var block: String,
    var buildingNumber: String,
    var description: String,
    var floorNumber: String,
    var custName: String,
    var custEmail: String,
    var mobile: String
)
