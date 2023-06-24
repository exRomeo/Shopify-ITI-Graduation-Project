package com.example.shopify.data.repositories.address

import com.example.shopify.resources.FakeAddress
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AddressRepositoryTest {


    private lateinit var addressRepository: AddressRepository

    @Before
    fun setup() {
        addressRepository = AddressRepository(FakeAddressManager())
    }


    @Test
    fun getAddresses_AddressesReceived() = runTest {

        //Given set of 3 addresses
        val items = addressRepository.address
        addressRepository.getAddresses()

        //When received address list
        val result = items.first()

        //Then address list size is 3
        assertThat(result.size, `is`(3))
    }

    @Test
    fun addAddress_addressesIncreased() = runTest {

        //Given set of 3 addresses
        val items = addressRepository.address
        addressRepository.getAddresses()

        //When adding new address
        addressRepository.addAddress(FakeAddress.address)
        val result = items.first()

        //then address list is increased by one
        assertThat(result.size, `is`(4))
    }

    @Test
    fun deleteAddress_addressesDecreased() = runTest {

        //Given set of 3 addresses
        val items = addressRepository.address
        addressRepository.getAddresses()

        //When removing an address
        addressRepository.removeAddress(FakeAddress.address1)
        val result = items.first()

        //then address list is decreased by one
        assertThat(result.size, `is`(2))
    }

    @Test
    fun updateAddress_addressesIsUpdated() = runTest {

        //Given set of addresses
        val items = addressRepository.address
        addressRepository.getAddresses()

        //When updating an address
        val newValue = "New Address"
        val newAddress = FakeAddress.address1.copy(fullAddress = newValue)
        addressRepository.updateAddress(newAddress)
        val result = items.first()

        //then address is updated
        val address = result[0]
        assertThat(address, `is`(newAddress))
    }


}