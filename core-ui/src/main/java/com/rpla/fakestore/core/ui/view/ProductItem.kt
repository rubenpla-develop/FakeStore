package com.rpla.fakestore.core.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.rpla.fakestore.core.ui.model.UiListItem
import com.rpla.fakestore.core.ui.theme.FakeStoreTheme
import com.rpla.fakestore.core.ui.view.testtags.TestTags.PRODUCT_FAVORITE_ICON
import com.rpla.fakestore.core.ui.view.testtags.TestTags.PRODUCT_ITEM
import com.rpla.fakestore.core.ui.view.testtags.TestTags.PRODUCT_NAME
import com.rpla.fakestore.core.ui.view.testtags.TestTags.PRODUCT_STATUS

@Composable
fun ProductItem(
    product: UiListItem,
    favoriteIconMode: FavoriteIconMode,
    onItemClick: () -> Unit,
    onFavoriteIconClicked: (id: Int) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        border = BorderStroke(2.dp, Color.DarkGray),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .height(200.dp)
                .fillMaxWidth()
                .clickable {
                    onItemClick()
                }.testTag(PRODUCT_ITEM),
    ) {
        ConstraintLayout {
            val (
                title, category, price, photo, spacerTop, spacerStart,
                spacerBottom, spacerEnd, favIcon,
            ) = createRefs()

            SubcomposeAsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(photo) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                loading = {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                error = {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )

            val isFilled =
                when (favoriteIconMode) {
                    FavoriteIconMode.Toggle -> product.isFavorite
                    FavoriteIconMode.RemoveOnly -> true
                }

            Box(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.DarkGray.copy(alpha = 0.75f))
                        .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(10.dp))
                        .constrainAs(favIcon) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                        }.testTag(PRODUCT_FAVORITE_ICON),
            ) {
                IconButton(onClick = { onFavoriteIconClicked(product.id) }) {
                    Icon(
                        imageVector = if (isFilled) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White,
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .constrainAs(spacerTop) {
                            top.linkTo(parent.top)
                        },
            )

            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .constrainAs(spacerBottom) {
                            bottom.linkTo(parent.bottom)
                        },
            )

            Spacer(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .constrainAs(spacerStart) {
                            start.linkTo(parent.start)
                        },
            )

            Spacer(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .constrainAs(spacerEnd) {
                            end.linkTo(parent.end)
                        },
            )

            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontSize = 10.sp,
                modifier =
                    Modifier
                        .background(Color.DarkGray, RoundedCornerShape(4.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(top = 1.dp, start = 3.dp, bottom = 1.dp, end = 3.dp)
                        .constrainAs(title) {
                            top.linkTo(spacerTop.bottom)
                            start.linkTo(spacerStart.end)
                            end.linkTo(spacerEnd.start)
                            width = Dimension.fillToConstraints
                        }.testTag(PRODUCT_NAME),
            )
            Text(
                text = product.price,
                color = Color.White,
                fontSize = 10.sp,
                modifier =
                    Modifier
                        .background(Color.DarkGray, RoundedCornerShape(4.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(top = 1.dp, start = 3.dp, bottom = 1.dp, end = 3.dp)
                        .constrainAs(price) {
                            bottom.linkTo(spacerBottom.top)
                            start.linkTo(spacerStart.end)
                        }.testTag(PRODUCT_STATUS),
            )

            Text(
                text = product.category,
                color = Color.White,
                fontSize = 10.sp,
                modifier =
                    Modifier
                        .background(Color.DarkGray, RoundedCornerShape(4.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(top = 1.dp, start = 3.dp, bottom = 1.dp, end = 3.dp)
                        .constrainAs(category) {
                            end.linkTo(spacerEnd.start)
                            bottom.linkTo(spacerBottom.top)
                        },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_ProductItem_Normal() {
    FakeStoreTheme {
        ProductItem(
            product =
                UiListItem(
                    id = 1,
                    title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
                    price = "64.00 €",
                    category = "electronics",
                    imageUrl = "https://via.placeholder.com/300",
                    isFavorite = false,
                ),
            onFavoriteIconClicked = { _ -> },
            favoriteIconMode = FavoriteIconMode.Toggle,
            onItemClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_ProductItem_Favorite() {
    FakeStoreTheme {
        ProductItem(
            product =
                UiListItem(
                    id = 2,
                    title = "White Gold Plated Princess",
                    price = "9.99 €",
                    category = "jewelery",
                    imageUrl = "https://via.placeholder.com/300",
                    isFavorite = true,
                ),
            onFavoriteIconClicked = { _ -> },
            favoriteIconMode = FavoriteIconMode.Toggle,
            onItemClick = {},
        )
    }
}
