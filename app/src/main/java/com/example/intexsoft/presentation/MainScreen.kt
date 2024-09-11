package com.example.intexsoft.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.intexsoft.presentation.model.DVOItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val viewModel = MainActivityViewModel()

    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val items by viewModel.itemsList.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val images = listOf(
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/6.jpeg"
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                shape = CircleShape,
                containerColor = Color(0xFF4285F4),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "Show Stats")
            }
        }
    ) {

        if (
            showBottomSheet
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                content = {
                    BottomSheetContent(items = items)
                }
            )
        }

        Column {
            ImageSliderWithDots(
                images = images,
            )

            SearchBar(
                query = searchText,
                onQueryChange = viewModel::onSearchTextChange,
                onSearch = viewModel::onSearchTextChange,
                active = isSearching,
                onActiveChange = { viewModel.onToogleSearch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {}

            LazyColumn {
                items(
                    items = items,
                ) { item ->
                    ScrollListItem(
                        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        title = item.title,
                        subtitle = item.body,
                    )
                }
            }

        }
    }
}


@Composable
fun ImageSliderWithDots(images: List<String>) {

    val lazyListState = rememberLazyListState()

    val selectedIndex by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex
        }
    }

    val maxDots = 5
    val visibleDots = minOf(images.size, maxDots)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(images.size) { index ->
                Image(
                    painter = rememberAsyncImagePainter(images[index]),
                    contentDescription = "Image Slider",
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DotsIndicator(
            totalDots = images.size,
            selectedIndex = selectedIndex,
            maxVisibleDots = visibleDots
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    maxVisibleDots: Int
) {
    val dotsToShow = if (totalDots <= maxVisibleDots) {
        (0 until totalDots).toList()
    } else {
        val halfRange = maxVisibleDots / 2
        when {
            selectedIndex < halfRange -> (0 until maxVisibleDots).toList()
            selectedIndex > totalDots - halfRange -> (totalDots - maxVisibleDots until totalDots).toList()
            else -> (selectedIndex - halfRange until selectedIndex + halfRange + 1).toList()
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        dotsToShow.forEach { index ->
            Dot(isSelected = index == selectedIndex)
        }
    }
}

@Composable
fun Dot(isSelected: Boolean) {
    val size = if (isSelected) 10.dp else 8.dp
    val color = if (isSelected) Color.Blue else Color.Gray

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSlider() {
    val images = listOf(
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
        "https://rickandmortyapi.com/api/character/avatar/6.jpeg"
    )
    ImageSliderWithDots(images = images)
}

@Composable
fun BottomSheetContent(items: List<DVOItem>) {
    val itemCount = items.size
    val allCharacters = items.joinToString(separator = "") { it.title + it.body }
    val charStats = allCharacters.groupBy { it }
        .mapValues { it.value.size }
    val top3Chars = charStats.entries.sortedByDescending { it.value }.take(3)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Item Count: $itemCount")
        top3Chars.forEach { (char, count) ->
            Text(text = "$char = $count")
        }
    }
}

@Composable
fun ScrollListItem(
    imageUrl: String,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F5E9))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image section
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                color = Color.Black
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScrollListItem() {
    ScrollListItem(
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        title = "List item title",
        subtitle = "List item subtitle"
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}
