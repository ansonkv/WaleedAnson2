package com.waleed.app.business.data

import android.os.Parcelable
import java.io.Serializable

data class AddressData(
    var addressId: Int,
    var addressName: String,
    var areaName: String,
    var blockNumber: Int,
    var streetNumber: Int,
    var avenueName: String,
    var houseNumber: String,
    var landMark: String,
    var isDefault: Boolean
):Serializable