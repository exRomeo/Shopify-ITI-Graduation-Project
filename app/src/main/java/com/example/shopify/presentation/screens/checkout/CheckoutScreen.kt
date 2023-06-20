package com.example.shopify.presentation.screens.checkout

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.currency.Currency
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.common.composables.NoConnectionScreen
import com.example.shopify.presentation.common.composables.NoData
import com.example.shopify.presentation.common.composables.SelectionDropdownMenu
import com.example.shopify.presentation.common.composables.SingleSelectionDropdownMenu
import com.example.shopify.utilities.ShopifyApplication
import com.stripe.android.paymentsheet.PaymentSheetContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavHostController) {

    val viewModel: CheckoutViewModel = viewModel(
        factory = CheckoutViewModelFactory(
            checkoutRepository =
            (LocalContext.current.applicationContext as ShopifyApplication).checkoutRepository
        )
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(context.getString(it))
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        bottomBar = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.fillMaxWidth(0.92f), onClick = {
//PaymentConfiguration.init(context,BuildConfig.STRIP_PUB_KEY);
//                        val paymentSheet = PaymentSheet(this@Row, )
//                        viewModel.confirmOrder()
//                        navController.navigate(Screens.Payment.route)
                        viewModel.makePayment()
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(id = R.string.confirm),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.Done,
                            contentDescription = ""
                        )
                    }
                }

            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.checkout),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val state by viewModel.state.collectAsState()
            when (state) {
                is UserScreenUISState.Loading -> {
                    LottieAnimation(animation = R.raw.loading_animation)
                }

                is UserScreenUISState.Success<*> -> {
                    val cartItems =
                        (state as UserScreenUISState.Success<*>).data as List<LineItem>

                    CheckoutScreenContent(
                        viewModel = viewModel, cartItems = cartItems
                    )
                }

                is UserScreenUISState.NotConnected -> {
                    NoConnectionScreen()
                }

                is UserScreenUISState.NoData -> {
                    NoData(message = "Add Some Products!")
                }

                else -> {}
            }
        }
    }
}

@Composable
fun CheckoutScreenContent(viewModel: CheckoutViewModel, cartItems: List<LineItem>) {
    SingleSelectionDropdownMenu(
        title = stringResource(id = R.string.choose_currency),
        items = Currency.list
    ) { currency ->
        viewModel.getExchangeRate(currency)
    }
    val addresses by viewModel.addresses.collectAsState()
    SelectionDropdownMenu(
        title = stringResource(id = R.string.choose_address),
        items = addresses
    ) { address ->
        viewModel.chooseAddress(address)
    }
    Spacer(modifier = Modifier.padding(top = 8.dp))
    CheckoutItems(cartItems = cartItems, viewModel = viewModel)


    val stripeLauncher = rememberLauncherForActivityResult(
        contract = PaymentSheetContract(),
        onResult = {
            viewModel.handlePaymentResult(it)
        }
    )
    val clientSecret by viewModel.clientSecret.collectAsStateWithLifecycle()
    clientSecret?.let {
        val args = PaymentSheetContract.Args.createPaymentIntentArgs(it)
        stripeLauncher.launch(args)
        viewModel.onPaymentLaunched()
    }
}

@Composable
fun CheckoutItems(cartItems: List<LineItem>, viewModel: CheckoutViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Order Details",
                            style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Divider(thickness = 2.dp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
                items(cartItems) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "${it.title} x ${it.quantity} = ${it.getTotalPrice()}",
                            style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        val itemsCount by viewModel.totalItems.collectAsState()
        val totalPrice by viewModel.totalPrice.collectAsState()
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = itemsCount)
                Text(text = "Total Price = ${viewModel.currencySymbol} $totalPrice")
                var code by remember { mutableStateOf("") }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(0.7f),
                        value = code,
                        onValueChange = { code = it },
                        label = {
                            Text(
                                text = stringResource(id = R.string.discount_code)
                            )
                        }
                    )
                }
                Text(text = stringResource(id = R.string.payment_method))
                var selectedOption by remember { mutableStateOf(1) }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.cod))
                    RadioButton(
                        selected = selectedOption == 0,
                        onClick = {
                            if (totalPrice.toInt() < 1000)
                                selectedOption = 0
                            else
                                viewModel.showMessage(R.string.no_cod)
                        },
                    )
                    Text(text = stringResource(id = R.string.credit))
                    RadioButton(
                        selected = selectedOption == 1,
                        onClick = { selectedOption = 1 }
                    )
                }

            }
        }
    }
}