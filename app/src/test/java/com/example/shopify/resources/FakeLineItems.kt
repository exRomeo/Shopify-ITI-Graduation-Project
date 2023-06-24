package com.example.shopify.resources

import com.example.shopify.data.models.draftorder.LineItem

object FakeLineItems {
    val lineItems = mutableListOf(
        LineItem(
            variantID = 1,
            productID = 1,
            title = "Variant 1",
            quantity = 2,
            name = "Item 1",
            price = "10.00"
        ),
        LineItem(
            variantID = 2,
            productID = 2,
            title = "Variant 2",
            quantity = 1,
            name = "Item 2",
            price = "15.50"
        ),
        LineItem(
            variantID = 3,
            productID = 3,
            title = "Variant 3",
            quantity = 3,
            name = "Item 3",
            price = "8.75"
        ),
        LineItem(
            variantID = 4,
            productID = 4,
            title = "Variant 4",
            quantity = 1,
            name = "Item 4",
            price = "12.99"
        ),
        LineItem(
            variantID = 5,
            productID = 5,
            title = "Variant 5",
            quantity = 2,
            name = "Item 5",
            price = "9.50"
        ),
        LineItem(
            variantID = 6,
            productID = 6,
            title = "Variant 6",
            quantity = 1,
            name = "Item 6",
            price = "14.25"
        ),
        LineItem(
            variantID = 7,
            productID = 7,
            title = "Variant 7",
            quantity = 4,
            name = "Item 7",
            price = "7.99"
        ),
        LineItem(
            variantID = 8,
            productID = 8,
            title = "Variant 8",
            quantity = 2,
            name = "Item 8",
            price = "18.50"
        ),
        LineItem(
            variantID = 9,
            productID = 9,
            title = "Variant 9",
            quantity = 3,
            name = "Item 9",
            price = "11.75"
        ),
        LineItem(
            variantID = 10,
            productID = 10,
            title = "Variant 10",
            quantity = 1,
            name = "Item 10",
            price = "16.99"
        )
    )


}