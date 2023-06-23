package com.example.shopify.presentation.common.composables

import androidx.annotation.RawRes
import com.example.shopify.R
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.contentColorFor
import com.example.shopify.BuildConfig
import com.example.shopify.core.navigation.Screens
import com.example.shopify.presentation.screens.authentication.common_auth_components.AuthenticationButton
import com.example.shopify.ui.theme.IbarraFont
import com.example.shopify.ui.theme.backgroundColor
import com.example.shopify.ui.theme.hintColor
import com.example.shopify.ui.theme.mainColor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun ShowCustomDialog(
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes buttonText: Int,
    @StringRes cancelText: Int = R.string.cancel,
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
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(36.dp))
                        Text(
                            text = stringResource(id = description),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 30.dp, start = 25.dp, end = 25.dp)
                                .fillMaxWidth(),
                            fontFamily = IbarraFont,

                            style = MaterialTheme.typography.bodyLarge,  //headlineSmall
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                onClick = onClose,
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(36.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(5.dp)),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = hintColor)
                            ) {
                                Text(
                                    text = stringResource(id = cancelText),
                                    color = mainColor
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(
                                onClick = onClickButton,
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(36.dp)
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(5.dp)
                                    )
                            ) {
                                Text(
                                    text = stringResource(id = buttonText),
                                    color = Color.White
                                )
                            }
                        }

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