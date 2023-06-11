package com.example.shopify.presentation.screens.homescreen

import android.R
import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify.ui.theme.ShopifyTheme


val mainCategories = listOf("Men", "Women", "Kid", "Sale")

@Composable
fun MainFilters(
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    cornerRadius: Int = 30,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {

    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }
    Row(
        modifier = Modifier,
    ) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                onClick = {
                    selectedIndex.value = index
                    onItemSelection(selectedIndex.value)
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStartPercent = cornerRadius,
                        topEndPercent = 0,
                        bottomStartPercent = cornerRadius,
                        bottomEndPercent = 0
                    )

                    items.size - 1 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = cornerRadius,
                        bottomStartPercent = 0,
                        bottomEndPercent = cornerRadius
                    )

                    else -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )
                },
                colors = if (selectedIndex.value == index) {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )

                } else {
                    ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                }
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedIndex.value == index) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )

            }


        }
    }
}

@Composable
fun SliderComponent(onPriceValueChanged: (Float) -> Unit) {
    var sliderValue by remember {
        mutableStateOf(0f)
    }
    Slider(
        value = sliderValue,
        onValueChange = { sliderValue_ ->
            sliderValue = sliderValue_
        },
        onValueChangeFinished = { onPriceValueChanged(sliderValue) },
        valueRange = 0f..1000f,
        steps = 9
    )

    Text(text = sliderValue.toString())
}

@Composable
fun CategoriesItems() {
    val list = (1..10).map { it.toString() }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
// content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(list.size) { index ->
                Card(
                    // backgroundColor = Color.Red,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    // elevation = CardElevation(),
                ) {
                    Text(
                        text = list[index],
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    )
}

enum class FloatingButtonState {
    Expanded,
    Collapsed
}

data class MiniFABItem(
    val icon: Int,
    val label:String,
    val identifier:String
)

@Composable
fun MiniFAB(
    item:MiniFABItem,
    onMiniFABClicked:(MiniFABItem)->Unit
){
    Button(onClick = { onMiniFABClicked.invoke(item) }

    ){
Text(text = "jjj")
    }
//    Canvas(
//        modifier = Modifier
//            .size(32.dp)
//            .clickable(
//                interactionSource = MutableInteractionSource(),
//                onClick = {
//                    onMiniFABClicked.invoke(item)
//                },
//                indication = rememberRipple(
//                    bounded = false,
//                    radius = 20.dp,
//                    color = MaterialTheme.colorScheme.primary
//                ),
//            )
//    ) {
//        drawCircle(
//            color = Color.Magenta,
//            radius = 36f
//        )
//
//        drawImage(
//            image = item.icon,
//            topLeft = Offset(
//                center.x - (item.icon.width / 2),
//                center.y - (item.icon.width / 2)
//            )
//        )
//

    }




@Composable
fun Floatingbutton(
  //  onFloatingButtonStateChange: (FloatingButtonState) -> Unit
) {
    var floatingButtonState by remember {
        mutableStateOf(FloatingButtonState.Collapsed)
    }
//    val option = BitmapFactory.Options()
//    option.inPreferredConfig = Bitmap.Config.ARGB_8888
//    val bitmap = BitmapFactory.decodeResource(
//        LocalContext.current.resources,
//        R.drawable.btn_plus,
//        option
//    ).asImageBitmap()
    val items = listOf(

            MiniFABItem(
                icon = R.drawable.alert_dark_frame,
                label = "Accessories",
                identifier = "AccessoriesFab"
            )
        ,

            MiniFABItem(
                icon =  R.drawable.alert_dark_frame,
                label = "T-Shirt",
                identifier = "ShirtFab"
            )
        ,

            MiniFABItem(
                icon =  R.drawable.alert_dark_frame,
                //ImageBitmap.imageResource(id = R.drawable.ic_boy),
                label = "Shoes",
                identifier = "ShoesFab"
            )

    )
    val transition = updateTransition(targetState = floatingButtonState, label = "transition")
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == FloatingButtonState.Collapsed) 0f else 315f
    }
    Column {
        if (transition.currentState == FloatingButtonState.Expanded) {
            Log.i("menna","fe button")
            items.forEach {
                MiniFAB(item = it, onMiniFABClicked = {

                })
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        FloatingActionButton(
            onClick = {
              //  onFloatingButtonStateChange(
                    if (transition.currentState == FloatingButtonState.Collapsed) {
                       floatingButtonState = FloatingButtonState.Expanded
                    } else {
                        floatingButtonState=   FloatingButtonState.Collapsed

                    }
              //  )
            },
            modifier = Modifier
                .rotate(rotate)
                .padding(100.dp)
        ) {
Icon (
    tint = MaterialTheme.colorScheme.primary,
    imageVector =
        Icons.Default.Edit,
    contentDescription = null
)

        }
    }


}

@Preview
@Composable
fun MainFiltersPreview() {
    ShopifyTheme {


        Floatingbutton()

            }
        }



//SliderComponent({})
    //  MainFilters(items = mainCategories, onItemSelection = {})

