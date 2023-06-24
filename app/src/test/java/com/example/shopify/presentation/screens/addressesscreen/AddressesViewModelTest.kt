package com.example.shopify.presentation.screens.addressesscreen


import com.example.shopify.MainCoroutineRule
import com.example.shopify.resources.FakeAddress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AddressesViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var addressViewModel: AddressesViewModel

    @Before
    fun setup() {
        addressViewModel = AddressesViewModel(FakeAddressRepository())
    }


    @Test
    fun getAddresses_AddressesReceived() = runTest {

        //Given set of 3 addresses
        val items = addressViewModel.addresses

        //When received address list
        val result = items.first()

        //Then address list size is 3
        assertThat(result.size, `is`(3))
    }

    @Test
    fun addAddress_addressesIncreased() = runTest {

        //Given set of 3 addresses
        val items = addressViewModel.addresses

        val originalValue = items.value.size

        //When adding new address
        addressViewModel.addAddress(FakeAddress.address)

        val result = items.first()

        //then address list is increased by one
        assertThat(result.size, `is`(originalValue + 1))
    }

    @Test
    fun deleteAddress_addressesDecreased() = runTest {

        //Given set of 3 addresses
        val items = addressViewModel.addresses
        val originalValue = items.value.size
        //When removing an address
        addressViewModel.removeAddress(FakeAddress.address1)
        val result = items.first()

        //then address list is decreased by one
        assertThat(result.size, `is`(originalValue - 1))
    }

    @Test
    fun updateAddress_addressesIsUpdated() = runTest {

        //Given set of addresses
        val items = addressViewModel.addresses

        //When updating an address
        val newValue = "New Address"
        val newAddress = FakeAddress.address1.copy(fullAddress = newValue)
        addressViewModel.updateAddress(newAddress)
        val result = items.first()

        //then address is updated
        val address = result[0]
        assertThat(address, `is`(newAddress))
    }


}