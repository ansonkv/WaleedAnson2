package com.waleed.app.business.data

data class WrapData(var tagId: Int, var tagName: String, var wrapperList: ArrayList<WrapperData>)

data class WrapperData(var id: Int, var imageUrl: String)