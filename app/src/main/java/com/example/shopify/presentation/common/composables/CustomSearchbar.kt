package com.example.shopify.presentation.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopify.R

@Composable
fun CustomSearchbar(
    searchText: String,
    onTextChange: (String) -> Unit,
    @StringRes hintText: Int,
    isSearching: Boolean,
    onCloseSearch: () -> Unit
) {

    TextField(
        shape = RoundedCornerShape(15.dp),
        value = searchText,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text(text = stringResource(id = hintText)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = stringResource(
                    id = R.string.search
                )
            )
        },
        trailingIcon = {
            if (isSearching) {
                Icon(
                    modifier = Modifier.clickable {
                        onCloseSearch()
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(
                        id = R.string.close
                    )
                )
            }

        }
    )
}