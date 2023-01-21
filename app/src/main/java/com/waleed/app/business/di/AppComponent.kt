package com.waleed.app.business.di

import com.waleed.app.Waleed
import com.waleed.app.business.di.modules.AppModule
import com.waleed.app.business.di.modules.NetworkModule
import com.waleed.app.ui.account.birthday.list.BirthdayListActivity
import com.waleed.app.ui.account.birthday.newchild.NewChildActivity
import com.waleed.app.ui.account.changepass.ChangePasswordActivity
import com.waleed.app.ui.account.contactus.ContactUsActivity
import com.waleed.app.ui.account.edit.EditProfileActivity
import com.waleed.app.ui.account.forgotpassword.ForgotPasswordActivity
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.account.terms.TermsActivity
import com.waleed.app.ui.address.list.AddressListActivity
import com.waleed.app.ui.address.mappickup.LocationMapActivity
import com.waleed.app.ui.address.newaddress.NewAddressActivity
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.cart.store.CartStoreActivity
import com.waleed.app.ui.checkout.CheckoutActivity
import com.waleed.app.ui.favourite.FavouriteProductListActivity
import com.waleed.app.ui.filter.SearchFilterActivity
import com.waleed.app.ui.home.HomeActivity
import com.waleed.app.ui.home.category.CategoryFragment
import com.waleed.app.ui.home.landing.HomeFragment
import com.waleed.app.ui.home.landing.ideal.HomeIdealFragment
import com.waleed.app.ui.home.landing.featured.HomeFeaturedFragment
import com.waleed.app.ui.home.profile.ProfileFragment
import com.waleed.app.ui.notification.NotificationActivity
import com.waleed.app.ui.orders.details.OrderDetailsActivity
import com.waleed.app.ui.orders.list.OrdersListActivity
import com.waleed.app.ui.payment.PaymentActivity
import com.waleed.app.ui.payment.PaymentSuccess
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.ui.productlist.ProductListActivity
import com.waleed.app.ui.rate.OrderRateActivity
import com.waleed.app.ui.reviews.ReviewsActivity
import com.waleed.app.ui.search.SearchActivity
import com.waleed.app.ui.splash.SplashActivity
import com.waleed.app.ui.stores.StoresActivity
import com.waleed.app.ui.subcategory.SubCategoryActivity
import com.waleed.app.ui.wrap.WrapActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class,NetworkModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Waleed): Builder

        fun build(): AppComponent
    }

    fun inject(splashActivity: SplashActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(categoryFragment: CategoryFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(homeFeaturedFragment: HomeFeaturedFragment)
    fun inject(idealFragment: HomeIdealFragment)
    fun inject(productListActivity: ProductListActivity)
    fun inject(productDetailsActivity: ProductDetailsActivity)
    fun inject(reviewsActivity: ReviewsActivity)
    fun inject(storesActivity: StoresActivity)
    fun inject(cartActivity: CartActivity)
    fun inject(wrapActivity: WrapActivity)
    fun inject(checkoutActivity: CheckoutActivity)
     fun inject(paymentSuccess: PaymentSuccess)
    fun inject(addressListActivity: AddressListActivity)
    fun inject(newAddressActivity: NewAddressActivity)
    fun inject(locationMapActivity: LocationMapActivity)
    fun inject(signInActivity: SignInActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(ordersListActivity: OrdersListActivity)
    fun inject(notificationActivity: NotificationActivity)
    fun inject(orderDetailsActivity: OrderDetailsActivity)
    fun inject(orderRateActivity: OrderRateActivity)
    fun inject(searchFilterActivity: SearchFilterActivity)
    fun inject(forgotPasswordActivity: ForgotPasswordActivity)
    fun inject(changePasswordActivity: ChangePasswordActivity)
    fun inject(subCategoryActivity: SubCategoryActivity)
    fun inject(cartStoreActivity: CartStoreActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(favouriteProductListActivity: FavouriteProductListActivity)
    fun inject(searchActivity: SearchActivity)
    fun inject(termsActivity: TermsActivity)
    fun inject(editProfileActivity: EditProfileActivity)
    fun inject(contactUsActivity: ContactUsActivity)
    fun inject(birthdayListActivity: BirthdayListActivity)
    fun inject(newChildActivity: NewChildActivity)

}