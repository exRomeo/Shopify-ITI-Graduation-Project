package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.shopify.R
import com.example.shopify.data.models.Options

@Composable
fun ColorOptions(option: List<Options>) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val colorList = getColor()
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
    ) {
        Log.i("TAG", "ColorOptions List: $colorList")
        option.forEachIndexed { index, _ ->

//            Log.i("TAG", "ColorOptions option: ${option[index].values?.get(0)}")
            if (option[index].name == "Color") {
//                    Text(option[index].values?.get(0) ?: "")
                LazyRow {
                    items(1) {
                        option[index].values?.forEachIndexed { colorIndex, _ ->
                            val color = colorList[option[index].values?.get(colorIndex)]
                            Surface(
                                shape = CircleShape,
                                border = if (selectedIndex == colorIndex) BorderStroke(
                                    2.dp,
                                    MaterialTheme.colorScheme.surfaceTint
                                ) else null,
                                onClick = { selectedIndex = colorIndex },
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.circle),
                                    contentDescription = stringResource(id = R.string.color_circle),
                                    tint = color ?: Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }
}

fun getColor(): Map<String, Color> {
    return mapOf(
        "red" to Color.Red,
        "green" to Color(0xFF008000),
        "blue" to Color.Blue,
        "yellow" to Color(0xFFF3F33A),
        "pink" to Color.Magenta,
        "black" to Color.Black,
        "white" to Color.White,
        "beige" to Color(0xFFCFCFA8),
        "light_brown" to Color(0xFFD2B48C),
        "burgandy" to Color(0xFF800020),
        "gray" to Color.Gray,

        )
}