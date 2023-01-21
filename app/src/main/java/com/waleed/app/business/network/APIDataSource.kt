package com.waleed.app.business.network

import com.waleed.app.business.data.newdata.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIDataSource {

    @POST("Slider/List")
    fun homeSliderData(): Observable<HomeSliderDataResponse>

    @POST("Customer/Register")
    @FormUrlEncoded
    fun registerAccount(
        @Field("CustomerName") name: String,
        @Field("EmailID") email: String,
        @Field("Password") password: String,
        @Field("ConfirmPassword") confirmPass: String,
        @Field("Mobile") phone: String,
        @Field("CallingCode") selectedCountryCode: String,
        @Field("LoginType") loginType: Int,
        @Field("DeviceTokenID") deviceId: String,
        @Field("AppType") appType: Int
    ): Observable<RegisterResponse>

    @POST("Customer/Details")
    @FormUrlEncoded
    fun getCustomerDetails(@Field("CustomerID") customerID: Int): Observable<CustomerDetailsResponse>

    @POST("Customer/Login")
    @FormUrlEncoded
    fun userLogin(
        @Field("EmailID") email: String,
        @Field("Password") password: String,
        @Field("DeviceTokenID") deviceId: String,
        @Field("AppType") appType: Int
    ): Observable<RegisterResponse>

    @POST("Customer/ForgotPassword")
    @FormUrlEncoded
    fun forgotPassword(@Field("EmailID") email: String): Observable<ForgotPassword>

    @POST("Customer/ChangePassword")
    @FormUrlEncoded
    fun updatePassword(
        @Field("CustomerID") customerId: Int,
        @Field("CurrentPassword") currentPass: String,
        @Field("NewPassword") newPass: String,
        @Field("ConfirmPassword") confirmNewPass: String
    ): Observable<ChangePasswordResponse>

    @POST("Category/List")
    fun getCategoryList(): Observable<CategoryListResponse>

    @POST("Ideal/List")
    fun getIdealCategoryList(): Observable<IdealTypeListResponse>

    @POST("Product/Featured")
    @FormUrlEncoded
    fun getFeaturedProductList(
        @Field("CustomerID") customerID: Int
    ): Observable<FeaturedProductListResponse>

    @POST("AgeGroup/List")
    @FormUrlEncoded
    fun getAgeGroupList(
        @Field("IdealID") idealId: Int
    ): Observable<AgeGroupListResponse>

    @POST("Product/Ideal")
    @FormUrlEncoded
    fun getAllIdealProductList(
        @Field("IdealID") idealId: Int,
        @Field("CustomerID") customerID: Int
    ): Observable<IdealProductListResponse>

    @POST("Product/AgeGroup")
    @FormUrlEncoded
    fun getProductListOnAgeGroup(
        @Field("AgeGroupID") ageGroupId: Int,
        @Field("CustomerID") customerID: Int
    ): Observable<AgeGroupProductListResponse>

    @POST("Category/SubCategoryList")
    @FormUrlEncoded
    fun getSubCategoryList(
        @Field("CategoryID") categoryId: Int
    ): Observable<SubCategoryListResponse>

    @POST("Product/SubCategory")
    @FormUrlEncoded
    fun getSubCategoryProductList(
        @Field("CategoryID") categoryId: Int,
        @Field("SubCategoryID") subCategoryId: Int,
        @Field("CustomerID") customerID: Int
    ): Observable<SubCategoryProductListResponse>

    @POST("Product/Details")
    @FormUrlEncoded
    fun getProductDetails(
        @Field("ProductID") productId: Int,
        @Field("CustomerID") customerID: Int
    ): Observable<ProductDetailsResponse>

    @POST("Product/AddToCart")
    @FormUrlEncoded
    fun addToCart(
        @Field("ProductID") productId: Int,
        @Field("CombinationID") combinationId: Int,
        @Field("Quantity") quantity: Int,
        @Field("StoreID") storeId: Int,
        @Field("CustomerID") customerID: Int,
        @Field("DeviceTokenID") deviceToken: String
    ): Observable<AddToCartResponse>

    @POST("Product/CartCount")
    @FormUrlEncoded
    fun getCartCount(
        @Field("CustomerID") customerID: Int,
        @Field("DeviceTokenID") deviceId: String,
        @Field("AppType") appType: Int
    ): Observable<CartCountResponse>

    @POST("Product/CartList")
    @FormUrlEncoded
    fun getCartListData(
        @Field("CustomerID") customerID: Int,
        @Field("DeviceTokenID") deviceId: String
    ): Observable<CartListDataResponse>

    @POST("Product/RemoveFromCart")
    @FormUrlEncoded
    fun removeCartItem(
        @Field("CustomerID") customerID: Int,
        @Field("DeviceTokenID") deviceId: String,
        @Field("CartID") cartID: Int
    ): Observable<BaseResponse>

    @POST("Product/StoreListInCart")
    @FormUrlEncoded
    fun getStoreListForCartItem(
        @Field("CartID") cartID: Int
    ): Observable<ProductStoreCartResponse>

    @POST("Store/List")
    fun getStoreListForCartItem(): Observable<StoreListResponse>

    @POST("Product/UpdateStoreInCart")
    @FormUrlEncoded
    fun updateShopForCartItem(
        @Field("CartID") cartID: Int,
        @Field("StoreID") storeId: Int
    ): Observable<BaseResponse>

    @POST("Product/UpdateCart")
    @FormUrlEncoded
    fun updateCartItemCount(
        @Field("CartID") cartID: Int,
        @Field("CombinationID") combinationId: Int,
        @Field("Quantity") quantity: Int
    ): Observable<BaseResponse>

    @POST("Product/Category")
    @FormUrlEncoded
    fun getCategoryProductList(
        @Field("CategoryID") categoryId: Int,
        @Field("CustomerID") customerID: Int
    ): Observable<CategoryProductListResponse>

    @POST("Customer/GovStateList")
    fun getGovernorateList(): Observable<GovStateListResponse>

    @POST("Customer/AreaCityList")
    @FormUrlEncoded
    fun getAreListId(
        @Field("GovStateID") govStateID: Int
    ): Observable<AreaCityListResponse>

    @POST("Customer/AddressAdd")
    @FormUrlEncoded
    fun addNewAddress(
        @Field("CustomerID") customerID: Int,
        @Field("GovStateID") govId: Int,
        @Field("AreaCityID") areaId: Int,
        @Field("AddressType") addressType: Int,
        @Field("Block") block: String,
        @Field("Building") building: String,
        @Field("Street") street: String,
        @Field("Floor") floor: String,
        @Field("FlatNo") flatNumber: String,
        @Field("HouseNo") houseNumber: String,
        @Field("Direction") direction: String,
        @Field("Title") title: String,
        @Field("Latitude") latitude: String,
        @Field("Longitude") longitude: String
    ): Observable<BaseResponse>

    @POST("Customer/AddressList")
    @FormUrlEncoded
    fun getAddressList(
        @Field("CustomerID") customerID: Int
    ): Observable<AddressListResponse>

    @POST("Customer/AddressUpdate")
    @FormUrlEncoded
    fun updateAddress(
        @Field("CustomerAddressID") addressId: Int,
        @Field("CustomerID") customerID: Int,
        @Field("GovStateID") govId: Int,
        @Field("AreaCityID") areaId: Int,
        @Field("AddressType") addressType: Int,
        @Field("Block") block: String,
        @Field("Building") building: String,
        @Field("Street") street: String,
        @Field("Floor") floor: String,
        @Field("FlatNo") flatNumber: String,
        @Field("HouseNo") houseNumber: String,
        @Field("Direction") direction: String,
        @Field("Title") title: String,
        @Field("Latitude") latitude: String,
        @Field("Longitude") longitude: String
    ): Observable<BaseResponse>

    @POST("DeliveryMethods")
    fun getDeliveryType(): Observable<DeliveryTypeResponse>

    @POST("DeliveryMethods/PreferredTime")
    fun getDeliveryTimeList(): Observable<DeliveryTimeListResponse>

    @POST("Product/Checkout")
    @FormUrlEncoded
    fun makeCheckOut(
        @Field("CustomerID") customerID: Int,
        @Field("CustomerAddressID") addressId: Int,
        @Field("DeliveryTypeID") deliveryTypeId: Int,
        @Field("CustomizeDate") customDate: String,
        @Field("CouponCode") couponCode: String,
        @Field("CouponDiscount") couponDiscount: String,
        @Field("SubTotalAmount") subTotal: String,
        @Field("TotalAmount") totalAmount: String,
        @Field("WrappingCharges") wrappingCharge: String,
        @Field("PaymentMode") paymentMode: Int,
        @Field("ApplicationType") applicationType: Int,
        @Field("DelTimeID") deliveryTimeId: Int
    ): Observable<CheckOutResponse>

    @POST("Favorite/Add")
    @FormUrlEncoded
    fun addFavouriteItem(
        @Field("CustomerID") customerId: Int,
        @Field("ProductID") productId: Int
    ): Observable<BaseResponse>

    @POST("Favorite/Remove")
    @FormUrlEncoded
    fun removeFavouriteItem(
        @Field("CustomerID") customerId: Int,
        @Field("ProductID") productId: Int
    ): Observable<BaseResponse>

    @POST("Favorite/List")
    @FormUrlEncoded
    fun getFavouriteProducts(
        @Field("CustomerID") customerID: Int
    ): Observable<FavoriteListResponse>

    @POST("Product/MyOrders")
    @FormUrlEncoded
    fun getMyOrderList(
        @Field("CustomerID") customerID: Int
    ): Observable<MyOrderResponse>

    @POST("Search")
    @FormUrlEncoded
    fun getSearchResult(
        @Field("SearchText") searchString: String
    ): Observable<SearchResponse>

    @POST("Filter")
    fun getFilterInfo(): Observable<FilterResponse>

    @POST("Filter/Apply")
    @FormUrlEncoded
    fun getFilteredProducts(
        @Field("CategoryID") categoryID: Int,
        @Field("CustomerID") customerID: Int,
        @Field("SortBy") sortBy: String,
        @Field("Availability") availability: Int,
        @Field("MinPrice") minPrice: Int,
        @Field("MaxPrice") maxPrice: Int,
        @Field("BrandIDList") brandIDList: String,
        @Field("IdealIDList") idealIDList: String,
        @Field("AgeGroupIDList") ageGroupIDList: String,
        @Field("Rating") rating: Int
    ): Observable<FilterProductListResponse>


    @Multipart
    @POST("Product/SubmitReview")
    fun submitReview(
        @Part files: List<MultipartBody.Part>,
        @Part("CustomerID") customerID: RequestBody,
        @Part("ProductID") productId: RequestBody,
        @Part("Rating") rating: RequestBody,
        @Part("Review") review: RequestBody
    ): Observable<BaseResponse>

    @POST("Product/WrapSelection")
    fun getWrapperData(): Observable<WrapperResponse>

    @POST("Product/WrapSubmit")
    @FormUrlEncoded
    fun submitWrapper(
        @Field("CartIDList") selectedCartIdString: String,
        @Field("TagID") selectedTagId: Int,
        @Field("PaperID") selectedPaperId: Int,
        @Field("Message") selectedMessage: String
    ): Observable<BaseResponse>

    @POST("Store/TermsConditions")
    fun getTermsAndConditions(): Observable<TermsConditionsResponse>

    @POST("Store/PrivacyPolicy")
    fun getPrivacyPolicy(): Observable<PrivacyPolicyResponse>

    @POST("Store/RefundPolicy")
    fun getRefund(): Observable<RefundPolicyResponse>

    @POST("Customer/UpdateProfile")
    @Multipart
    fun updateProfile(
        @Part("CustomerID") customerID: RequestBody,
        @Part("CustomerName") name: RequestBody,
        @Part("CallingCode") callingCode: RequestBody,
        @Part("Mobile") mobile: RequestBody
    ): Observable<UpdateProfileResponse>

    @POST("Store/ContactFormSubmit")
    @FormUrlEncoded
    fun submitContactUsForm(
        @Field("Name") name: String,
        @Field("Email") email: String,
        @Field("CallingCode") callingCode: String,
        @Field("Mobile") mobile: String,
        @Field("ReportType") reportType: Int,
        @Field("Message") message: String
    ): Observable<BaseResponse>

    @POST("Product/CouponApply")
    @FormUrlEncoded
    fun applyCoupon(
        @Field("CustomerID") customerId: Int,
        @Field("SubTotal") subTotal: String,
        @Field("CouponCode") couponCode: String
    ): Observable<DiscountCouponResponse>

    @POST("Customer/SocialLogin")
    @FormUrlEncoded
    fun socialLogin(
        @Field("EmailID") email: String,
        @Field("CustomerName") name: String,
        @Field("LoginType") loginType: Int,
        @Field("DeviceTokenID") deviceToken: String,
        @Field("AppType") appType: Int
    ): Observable<RegisterResponse>

    @POST("NotificationList")
    @FormUrlEncoded
    fun getNotification(
        @Field("CustomerID") customerID: Int
    ): Observable<NotificationResponse>

    @POST("Store/SocialMediaLinks")
    fun getSocialLinks(): Observable<SocialMediaReponse>

    @POST("Customer/ChildList")
    @FormUrlEncoded
    fun getChildList(
        @Field("CustomerID") customerID: Int
    ): Observable<ChildListResponse>

    @POST("Customer/RemoveChild")
    @FormUrlEncoded
    fun deleteChildItem(@Field("ChildID") childID: Int): Observable<BaseResponse>


    @POST("Customer/AddChild")
    @FormUrlEncoded
    fun addNewChild(
        @Field("CustomerID") customerId: Int,
        @Field("ChildName") childName: String,
        @Field("BirthDate") dateOfBirth: String
    ): Observable<BaseResponse>

    @POST("Customer/UpdateChild")
    @FormUrlEncoded
    fun updateChildData(
        @Field("ChildID") childID: Int,
        @Field("CustomerID") customerId: Int,
        @Field("ChildName") childName: String,
        @Field("BirthDate") dateOfBirth: String
    ): Observable<BaseResponse>

    @POST("Customer/AddressRemove")
    @FormUrlEncoded
    fun deleteAddress(
        @Field("CustomerAddressID") addressId: Int,
        @Field("CustomerID") customerId: Int
    ): Observable<BaseResponse>

    @POST("Product/PayMethods")
    fun getPaymentMethods(): Observable<PaymentMethodsResponse>

    @POST("Customer/GuestLogin")
    @FormUrlEncoded
    fun guestUserLogin(

        @Field("GovStateID") govId: Int,
        @Field("AreaCityID") areaId: Int,
        @Field("AddressType") addressType: Int,
        @Field("Block") block: String,
        @Field("Building") building: String,
        @Field("Street") street: String,
        @Field("Floor") floor: String,
        @Field("FlatNo") flatNumber: String,
        @Field("HouseNo") houseNumber: String,
        @Field("Direction") direction: String,
        @Field("Title") title: String,
        @Field("AppType") appType:Int,
        @Field("DeviceTokenID")deviceId:String,
        @Field("Mobile") mobileNUmber:String,
        @Field("CallingCode") callingCode:String,
        @Field("EmailID") emailId:String,
        @Field("CustomerName")customerName:String

    ): Observable<GuestLoginResponse>


}