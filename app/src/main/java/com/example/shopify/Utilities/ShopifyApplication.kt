package com.example.shopify.utilities

import android.app.Application
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.RetrofitHelper
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.core.utils.SharedPreference
import com.example.shopify.data.managers.orders.OrdersManager
import com.example.shopify.data.managers.address.AddressManager
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.models.CollectCurrentCustomerData
import com.example.shopify.data.models.GetCurrentCustomer.getCurrentCustomer
import com.example.shopify.data.remote.authentication.AuthenticationClient
import com.example.shopify.data.remote.authentication.IAuthenticationClient
import com.example.shopify.data.remote.product.RemoteResource
import com.example.shopify.data.repositories.authentication.AuthRepository
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.data.repositories.product.ProductRepository
import com.example.shopify.data.repositories.user.IUserDataRepository
import com.example.shopify.data.repositories.user.UserDataRepository
import com.example.shopify.data.repositories.user.remote.retrofitclient.ShopifyAPIClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"
private const val CUSTOMER_PREF_NAME = "customer"


class ShopifyApplication : Application() {

    val repository: IProductRepository by lazy {
        ProductRepository(
            RemoteResource.getInstance(context = applicationContext)
        )
    }

     val sharedPreference by lazy{
        SharedPreference.customPreference(applicationContext,CUSTOMER_PREF_NAME)
    }
    private val authenticationClient: IAuthenticationClient by lazy {
        AuthenticationClient(
            RetrofitHelper.getAuthenticationService(BASE_URL),
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance()
        )
    }

    val authRepository: IAuthRepository by lazy {
        AuthRepository(
            authenticationClient,
            SharedPreference.customPreference(applicationContext, CUSTOMER_PREF_NAME)
        )
    }
    var currentCustomer: CollectCurrentCustomerData? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        ConnectionUtil.initialize(applicationContext)
        if (authRepository.checkedLoggedIn()) { //Is loggedIn

            GlobalScope.launch(Dispatchers.IO) {
                currentCustomer = getCurrentCustomer(authRepository)
                CurrentUserHelper.initialize(authRepository)
            }
        } else {  //IsNot LoggedIn
            currentCustomer = null
        }
    }

    val addressManager: AddressManager by lazy {
        AddressManager(ShopifyAPIClient.customerAddressAPI)
    }

     val wishlistManager: WishlistManager by lazy {
        WishlistManager(ShopifyAPIClient.draftOrderAPI)
     }

    val cartManager: CartManager by lazy {
        CartManager(ShopifyAPIClient.draftOrderAPI)
    }

    val ordersManager: OrdersManager by lazy {
        OrdersManager(ShopifyAPIClient.ordersAPI)
    }

    val userDataRepository: IUserDataRepository by lazy {
        UserDataRepository(
            addressManager,
            wishlistManager,
            cartManager,
            ordersManager
        )
    }
}