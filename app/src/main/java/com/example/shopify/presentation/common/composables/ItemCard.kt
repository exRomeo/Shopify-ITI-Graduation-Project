package com.example.shopify.presentation.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.models.order.OrderIn


@Composable
fun SettingItemCard(
    modifier: Modifier = Modifier,
    mainText: String,
    subText: String,
    iconButton: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mainText,
                    style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize)
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = subText,
                    style = TextStyle(color = Color.Gray),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            iconButton()
        }
    }
}

@Composable
@Preview
fun PreviewSettingItemCard() {
    SettingItemCard(
        mainText = "Main Text",
        subText = "Sub Text",
        iconButton = {
            IconButton(onClick = { /*TODO*/ }, enabled = false) {
                Icon(Icons.Default.Place, "")
            }
        }
    ) {

    }
}

@Composable
fun WishlistItemCard(
    modifier: Modifier = Modifier,
    product: ProductSample,
    onRemoveItem: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.image.src)
                        .placeholder(R.drawable.product_image_placeholder)
                        .error(R.drawable.product_image_placeholder)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.product_image),
                    placeholder = painterResource(id = R.drawable.product_image_placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .size(125.dp)
                )

                Column(
                    modifier
                        .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = product.title,
                        style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = product.variants[0].price ?: "0.00",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .height(125.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            onRemoveItem()
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            stringResource(id = R.string.remove)
                        )
                    }
                }
            }
        }

    }
}

@Composable
@Preview
fun WishlistItemCardPreview() {
    WishlistItemCard(
        product = ProductSample(
            5,
            "Very Long Product Title1235",
            listOf(Product(1, 0, "Product TITLE", "10.254", 10L)),
            listOf(Image("")),
            Image(""),
        ),
        onRemoveItem = {},
        onClick = {}
    )
}

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    product: ProductSample,
    initialCount: Long,
    maxCount: Long,
    increase: () -> Unit,
    decrease: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        var count by remember { mutableStateOf(initialCount) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.images[0].src)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.product_image),
                placeholder = painterResource(id = R.drawable.product_image_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .width(125.dp)
                    .height(125.dp)
            )
            Column(
                modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
                    .weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = product.variants[0].price ?: "0.00!",
                    style = TextStyle(color = Color.Gray),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .height(125.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = {
                            increase()
                            if (count < maxCount) {
                                count++
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            stringResource(id = R.string.remove)
                        )
                    }
                    Text(
                        text = count.toString(),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            textAlign = TextAlign.Center
                        )
                    )
                    IconButton(
                        onClick = {
                            decrease()
                            if (count > 1) {
                                count--
                            }
                        }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.remove),
                            stringResource(id = R.string.remove)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CartItemCardPreview() {
    CartItemCard(
        product = ProductSample(
            5,
            "Very Long Product Title1235",
            listOf(Product(1, 0, "Product TITLE", "10.254", 10L)),
            listOf(Image("")),
            Image(""),
        ),
        initialCount = 0,
        maxCount = 12,
        increase = {},
        decrease = {},
        onClick = {}
    )
}

@Composable
fun OrderItemCard(
    modifier: Modifier = Modifier,
    order: OrderIn,
    onCancelClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${stringResource(id = R.string.order_no)} ${order.id}")
                Text(text = "${stringResource(id = R.string.placed_on)} ${order.getDateTime()[0]}")
            }
            TextButton(onClick = { onCancelClick() }) {
                Text(
                    text = stringResource(id = R.string.cancel_order),
                    style = TextStyle(color = MaterialTheme.colorScheme.error)
                )
            }
        }
        Divider(Modifier.padding(horizontal = 8.dp))
        Row {
            Column(
                modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
                    .weight(1f)
            ) {
                LazyColumn(modifier = Modifier.heightIn(0.dp, 150.dp)) {
                    items(order.lineItems) {
                        LineItemCard(it)
                    }
                    item {
                        Divider(Modifier.padding(horizontal = 8.dp))
                        Text(
                            text = "Total = ${order.currency} ${order.total}",
                            style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(text = stringResource(id = R.string.expected_to_arrive))

            }

        }
    }
}

@Composable
@Preview
fun LineItemCard(lineItem: LineItem = LineItem(1, 0, "Product TITLE", 10, "item", "5200.00")) {
    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "${lineItem.title} x ${lineItem.quantity}",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "EGP ${lineItem.getTotalPrice()}",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
fun OrderItemCardPreview() {
    OrderItemCard(
        order = OrderIn(
            id = 54556464646454,
            total = "500.0",
            totalDiscounts = "10",
            orderURL = "",
            lineItems = listOf(
                LineItem(1, 0, "Product TITLE", 10, "item", "5200.00"),
                LineItem(1, 0, "Product TITLE", 10, "item", "520.00"),
                LineItem(1, 0, "Product TITLE", 10, "item", "520.0"),
                LineItem(1, 0, "Product TITLE", 10, "item", "520.0"),
            ),
            date = "2023-06-19T23:05:26-04:00",
            currency = "EGP",
            confirmed = false
        ),
        onCancelClick = {}
    ) {

    }
}

