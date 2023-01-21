package com.waleed.app.business.data

data class SearchFilterCategory(var id: Int, var name: String, var valueList: List<SearchFilterCategoryValues>?,var isMultiChoice:Boolean,var count:Int?)

data class SearchFilterCategoryValues(var valueID: Int, var valueName: String)