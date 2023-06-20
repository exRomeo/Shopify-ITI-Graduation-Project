package com.example.shopify.data.models.payment

import com.google.gson.annotations.SerializedName

data class EphemeralKeyApiModel (
    val id: String? = null,
    @field:SerializedName("object")
    val ephemeralKeyApiModelObject: String? = null,
    @field:SerializedName("associated_objects")
    val associatedObjects: List<AssociatedObject>? = null,
    val created: Long? = null,
    val expires: Long? = null,
    val livemode: Boolean? = null,
    val secret: String? = null
)

data class AssociatedObject (
    val id: String? = null,
    val type: String? = null
)