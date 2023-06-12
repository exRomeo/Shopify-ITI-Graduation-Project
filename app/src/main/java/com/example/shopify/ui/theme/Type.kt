package com.example.shopify.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.shopify.R

val IbarraFont = FontFamily(
    Font(R.font.ibarra_real_nova_regular),
    Font(R.font.ibarra_real_nova_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.ibarra_real_nova_bold, weight = FontWeight.Bold)
)

val ibarraRegular = TextStyle(
    fontFamily = IbarraFont,
    fontWeight = FontWeight.Normal,
    //color = ,
)
val ibarraSemiBold = TextStyle(
    fontFamily = IbarraFont,
    fontWeight = FontWeight.SemiBold,
    //color = ,
)
val ibarraBold = TextStyle(
    fontFamily = IbarraFont,
    fontWeight = FontWeight.Bold,
    //color = ,
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)