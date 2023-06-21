package com.example.shopify.data.models.payment

import com.google.gson.annotations.SerializedName

data class PaymentIntentApiModel(
    val id: String? = null,
    @field:SerializedName("object")
    val paymentIntentApiModelObject: String? = null,
    val amount: Long? = null,
    @field:SerializedName("amount_capturable")
    val amountCapturable: Long? = null,
    @field:SerializedName("amount_details")
    val amountDetails: AmountDetails? = null,
    @field:SerializedName("amount_received")
    val amountReceived: Long? = null,
    val application: Any? = null,
    @field:SerializedName("application_fee_amount")
    val applicationFeeAmount: Any? = null,
    @field:SerializedName("automatic_payment_methods")
    val automaticPaymentMethods: AutomaticPaymentMethods? = null,
    @field:SerializedName("canceled_at")
    val canceledAt: Any? = null,
    @field:SerializedName("cancellation_reason")
    val cancellationReason: Any? = null,
    @field:SerializedName("capture_method")
    val captureMethod: String? = null,
    @field:SerializedName("client_secret")
    val clientSecret: String? = null,
    @field:SerializedName("confirmation_method")
    val confirmationMethod: String? = null,
    val created: Long? = null,
    val currency: String? = null,
    val customer: String? = null,
    val description: Any? = null,
    val invoice: Any? = null,
    @field:SerializedName("last_payment_error")
    val lastPaymentError: Any? = null,
    @field:SerializedName("latest_charge")
    val latestCharge: Any? = null,
    val livemode: Boolean? = null,
    val metadata: Metadata? = null,
    @field:SerializedName("next_action")
    val nextAction: Any? = null,
    @field:SerializedName("on_behalf_of")
    val onBehalfOf: Any? = null,
    @field:SerializedName("payment_method")
    val paymentMethod: Any? = null,
    @field:SerializedName("payment_method_options")
    val paymentMethodOptions: PaymentMethodOptions? = null,
    @field:SerializedName("payment_method_types")
    val paymentMethodTypes: List<String>? = null,
    val processing: Any? = null,
    @field:SerializedName("receipt_email")
    val receiptEmail: Any? = null,
    val review: Any? = null,
    @field:SerializedName("setup_future_usage")
    val setupFutureUsage: Any? = null,
    val shipping: Any? = null,
    val source: Any? = null,
    @field:SerializedName("statement_descriptor")
    val statementDescriptor: Any? = null,
    @field:SerializedName("statement_descriptor_suffix")
    val statementDescriptorSuffix: Any? = null,
    val status: String? = null,
    @field:SerializedName("transfer_data")
    val transferData: Any? = null,
    @field:SerializedName("transfer_group")
    val transferGroup: Any? = null
)

data class AmountDetails(
    val tip: Metadata? = null
)

class Metadata()

data class AutomaticPaymentMethods(
    @field:SerializedName("")
    val allowRedirects: String? = null,
    val enabled: Boolean? = null
)

data class PaymentMethodOptions(
    val card: Card? = null,
    val link: Link? = null
)

data class Card(

    val installments: Any? = null,
    @field:SerializedName("mandate_options")
    val mandateOptions: Any? = null,
    val network: Any? = null,
    @field:SerializedName("request_three_d_secure")
    val requestThreeDSecure: String? = null
)

data class Link(
    @field:SerializedName("persistent_token")
    val persistentToken: Any? = null
)

