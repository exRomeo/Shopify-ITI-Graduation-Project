package com.example.shopify.presentation.common.composables

import androidx.annotation.RawRes
import com.example.shopify.R
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ShowCustomDialog(
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes buttonText: Int,
    @RawRes animatedId: Int,
    onClose: () -> Unit,
    onClickButton: () -> Unit
) {
    Dialog(onDismissRequest = onClickButton) {
        Box(
            modifier = Modifier
                .height(460.dp)
        ) {

            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(230.dp))
                Box(
                    modifier = Modifier
                        .height(520.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                        )
                ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.TopStart),
                        onClick = {
                            onClickButton()
                            onClose()
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(id = title),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = description),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onClickButton,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Text(
                                text = stringResource(id = buttonText),
                                color = Color.White
                            )
                        }
                        /*ElevatedButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Text(
                                text = "Enable",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))*/
                    }

                }
            }
            HeaderImage(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter),
                animationId = animatedId
            )
        }

    }
}

@Composable
fun HeaderImage(
    modifier: Modifier,
    @RawRes animationId: Int
) {
    LottieAnimation(animation = animationId, modifier = modifier)
}