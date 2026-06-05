package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalContext
import android.speech.tts.TextToSpeech
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.database.ChatMessageEntity
import com.example.data.database.FavoriteSpot
import com.example.data.database.AddaSessionEntity
import com.example.data.model.FoodTrail
import com.example.data.model.HeritageDish
import com.example.data.model.HeritageSpot
import com.example.ui.viewmodel.CulinaryViewModel
import com.example.ui.viewmodel.Screen
import com.example.ui.translation.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val isBengali by viewModel.isBengali.collectAsStateWithLifecycle()
 
    // Dialog state collectors
    val selectedSpot by viewModel.selectedSpot.collectAsStateWithLifecycle()
    val selectedDish by viewModel.selectedDish.collectAsStateWithLifecycle()
    val selectedTrail by viewModel.selectedTrail.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteSpotIdsSet.collectAsStateWithLifecycle()
 
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isBengali) "আহার ঐতিহ্য" else "Aahar Heritage",
                            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 22.sp
                        )
                        Text(
                            text = if (isBengali) "কলকাতার রন্ধনশৈলী সহায়িকা" else "Kolkata Culinary Concierge",
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "আ", // Bengali short vocal 'aa' for Aahar
                            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.toggleLanguage() }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = if (isBengali) "🌐 EN" else "🌐 বাংলা",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (currentScreen == Screen.AIConcierge) {
                            IconButton(onClick = { viewModel.clearChatHistory() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset Chat Memory",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            IconButton(onClick = { viewModel.navigateTo(Screen.Favorites) }) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "View Favorites",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = (currentScreen == Screen.Explore),
                    onClick = { viewModel.navigateTo(Screen.Explore) },
                    icon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Explore") },
                    label = { Text(LanguageTranslation.translate("Explore", isBengali), fontFamily = MaterialTheme.typography.labelSmall.fontFamily) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
 
                NavigationBarItem(
                    selected = (currentScreen == Screen.Trails),
                    onClick = { viewModel.navigateTo(Screen.Trails) },
                    icon = { Icon(imageVector = Icons.Default.Place, contentDescription = "Trails") },
                    label = { Text(LanguageTranslation.translate("Trails", isBengali), fontFamily = MaterialTheme.typography.labelSmall.fontFamily) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
 
                NavigationBarItem(
                    selected = (currentScreen == Screen.AIConcierge),
                    onClick = { viewModel.navigateTo(Screen.AIConcierge) },
                    icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Aahar AI") },
                    label = { Text(LanguageTranslation.translate("Aahar AI", isBengali), fontFamily = MaterialTheme.typography.labelSmall.fontFamily) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
 
                NavigationBarItem(
                    selected = (currentScreen == Screen.Favorites),
                    onClick = { viewModel.navigateTo(Screen.Favorites) },
                    icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text(LanguageTranslation.translate("Bookmarks", isBengali), fontFamily = MaterialTheme.typography.labelSmall.fontFamily) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Screen Transitions
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = spring()) togetherWith fadeOut(animationSpec = spring())
                },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.Explore -> ExploreScreen(
                        viewModel = viewModel,
                        favoriteIds = favoriteIds
                    )
                    Screen.Trails -> TrailsScreen(
                        viewModel = viewModel
                    )
                    Screen.AIConcierge -> ChatScreen(
                        viewModel = viewModel
                    )
                    Screen.Favorites -> FavoritesScreen(
                        viewModel = viewModel
                    )
                }
            }

            // --- Dialog Overlays ---
            selectedSpot?.let { spot ->
                SpotDetailsDialog(
                    spot = spot,
                    isFavorite = favoriteIds.contains(spot.id),
                    isBengali = isBengali,
                    onDismiss = { viewModel.selectSpot(null) },
                    onToggleFavorite = { viewModel.toggleFavorite(spot) },
                    onAskAahar = {
                        viewModel.selectSpot(null)
                        viewModel.navigateTo(Screen.AIConcierge)
                        viewModel.sendChatMessage("Namoshkar! Please tell me are there any rich stories, historical background or chef legends associated with the eatery '${spot.name}'? I want to connect with its genuine heritage.")
                    }
                )
            }
 
            selectedDish?.let { dish ->
                DishDetailsDialog(
                    dish = dish,
                    isBengali = isBengali,
                    onDismiss = { viewModel.selectDish(null) },
                    onAskAahar = {
                        viewModel.selectDish(null)
                        viewModel.navigateTo(Screen.AIConcierge)
                        viewModel.sendChatMessage("Greetings Aahar! Please tell me the historical narrative of the dish '${dish.name}'. Walk me through the era '${dish.era}' and any stories of its generational cooks in Kolkata.")
                    }
                )
            }

            selectedTrail?.let { trail ->
                TrailDetailsDialog(
                    trail = trail,
                    viewModel = viewModel,
                    onDismiss = { viewModel.selectTrail(null) },
                    onAskAahar = {
                        viewModel.selectTrail(null)
                        viewModel.navigateTo(Screen.AIConcierge)
                        viewModel.sendChatMessage("Dear Aahar, I want to take a closer look at the food trail: '${trail.name}'. Please customized a walking route, telling me of its theme '${trail.theme}' and advising optimal timings.")
                    }
                )
            }
        }
    }
}

// ==========================================
// 1. EXPLORE SCREEN
// ==========================================
@Composable
fun ExploreScreen(
    viewModel: CulinaryViewModel,
    favoriteIds: Set<String>,
    modifier: Modifier = Modifier
) {
    var selectedCategoryTab by remember { mutableStateOf(0) } // 0 = Dish, 1 = Eatery, 2 = Adda Map
    val listState = rememberLazyListState()
 
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredDishes by viewModel.filteredDishes.collectAsStateWithLifecycle()
    val filteredSpots by viewModel.filteredSpots.collectAsStateWithLifecycle()
    val isBengali by viewModel.isBengali.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearchQueries.collectAsStateWithLifecycle()
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
 
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Story Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = LanguageTranslation.translate("Savor Kolkata's History", isBengali),
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = LanguageTranslation.translate("From high colonial cabins where secret freedom movements were planned over lacy egg cutlets, to 19th-century sugar renaissance that spawned the Rossogolla. Trace the stories of generational cooks in Bengal's unhurried 'Adda' era.", isBengali),
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable { viewModel.navigateTo(Screen.AIConcierge) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Chat",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = LanguageTranslation.translate("Ask Concierge Aahar", isBengali),
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
 
        // Search Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = LanguageTranslation.translate("🔎 Search Neural Archives and Eateries", isBengali),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text(LanguageTranslation.translate("Search eateries, dishes, or eras (e.g. Mughal, Colonial, Sweets)", isBengali), fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isSearchFocused = it.isFocused }
                            .testTag("historical_search_input"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { 
                                        viewModel.setSearchQuery("") 
                                    },
                                    modifier = Modifier.testTag("clear_search_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchQuery.isNotBlank()) {
                                    viewModel.saveSearchQuery(searchQuery)
                                }
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                        )
                    )
                    
                    // Floating/Inline recent searches block dropdown style
                    AnimatedVisibility(
                        visible = isSearchFocused,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("recent_searches_panel"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = LanguageTranslation.translate("Recent Searches", isBengali),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (recentSearches.isNotEmpty()) {
                                        TextButton(
                                            onClick = { viewModel.clearSearchHistory() },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp).testTag("clear_recent_searches_btn")
                                        ) {
                                            Text(
                                                text = LanguageTranslation.translate("Clear All", isBengali),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }

                                if (recentSearches.isEmpty()) {
                                    Text(
                                        text = LanguageTranslation.translate("No recent searches found.", isBengali),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Column {
                                        recentSearches.forEach { searchEntity ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        viewModel.setSearchQuery(searchEntity.queryText)
                                                        viewModel.saveSearchQuery(searchEntity.queryText)
                                                        focusManager.clearFocus()
                                                        keyboardController?.hide()
                                                    }
                                                    .padding(vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Search,
                                                        contentDescription = "Recent query",
                                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = searchEntity.queryText,
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                IconButton(
                                                    onClick = { viewModel.deleteSearchQuery(searchEntity.queryText) },
                                                    modifier = Modifier.size(24.dp).testTag("delete_recent_search_${searchEntity.queryText}")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Delete search record",
                                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                TextButton(
                                    onClick = { focusManager.clearFocus() },
                                    modifier = Modifier.align(Alignment.End).height(28.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isBengali) "বন্ধ করুন" else "Close",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Suggested Search Chips
                    Text(
                        text = LanguageTranslation.translate("Suggested Historical Queries / Eateries:", isBengali),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val suggestions = listOf("Mughal", "Colonial", "Sweets", "North Kolkata", "Anglo-Indian")
                        items(suggestions.size) { index ->
                            val s = suggestions[index]
                            val isSelected = searchQuery.equals(s, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                    .clickable {
                                        if (isSelected) {
                                            viewModel.setSearchQuery("")
                                        } else {
                                            viewModel.setSearchQuery(s)
                                            viewModel.saveSearchQuery(s)
                                        }
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("suggested_chip_$s")
                            ) {
                                Text(
                                    text = LanguageTranslation.translate(s, isBengali),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Hub
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryTab == 0 && searchQuery.isBlank(),
                        onClick = { 
                            selectedCategoryTab = 0 
                            viewModel.setSearchQuery("")
                        },
                        label = { Text(LanguageTranslation.translate("Legendary Dishes", isBengali), fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategoryTab == 1 && searchQuery.isBlank(),
                        onClick = { 
                            selectedCategoryTab = 1 
                            viewModel.setSearchQuery("")
                        },
                        label = { Text(LanguageTranslation.translate("Heritage Eateries", isBengali), fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategoryTab == 2 && searchQuery.isBlank(),
                        onClick = { 
                            selectedCategoryTab = 2 
                            viewModel.setSearchQuery("")
                        },
                        label = { Text(LanguageTranslation.translate("🗺️ 'Adda' Map", isBengali), fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategoryTab == 3 && searchQuery.isBlank(),
                        onClick = { 
                            selectedCategoryTab = 3 
                            viewModel.setSearchQuery("")
                        },
                        label = { Text(LanguageTranslation.translate("📷 AI Archival Gallery", isBengali), fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategoryTab == 4 && searchQuery.isBlank(),
                        onClick = { 
                            selectedCategoryTab = 4 
                            viewModel.setSearchQuery("")
                        },
                        label = { Text(LanguageTranslation.translate("🎭 Adda Moods", isBengali), fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
 
        // Dynamic Lists / Unified Search
        if (searchQuery.isNotBlank()) {
            if (filteredDishes.isEmpty() && filteredSpots.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🔍",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = LanguageTranslation.translate("No matching legendary dishes or eateries found", isBengali),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = LanguageTranslation.translate("Try searching for 'Mughal', 'Sweets', 'Colonial' or specific locations.", isBengali),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                if (filteredDishes.isNotEmpty()) {
                    item {
                        Text(
                            text = LanguageTranslation.translate("Legendary Dishes", isBengali),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }
                    items(filteredDishes) { dish ->
                        DishItemRow(
                            dish = dish,
                            isBengali = isBengali,
                            onClick = { viewModel.selectDish(dish) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (filteredSpots.isNotEmpty()) {
                    item {
                        Text(
                            text = LanguageTranslation.translate("Heritage Eateries", isBengali),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }
                    items(filteredSpots) { spot ->
                        SpotItemRow(
                            spot = spot,
                            isFavorite = favoriteIds.contains(spot.id),
                            isBengali = isBengali,
                            onClick = { viewModel.selectSpot(spot) },
                            onToggleFavorite = { viewModel.toggleFavorite(spot) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        } else {
            // When not searching, normal tab content is shown
            if (selectedCategoryTab == 0) {
                if (filteredDishes.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🍽️",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = LanguageTranslation.translate("No matching legendary dishes found", isBengali),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = LanguageTranslation.translate("Try searching for 'Mughal', 'Sweets' or 'Colonial'.", isBengali),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredDishes) { dish ->
                        DishItemRow(
                            dish = dish,
                            isBengali = isBengali,
                            onClick = { viewModel.selectDish(dish) }
                        )
                    }
                }
            } else if (selectedCategoryTab == 1) {
                if (filteredSpots.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🏪",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = LanguageTranslation.translate("No matching eateries found", isBengali),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = LanguageTranslation.translate("Try searching for 'College Street', 'North' or 'Chitpur'.", isBengali),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredSpots) { spot ->
                        SpotItemRow(
                            spot = spot,
                            isFavorite = favoriteIds.contains(spot.id),
                            isBengali = isBengali,
                            onClick = { viewModel.selectSpot(spot) },
                            onToggleFavorite = { viewModel.toggleFavorite(spot) }
                        )
                    }
                }
            } else if (selectedCategoryTab == 2) {
                item {
                    InteractiveAddaMap(viewModel = viewModel)
                }
            } else if (selectedCategoryTab == 3) {
                item {
                    AIArchivalGallery(viewModel = viewModel)
                }
            } else if (selectedCategoryTab == 4) {
                item {
                    AddaMoodTrackerScreen(viewModel = viewModel)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DishItemRow(
    dish: HeritageDish,
    isBengali: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.localizedName(isBengali),
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
 
            Spacer(modifier = Modifier.width(16.dp))
 
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dish.localizedName(isBengali),
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 17.sp
                )
                Text(
                    text = dish.localizedEra(isBengali),
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dish.localizedStory(isBengali),
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
 
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
 
@Composable
fun SpotItemRow(
    spot: HeritageSpot,
    isFavorite: Boolean,
    isBengali: Boolean = false,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = spot.imageUrl,
                contentDescription = spot.localizedName(isBengali),
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
 
            Spacer(modifier = Modifier.width(16.dp))
 
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spot.localizedName(isBengali),
                        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isBengali) "স্থাপিত: ${spot.established}" else "Est. ${spot.established}",
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = spot.localizedLocation(isBengali),
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = spot.localizedDescription(isBengali),
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
 
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ==========================================
// 2. FOOD TRAILS SCREEN
// ==========================================
@Composable
fun TrailsScreen(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Historical Trail Walk",
                    fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Pick a thematic walking tour curated carefully to showcase Wajid Ali Shah's exile remnants, old printing clubs, or Chinese diaspora soup bowls.",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        items(viewModel.trails) { trail ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTrail(trail) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trail.theme,
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Time",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = trail.estimatedTime,
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = trail.name,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = trail.description,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    Spacer(modifier = Modifier.height(12.dp))

                    // Footprint preview stops
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Stops:",
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        trail.stops.forEach { stopId ->
                            val stopSpot = viewModel.spots.find { it.id == stopId }
                            if (stopSpot != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = stopSpot.name,
                                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// ==========================================
// 3. AI CHAT SCREEN (CONCIERGE STORYTELLER)
// ==========================================
@Composable
fun ChatScreen(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    
    var currentTxt by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // AutoScroll to bottom when message arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Chat Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Empty state or suggestion chips
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚜️",
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "I am Aahar, culinary teller of old stories.",
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ask me how potatoes arrived in Kolkata Royal Biryani, where to find genuine Shovabazar Kobiraji cutlets, or customized trail variants.",
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Suggestion Chip Row
                    Text(
                        text = "Touch to prompt your storyteller:",
                        fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val promptChips = listOf(
                        "Colonial Delicacies 🌍" to "As the Aahar Culinary Concierge, create a narrative exploring 'Colonial Era Delicacies' in Kolkata. Discuss how British, Portuguese, and other colonial influences shaped Bengali cuisine. Recommend 2-3 heritage restaurants in Kolkata known for preserving and serving dishes from this era, providing a rich, story-driven description of their significance.",
                        "Secrets of Sweets ⚜️" to "As the Aahar Culinary Concierge, delve into the 'Secrets of Sweets' in Kolkata. Choose a classic Bengali sweet (e.g., Sandesh, Rasmalai, or Chom Chom) and share its origin story, the generational techniques involved in its preparation, and recommend a specific, historic sweet shop in Kolkata where it can be savored. Use a warm, proud, and storytelling tone.",
                        "Rossogolla Story" to "Tell me the sweet science of Nobin Das's original 1868 Rossogolla and how lady canning got a sweet named after her.",
                        "Exile Biryani" to "Why does Kolkata Biryani have potato? Tell me of Nawab Wajid Ali Shah's royal exile to Metiabruz.",
                        "North Cabin Adda" to "What was the 'Cabin Culture' in historic North Kolkata cafes? Why did secret freedom clubs gather there?",
                        "Hakka Roots" to "Tell me the story of the Hakka Chinese community settling in Tiretta Bazaar. What are the genuine spots?"
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(promptChips) { (title, query) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.sendSeedPrompt(query) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "✨",
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = title,
                                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(messages) { message ->
                        ChatBubbleRow(message = message)
                    }

                    if (isGenerating) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Aahar is drawing from historical archives...",
                                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = currentTxt,
                onValueChange = { currentTxt = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Ask of history, cabin recipe or spots...",
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (currentTxt.isNotBlank()) {
                            viewModel.sendChatMessage(currentTxt)
                            currentTxt = ""
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
                ),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                maxLines = 3
            )

            FloatingActionButton(
                onClick = {
                    if (currentTxt.isNotBlank()) {
                        viewModel.sendChatMessage(currentTxt)
                        currentTxt = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Message",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubbleRow(message: ChatMessageEntity) {
    val isUser = message.sender == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 16.dp
                    )
                )
                .background(
                    if (isUser) MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 1.dp,
                    color = if (isUser) Color.Transparent else MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.08f
                    ),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 16.dp
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = if (isUser) "You" else "⚜️ Aahar, Culinary Concierge",
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    color = if (isUser) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Text(
                text = message.text,
                fontFamily = if (isUser) MaterialTheme.typography.bodyMedium.fontFamily else MaterialTheme.typography.bodyLarge.fontFamily,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

// ==========================================
// 4. FAVORITES SCREEN
// ==========================================
@Composable
fun FavoritesScreen(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favoriteSpots.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "My Heritage Bookmarks",
                    fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Your personalized collection of historic cabins and spice counters booked for your physical tour.",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        if (favorites.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🪔",
                        fontSize = 44.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No saved legacy spaces",
                        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Browse the Explore tab and touch the heart of any eatery to bookmark it here.",
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            items(favorites) { fav ->
                // Map FavoriteSpot to HeritageSpot to support dialogue reuse
                val mappedSpot = HeritageSpot(
                    id = fav.id,
                    name = fav.name,
                    description = fav.description,
                    established = fav.era,
                    iconicFor = fav.iconicFor,
                    location = fav.location,
                    trivia = "",
                    imageUrl = fav.imageUrl
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectSpot(mappedSpot) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = fav.imageUrl,
                            contentDescription = fav.name,
                            modifier = Modifier
                                .size(75.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = fav.name,
                                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp
                            )
                            Text(
                                text = fav.location,
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Iconic: ${fav.iconicFor}",
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        IconButton(onClick = { viewModel.toggleFavorite(mappedSpot) }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Unfavorite",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// ==========================================
// 5. DETAIL DIALOGS
// ==========================================

@Composable
fun SpotDetailsDialog(
    spot: HeritageSpot,
    isFavorite: Boolean,
    isBengali: Boolean = false,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAskAahar: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    AsyncImage(
                        model = spot.imageUrl,
                        contentDescription = spot.localizedName(isBengali),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
 
                    // Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )
 
                    // Close, Fav
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.4f))
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
 
                        IconButton(
                            onClick = onToggleFavorite,
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.4f))
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle Favorite",
                                tint = if (isFavorite) MaterialTheme.colorScheme.secondary else Color.White
                            )
                        }
                    }
 
                    // Floating spot name
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = spot.localizedName(isBengali),
                            fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                        Text(
                            text = if (isBengali) "স্থাপিত: ${spot.established}" else "Established in ${spot.established}",
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
 
                // Core details content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = LanguageTranslation.translate("LOCATION", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = spot.localizedLocation(isBengali),
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
 
                    item {
                        Column {
                            Text(
                                text = LanguageTranslation.translate("CRITICAL SPECIALTIES", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = spot.localizedIconicFor(isBengali),
                                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
 
                    item {
                        Column {
                            Text(
                                text = LanguageTranslation.translate("CULTURAL NARRATIVE", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = spot.localizedDescription(isBengali),
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
 
                    if (spot.trivia.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = LanguageTranslation.translate("Historic Trivia", isBengali),
                                        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = spot.localizedTrivia(isBengali),
                                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
 
                // CTA action
                Surface(
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Button(
                        onClick = onAskAahar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(LanguageTranslation.translate("Connect with Aahar AI for Stories ⚜️", isBengali), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
 
@Composable
fun DishDetailsDialog(
    dish: HeritageDish,
    isBengali: Boolean = false,
    onDismiss: () -> Unit,
    onAskAahar: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    AsyncImage(
                        model = dish.imageUrl,
                        contentDescription = dish.localizedName(isBengali),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
 
                    // Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )
 
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(12.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.4f))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
 
                    // Floating dish name
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = dish.localizedName(isBengali),
                            fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 24.sp
                        )
                        Text(
                            text = dish.localizedEra(isBengali),
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
 
                // Core details content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = LanguageTranslation.translate("HISTORICAL ORIGIN", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = dish.localizedStory(isBengali),
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 21.sp
                            )
                        }
                    }
 
                    item {
                        Column {
                            Text(
                                text = LanguageTranslation.translate("AUTHENTIC HERITAGE SPOTS TO ENJOY THIS", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            dish.keyEateries.forEach { eatery ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Place,
                                            contentDescription = "Spot",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = LanguageTranslation.translate(eatery, isBengali),
                                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
 
                // CTA action
                Surface(
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Button(
                        onClick = onAskAahar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(LanguageTranslation.translate("Connect with Aahar AI for Stories ⚜️", isBengali), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TrailDetailsDialog(
    trail: FoodTrail,
    viewModel: CulinaryViewModel,
    onDismiss: () -> Unit,
    onAskAahar: () -> Unit
) {
    val isBengali by viewModel.isBengali.collectAsStateWithLifecycle()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Title Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = LanguageTranslation.translate("WALK THE TIMELINE", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = onDismiss,
                                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.15f)),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = trail.localizedName(isBengali),
                            fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${LanguageTranslation.translate(trail.estimatedTime, isBengali)} • ${trail.localizedTheme(isBengali)}",
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Scrollable trail details
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = LanguageTranslation.translate("HISTORICAL LEGACY backstory", isBengali),
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = trail.localizedBackstory(isBengali),
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    item {
                        Text(
                            text = LanguageTranslation.translate("TRAIL TIMELINE STOPS", isBengali),
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Loop stops
                    items(trail.stops) { stopId ->
                        val spot = viewModel.spots.find { it.id == stopId }
                        if (spot != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectSpot(spot)
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    ) {
                                        AsyncImage(
                                            model = spot.imageUrl,
                                            contentDescription = spot.localizedName(isBengali),
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = spot.localizedName(isBengali),
                                            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = spot.localizedLocation(isBengali),
                                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${LanguageTranslation.translate("Specialty", isBengali)}: ${spot.localizedIconicFor(isBengali)}",
                                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // CTA action
                Surface(
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Button(
                        onClick = onAskAahar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(LanguageTranslation.translate("Ask Aahar to Tailor Walk Details ⚜️", isBengali), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. INTERACTIVE 'ADDA' MAP COMPONENTS
// ==========================================

data class AddaHotspot(
    val id: String,
    val name: String,
    val establishment: String,
    val era: String,
    val badge: String,
    val coordinates: Pair<Float, Float>,
    val historicalSignificance: String,
    val specialty: String,
    val trivia: String
)

fun biasAlignment(xPercent: Float, yPercent: Float): androidx.compose.ui.BiasAlignment {
    return androidx.compose.ui.BiasAlignment(
        horizontalBias = (xPercent * 2f) - 1f,
        verticalBias = (yPercent * 2f) - 1f
    )
}

@Composable
fun InteractiveAddaMap(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    val hotspots = remember {
        listOf(
            AddaHotspot(
                id = "coffee_house",
                name = "Indian Coffee House",
                establishment = "1876 / 1942",
                era = "Satyajit Ray & Sunil Gangopadhyay Era",
                badge = "☕ Intellectual Epicenter",
                coordinates = Pair(0.58f, 0.40f),
                historicalSignificance = "The absolute high-altar of Bengali intellectual 'Adda'. Famed as the meeting grounds for filmmakers, poets, and student organizers. It was here that Satyajit Ray debated set designs, Sunil Gangopadhyay plotted the Krittibas poetry movement, and Manna Dey composed the immortal anthem of longing, 'Coffee Houser Sei Addata'.",
                specialty = "Infusion (Black Coffee) & Mutton Kabiraji Cutlet",
                trivia = "Originally founded as Albert Hall in 1876, it was rebranded as Coffee House in 1942 and run by a cooperative of its workers after a historic strike."
            ),
            AddaHotspot(
                id = "mitra_cafe",
                name = "Mitra Cafe",
                establishment = "1920",
                era = "Classical North-Kolkata Cabin Culture",
                badge = "🚪 Cabins of Camaraderie",
                coordinates = Pair(0.42f, 0.18f),
                historicalSignificance = "Mitra Cafe represents the peak of 'Para' (neighborhood) camaraderie. High wooden private cabins allowed actors, revolutionary thinkers, and sports enthusiasts to sit for hours discussing cinema, football, and revolutionary ideas. Its crisp deep-fried snacks became legendary fuel for these long, intellectual evenings.",
                specialty = "Diamond Fish Fry, Brain Chop, Mutton Kabiraji",
                trivia = "The word 'Mitra' means friend, and its founder, Sushil Roy, chose the name because he wanted the eatery to feel like a friend's open home."
            ),
            AddaHotspot(
                id = "paramount",
                name = "Paramount Sherbets",
                establishment = "1918",
                era = "Secret Nationalist Underground",
                badge = "🍋 Revolutionary Haven",
                coordinates = Pair(0.68f, 0.38f),
                historicalSignificance = "Paramount was founded by chemical scientist and Swadeshi activist Nihar Ranjan Majumdar. Behind the cover of serving cooling sherbets, the shop hosted secret cells of the revolutionary Anushilan Samiti. Heavy discussions on self-reliance and anti-colonial strategies were forged here over refreshing local beverages.",
                specialty = "Daab Sherbet (Green Coconut & Ice) & Cocoa Malai",
                trivia = "Acharya Prafulla Chandra Ray (father of Indian chemistry) helped formulate the famous 'Daab Sherbet' recipe to keep underground nationalists hydrated and fit."
            ),
            AddaHotspot(
                id = "dilkhusha",
                name = "Dilkhusha Cabin",
                establishment = "1907",
                era = "Pre-Independence Nationalist Hub",
                badge = "🎭 Rebel Retreat",
                coordinates = Pair(0.55f, 0.48f),
                historicalSignificance = "Frequented by iconic rebel poet Kazi Nazrul Islam and freedom fighter Ganesh Ghosh. It provided a key hideout for nationalists. The cabin structure allowed political rebels to slide curtains shut and conduct high-risk strategy planning away from the watchful eyes of colonial police under the guise of an evening snacking 'adda'.",
                specialty = "Mince-meat egg Kabiraji cutlet",
                trivia = "The term 'Kabiraji' is a Bengali corruption of 'Coverage', referring to the lacy web of beaten egg that completely covers the inner mutton cutlet."
            ),
            AddaHotspot(
                id = "chitto_babu",
                name = "Chitto Babur Dokan",
                establishment = "1940s",
                era = "Peoples' Working Class Hub",
                badge = "🥘 Proletariat Pulpit",
                coordinates = Pair(0.32f, 0.59f),
                historicalSignificance = "Dacres Lane houses Chitto Babur Dokan, which remains the energetic heartbeat of clerical, administrative, and working-class 'Adda'. Journalists, writers, and office-goers gather on benches in the narrow, smoky lane, drinking tea and arguing about trade unions, global politics, and cricket.",
                specialty = "Legendary Chicken Stew & Thick Butter Toast",
                trivia = "Dacres Lane was originally laid out by Philip Milner Dacres in the 18th century as a residential bypass which later became the food capital of the common man."
            )
        )
    }

    var selectedHotspot by remember { mutableStateOf(hotspots[0]) }
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseFloat"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlphaFloat"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Explanatory card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "What is Bengali 'Adda'?",
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Adda is a beloved Bengali socio-cultural ritual of unhurried, highly-charged group conversation about literature, art, politics, or sport. These 5 legendary coordinates was where Kolkata's modern history, rebellion plans, and artistic waves were forged over cups of tea and crispy cutlets.",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Interactive Adda Hotspots Map",
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp
        )
        Text(
            text = "Tap on map markers or hotspots below to explore their rich stories.",
            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Map Canvas Box
        val primaryColor = MaterialTheme.colorScheme.primary
        val secondaryColor = MaterialTheme.colorScheme.secondary
        val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)), RoundedCornerShape(16.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { 
                        // Enabled clickable layer
                    }
            ) {
                val width = size.width
                val height = size.height

                // Draw the Hooghly River flowing vertically along the west (left)
                val riverPath = Path().apply {
                    moveTo(0.12f * width, 0f)
                    cubicTo(
                        0.18f * width, 0.25f * height,
                        0.05f * width, 0.50f * height,
                        0.15f * width, 1.0f * height
                    )
                }
                drawPath(
                    path = riverPath,
                    color = Color(0xFF4A90E2).copy(alpha = 0.35f),
                    style = Stroke(width = 24f)
                )

                // Draw major connector routes (Stylized dotted lines)
                // Howrah Bridge (North-Central)
                drawLine(
                    color = primaryColor.copy(alpha = 0.25f),
                    start = Offset(0.12f * width, 0.3f * height),
                    end = Offset(0.50f * width, 0.35f * height),
                    strokeWidth = 4f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )

                // Second Hooghly Bridge (Vidyasagar Setu) (South-West)
                drawLine(
                    color = primaryColor.copy(alpha = 0.25f),
                    start = Offset(0.12f * width, 0.7f * height),
                    end = Offset(0.35f * width, 0.75f * height),
                    strokeWidth = 4f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )

                // Draw lines connecting college street hub hotspots to emphasize the main street Corridor
                // ICH, Paramount, Dilkhusha
                drawLine(
                    color = secondaryColor.copy(alpha = 0.2f),
                    start = Offset(0.58f * width, 0.40f * height),
                    end = Offset(0.68f * width, 0.38f * height),
                    strokeWidth = 5f
                )
                drawLine(
                    color = secondaryColor.copy(alpha = 0.2f),
                    start = Offset(0.58f * width, 0.40f * height),
                    end = Offset(0.55f * width, 0.48f * height),
                    strokeWidth = 5f
                )

                // Draw nodes
                hotspots.forEach { hotspot ->
                    val x = hotspot.coordinates.first * width
                    val y = hotspot.coordinates.second * height
                    val isSelected = hotspot.id == selectedHotspot.id

                    // Pulse effect for active selection
                    if (isSelected) {
                        drawCircle(
                            color = primaryColor.copy(alpha = pulseAlpha),
                            radius = pulseSize * 2f,
                            center = Offset(x, y)
                        )
                    }

                    // Base circle
                    drawCircle(
                        color = if (isSelected) primaryColor else secondaryColor.copy(alpha = 0.8f),
                        radius = if (isSelected) 10f else 7f,
                        center = Offset(x, y)
                    )

                    // Inner core
                    drawCircle(
                        color = Color.White,
                        radius = 3f,
                        center = Offset(x, y)
                    )

                    // Small indicator ring
                    drawCircle(
                        color = if (isSelected) primaryColor else secondaryColor,
                        radius = if (isSelected) 16f else 11f,
                        center = Offset(x, y),
                        style = Stroke(width = 2f)
                    )
                }
            }

            // Floating touch zones inside the map box using Box relative offsets
            hotspots.forEach { hotspot ->
                val xPercent = hotspot.coordinates.first
                val yPercent = hotspot.coordinates.second

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .align(
                                biasAlignment(
                                    xPercent = xPercent,
                                    yPercent = yPercent
                                )
                            )
                            .size(48.dp)
                            .testTag("map_marker_${hotspot.id}")
                            .clickable {
                                selectedHotspot = hotspot
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Empty container centered at the alignment bias coordinate
                    }
                }
            }

            // Map overlay labels
            Text(
                text = "Hooghly River  ⛵",
                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                fontSize = 9.sp,
                color = Color(0xFF4A90E2).copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            )

            Text(
                text = "Howrah Bridge",
                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                fontSize = 8.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .padding(top = 60.dp, start = 50.dp)
            )

            // Dynamic card showing the currently hovered/selected spot's title
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Selected Spot",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = selectedHotspot.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal clickable chips for selecting hotspots so users have a list alternative to touching the map
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hotspots) { spot ->
                val isSelected = spot.id == selectedHotspot.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                        .clickable { selectedHotspot = spot }
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = spot.name,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = MaterialTheme.typography.labelSmall.fontFamily
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Detail Card for the Active Hotspot
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("adda_details_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header badge row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = selectedHotspot.badge,
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Est. ${selectedHotspot.establishment}",
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = selectedHotspot.name,
                    fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )

                Text(
                    text = selectedHotspot.era,
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "HISTORICAL SIGNIFICANCE",
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = selectedHotspot.historicalSignificance,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 19.sp,
                    fontSize = 13.5.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Specialty column
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Specialty",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "UNMISSABLE SPECIALTY",
                            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedHotspot.specialty,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Trivia
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "💡 INSIDER ANECDOTE",
                        fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "The ${selectedHotspot.name} houses unique history: ${selectedHotspot.trivia}",
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Short Audio Stories Narrated by Local Voices
                AddaAudioStoryPlayer(selectedHotspot = selectedHotspot)

                Spacer(modifier = Modifier.height(16.dp))

                // Concierge CTA button
                Button(
                    onClick = {
                        viewModel.navigateTo(Screen.AIConcierge)
                        viewModel.sendChatMessage("Greetings, Concierge Aahar! I am deeply fascinated by the legendary Kolkata 'Adda' culture. Please tell me more about '${selectedHotspot.name}' established in ${selectedHotspot.establishment}. Share any culinary folklore, specific chairs or tables where artists sat, and details of their iconic menu like the ${selectedHotspot.specialty}.")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Ask",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Deep-Dive with Aahar AI ⚜️",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        fontFamily = MaterialTheme.typography.labelSmall.fontFamily
                    )
                }
            }
        }
    }
}

data class AudioStoryVoice(
    val name: String,
    val role: String,
    val text: String
)

@Composable
fun AddaAudioStoryPlayer(
    selectedHotspot: AddaHotspot,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var selectedVoiceIndex by remember { mutableStateOf(0) }
    
    val voiceOptions = remember(selectedHotspot.id) {
        when (selectedHotspot.id) {
            "coffee_house" -> listOf(
                AudioStoryVoice(
                    name = "Prabir-babu",
                    role = "Retired College Street Academic",
                    text = "Aah, the aroma of boiling Infusion... listen closely. Can you hear the clink of porcelain cups? This corner table is where Satyajit Ray sketched 'Pather Panchali' scenes on paper napkins. We sat here for seven hours over a single ninety-paise cup of coffee, arguing of Godard and French New Wave, until our voices were hoarse and the stars rose over College Street..."
                ),
                AudioStoryVoice(
                    name = "Rupam",
                    role = "College Street Indie Musician",
                    text = "This hall is our temple! When Manna Dey sang of the lost addas here, he wasn't just singing a song; he was reading our collective diary. The smoke, the infusion, the cold mutton cutlets, and the heated debates across broken wooden tables... that's where the soul of Kolkata poetry is born daily."
                )
            )
            "mitra_cafe" -> listOf(
                AudioStoryVoice(
                    name = "Sushil-da",
                    role = "Proprietor & Master Confectioner",
                    text = "Slide the heavy curtain of this cabin shut, my friend. Here, the diamond-cut fish fry is fried to a precise, heavy gold. In the 1930s, this cabin was where freedom fighters nested under the cover of dense frying smoke, sliding pamphlets into their hands. The crispiness is our family's oath; the crust must crisp in your mouth, but the fish inside must melt like butter."
                ),
                AudioStoryVoice(
                    name = "Tathagata",
                    role = "Kolkata Theatre Director",
                    text = "After a four-hour intense play rehearsal in Shyambazar, we would pack ourselves in these tiny wood cabins of Mitra Cafe. Between bites of Brain Chop and mutton gravy, we'd draft the next street theater scripts. This cabin has heard more revolutionary monologues than any grand hall."
                )
            )
            "paramount" -> listOf(
                AudioStoryVoice(
                    name = "Haran-babu",
                    role = "Senior Sherbet Master",
                    text = "Feel the chill of the Daab Sherbet glass. This recipe was formulated by Acharya Prafulla Chandra Ray himself to keep our underground Swadeshi cadets hydrated and sharp. In these wooden booths, under the heavy stuffed animal heads on the walls, young rebels would meet, whisper codes of revolution, and down a sweet coconut nectar before escaping into the crowded tram lanes."
                ),
                AudioStoryVoice(
                    name = "Anjali",
                    role = "Historian & Barrio Resident",
                    text = "Step in and look at the dark wood paneling. This sherbet house survived world wars and independence riots. To taste our cold rose-cocoa malai syrup is to drink a liquid history of the Swadeshi movement. Every spoonful is a sweet, cool act of remembrance."
                )
            )
            "dilkhusha" -> listOf(
                AudioStoryVoice(
                    name = "Swapan",
                    role = "Senior Cabin Master",
                    text = "The term 'Kabiraji' is a celebration of custom! Our rebel poet Kazi Nazrul Islam sat right on this wooden bench, drumming his restless fingers on the table while humming a new raga. We would rush to serve him his mutton cutlet wrapped in the golden, lacy net of egg. He said the mesh was a crown for a rebel's snack!"
                ),
                AudioStoryVoice(
                    name = "Kunal",
                    role = "Presidency College Alumnus",
                    text = "Dilkhusha has that damp, old-world charm that binds you. We'd sit with our closed curtains, feeling like spies, debating political economics while peeling off the lacy egg layer of the cutlet. It's a sanctuary where time stands still."
                )
            )
            "chitto_babu" -> listOf(
                AudioStoryVoice(
                    name = "Joydeb",
                    role = "Generational Stew Cook",
                    text = "Watch the steam rising from this heavy metal bowl of chicken stew. Since the 1940s, we have boiled this light, healing broth on coal ovens. Clerks, barristers, and street sweepers sit shoulder-to-shoulder on these long wooden benches. Here, we don't care about rank; we discuss the football derby with high passion over buttery thick toasts."
                ),
                AudioStoryVoice(
                    name = "Monojit",
                    role = "High Court Clerk",
                    text = "Every single afternoon at 1 PM, Dacres Lane turns into a high-energy theater of food. You stand on the narrow path with your plate of hot chicken stew and slice of buttery toast, shouting sports opinions over the clink of teacups. It is the fuel of the working middle class."
                )
            )
            else -> listOf(
                AudioStoryVoice(
                    name = "Local Voice",
                    role = "Kolkata Resident",
                    text = "Kolkata's street food is a living archive, telling a story of migration, survival, and royal tastes. Pull up a chair and listen to the murmurs of the city."
                )
            )
        }
    }

    // Reset voice selection and stop playing when hotspot changes
    LaunchedEffect(selectedHotspot.id) {
        selectedVoiceIndex = 0
        isPlaying = false
    }

    val activeVoice = voiceOptions.getOrNull(selectedVoiceIndex) ?: voiceOptions[0]

    // Initialize Text-to-Speech safely
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    val mainHandler = remember { android.os.Handler(android.os.Looper.getMainLooper()) }

    DisposableEffect(context) {
        val tempTts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
        tts = tempTts
        onDispose {
            tempTts.stop()
            tempTts.shutdown()
        }
    }

    // Register progress listener to auto-stop visual player when narration finishes
    LaunchedEffect(tts, ttsReady) {
        if (ttsReady && tts != null) {
            tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    mainHandler.post { isPlaying = false }
                }
                override fun onError(utteranceId: String?) {
                    mainHandler.post { isPlaying = false }
                }
            })
        }
    }

    // Pause / Stop TTS if composition leaves or hotspot updates
    LaunchedEffect(isPlaying, activeVoice) {
        if (!isPlaying) {
            tts?.stop()
        } else {
            tts?.stop()
            val params = android.os.Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "narrative_story")
            tts?.speak(activeVoice.text, TextToSpeech.QUEUE_FLUSH, params, "narrative_story")
        }
    }

    // Ticker progress animation
    var elapsedSeconds by remember { mutableStateOf(0) }
    val estimatedDuration = remember(activeVoice.text) { 
        (activeVoice.text.split(" ").size / 2.5).toInt().coerceAtLeast(10)
    }

    LaunchedEffect(isPlaying, activeVoice) {
        if (isPlaying) {
            elapsedSeconds = 0
            while (elapsedSeconds < estimatedDuration) {
                kotlinx.coroutines.delay(1000)
                elapsedSeconds += 1
            }
            isPlaying = false
        } else {
            elapsedSeconds = 0
        }
    }

    // Interactive waveform heights ticker
    var amplitudes by remember { mutableStateOf(List(16) { 0.2f }) }
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                amplitudes = List(16) { 0.15f + kotlin.random.Random.nextFloat() * 0.85f }
                kotlinx.coroutines.delay(110)
            }
        } else {
            amplitudes = List(16) { 0.15f }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎙️ LOCAL ADDA AUDIO STORY",
                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f)
            )
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Aahar Voices",
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Voice Picker Chips
        Text(
            text = "Select Local Narrator:",
            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            voiceOptions.forEachIndexed { idx, voice ->
                val isSelected = idx == selectedVoiceIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (selectedVoiceIndex != idx) {
                                isPlaying = false
                                selectedVoiceIndex = idx
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (idx == 0) "🧔🏽 ${voice.name}" else "🎸 ${voice.name}",
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = voice.role,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 8.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual Audio Player Console
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play/Pause Button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (isPlaying) MaterialTheme.colorScheme.secondary
                                else MaterialTheme.colorScheme.primary
                            )
                            .testTag("audioplayer_play_pause")
                            .clickable {
                                isPlaying = !isPlaying
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isPlaying) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.width(4.dp).height(16.dp).background(Color.White, RoundedCornerShape(1.dp)))
                                Box(modifier = Modifier.width(4.dp).height(16.dp).background(Color.White, RoundedCornerShape(1.dp)))
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Story",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Simulated Live Waveform Graphic
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .height(26.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            amplitudes.forEach { amp ->
                                val animatedAmp by animateFloatAsState(
                                    targetValue = amp,
                                    animationSpec = tween(100),
                                    label = "waveform_bar_amp"
                                )
                                Box(
                                    modifier = Modifier
                                        .width(3.2.dp)
                                        .fillMaxHeight(animatedAmp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(
                                            if (isPlaying) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
                                        )
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))

                        // Progress line and counters
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val activeProgress = if (estimatedDuration > 0) elapsedSeconds.toFloat() / estimatedDuration.toFloat() else 0f
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(1.5.dp))
                                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(activeProgress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "0:${elapsedSeconds.toString().padStart(2, '0')} / 0:${estimatedDuration.toString().padStart(2, '0')}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 9.sp,
                                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Transcript Caption Overlay
                Spacer(modifier = Modifier.height(10.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "“${activeVoice.text}”",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "⚡ Real-time narration powered by Android's speech engine.",
            fontSize = 8.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

// --- INTERACTIVE AI ARCHIVAL GALLERY COMPONENT ---

data class ArchivalScene(
    val id: String,
    val title: String,
    val location: String,
    val era: String,
    val category: String,
    val summary: String,
    val plateNo: String,
    val primarySubject: String
)

@Composable
fun AIArchivalGallery(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 1. Static historical scenes
    val scenes = remember {
        listOf(
            ArchivalScene(
                id = "coffee_house_1953",
                title = "Albert Hall Coffee House Adda",
                location = "College Street, Calcutta",
                era = "1953",
                category = "Intellectual Hubs",
                summary = "A smoke-filled colonial double-height hall vibrating with Bengali Renaissance energy. Satyajit Ray, Mrinal Sen, and iconic poets sat here debating French Cinema, literature, and art over ninety-paisa tea infusions.",
                plateNo = "Plate I",
                primarySubject = "Satyajit Ray sketching storyboards on paper napkins"
            ),
            ArchivalScene(
                id = "paramount_1932",
                title = "Paramount Swadeshi Booths",
                location = "College Street, Calcutta",
                era = "1932",
                category = "Revolutionary Parlors",
                summary = "Behind heavy timber curtains, young Swadeshi revolutionaries met in secret booths to hide booklets. Under mounting wall trophies, they drank cooling Daab Sherbets formulated by Acharya P.C. Ray to sharpen nationalistic minds.",
                plateNo = "Plate II",
                primarySubject = "Anushilan Samiti cadets plotting underground routes over sherbet"
            ),
            ArchivalScene(
                id = "mitra_cafe_1948",
                title = "Mitra Cafe Shyambazar Cabin",
                location = "Shyambazar Crossroads",
                era = "1948",
                category = "Curtained Cabins",
                summary = "Private plywood dining cabins offering high privacy for post-independence activists and theater visionaries. Here, secret scripts were drafted while eating legendary Kabiraji fish cutlets warped in complex lacy fried egg nets.",
                plateNo = "Plate III",
                primarySubject = "Group of anti-colonial street theater actors editing screenplays"
            ),
            ArchivalScene(
                id = "bhim_nag_1885",
                title = "Bhim Chandra Nag Confectioneries",
                location = "Bowbazar Bazaar",
                era = "1885",
                category = "Sweet Renaissance",
                summary = "Where master sweet artisans of Bengal molded sandesh on rare wood seals. When Lady Canning visited, Bowbazar rolled out the legendary 'Ledikeni' syrup sweet in her honor. Antique Cooke & Kelvey grandfather clocks still tick here.",
                plateNo = "Plate IV",
                primarySubject = "Aristocratic zamindari landholders sampling gold-leafed sandesh"
            ),
            ArchivalScene(
                id = "dacres_lane_1941",
                title = "Chitto Babur Dokan WWII Lunch",
                location = "Dacres Lane Office Alley",
                era = "1941",
                category = "Street Food Cauldrons",
                summary = "Bustling World War II clerical alley. Inside small brick rooms, High Court clerks, soldiers, and tram conductors sat cheek-by-jowl over massive coal cauldrons boiling delicious, light-peppered chicken stews and heavy buttery toast.",
                plateNo = "Plate V",
                primarySubject = "High Court clerks debating soccer matches over steaming broth"
            )
        )
    }

    var selectedSceneIndex by remember { mutableStateOf(0) }
    val currentScene = scenes[selectedSceneIndex]

    // Filter modes: 0 = Monochromatic Negative, 1 = Washed Sepia, 2 = AI Reseamed Color
    var filterType by remember { mutableStateOf(1) }

    // Custom focus prompt state
    var focusFieldText by remember { mutableStateOf("") }
    
    // ViewModel states
    val isRecoloring by viewModel.isRecoloring.collectAsStateWithLifecycle()
    val archivalReimagination by viewModel.archivalReimagination.collectAsStateWithLifecycle()

    // Reset reimagination block whenever selected scene changes
    LaunchedEffect(selectedSceneIndex) {
        viewModel.clearArchivalReimagination()
        focusFieldText = ""
        filterType = 1 // default to sepia
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Visual Board Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "📷 NEW NEURAL HERITAGE GALLERY",
                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.2.sp
            )
        }

        // Introduction text block
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            PaddingValues(12.dp).let { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Text(
                        text = "Step into Kolkata\\'s archival history. Scroll through 19th & 20th-century retro kitchen scenes and use are Generative AI engine to colorize pictures, recover acoustic soundscapes, and restore vintage flavors from physical memories.",
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Horizontal Carousel selector of historic plates
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Select Plate Negative to Analyze:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(scenes.size) { idx ->
                    val sceneItem = scenes[idx]
                    val isSelected = idx == selectedSceneIndex
                    
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                selectedSceneIndex = idx
                            }
                            .padding(8.dp)
                            .testTag("archival_photo_card_${sceneItem.id}")
                    ) {
                        Column {
                            // Mini plate banner
                            Text(
                                text = sceneItem.plateNo,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = sceneItem.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Est. ${sceneItem.era}",
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Active Photographic Plate Display Console
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                
                // Photo Area with filters and details
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = when (filterType) {
                                    0 -> listOf(Color(0xFF222222), Color(0xFF111111)) // monochrome
                                    1 -> listOf(Color(0xFF423727), Color(0xFF231E18)) // sepia
                                    else -> listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.surface
                                    ) // colorized
                                }
                            )
                        )
                        .padding(12.dp)
                ) {
                    // Vintage paper plate style
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(6.dp)
                    ) {
                        // Natively Drawn Retro Illustration Vector Panel
                        ArchivalIllustrationDrawn(
                            sceneId = currentScene.id,
                            filterType = filterType,
                            modifier = Modifier.fillMaxSize()
                        )

                        // 19th Century Print corners drawing
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cornerSize = 14.dp.toPx()
                            val strokeWidth = 2.dp.toPx()
                            val cornerColor = if (filterType == 2) Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.25f)

                            // Top Left Corner
                            drawPath(
                                path = Path().apply {
                                    moveTo(0f, cornerSize)
                                    lineTo(0f, 0f)
                                    lineTo(cornerSize, 0f)
                                },
                                color = cornerColor,
                                style = Stroke(width = strokeWidth)
                            )
                            // Top Right Corner
                            drawPath(
                                path = Path().apply {
                                    moveTo(size.width - cornerSize, 0f)
                                    lineTo(size.width, 0f)
                                    lineTo(size.width, cornerSize)
                                },
                                color = cornerColor,
                                style = Stroke(width = strokeWidth)
                            )
                            // Bottom Left Corner
                            drawPath(
                                path = Path().apply {
                                    moveTo(0f, size.height - cornerSize)
                                    lineTo(0f, size.height)
                                    lineTo(cornerSize, size.height)
                                },
                                color = cornerColor,
                                style = Stroke(width = strokeWidth)
                            )
                            // Bottom Right Corner
                            drawPath(
                                path = Path().apply {
                                    moveTo(size.width - cornerSize, size.height)
                                    lineTo(size.width, size.height)
                                    lineTo(size.width, size.height - cornerSize)
                                },
                                color = cornerColor,
                                style = Stroke(width = strokeWidth)
                            )
                        }

                        // Ink Written Plate Stamp Overlay (Monospace Vintage Typewriter label)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.65f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${currentScene.plateNo} • ${currentScene.location} [circa ${currentScene.era}]",
                                color = Color.White,
                                fontSize = 8.5.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }

                        // Glowing AI Sweep Reconstruction Scanline Overlay
                        if (isRecoloring) {
                            val infiniteTransition = rememberInfiniteTransition(label = "scanning_line")
                            val sweepOffset by infiniteTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1400, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "scanning_offset"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.04f)
                                    .offset(y = 160.dp * sweepOffset)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.1f),
                                                MaterialTheme.colorScheme.primary,
                                                Color.White.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 0.5.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                    )
                            )
                        }
                    }
                }

                // Control Console Toggles
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Image Shader Modes:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("📸 B&W Negative", "🎞️ Sepia", "🎨 AI Colorized").forEachIndexed { idx, label ->
                            val isActive = filterType == idx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isActive) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isActive) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        filterType = idx
                                    }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                                    .testTag(
                                        if (idx == 0) "filter_negative"
                                        else if (idx == 1) "filter_sepia"
                                        else "filter_recolor"
                                    )
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 8.5.sp,
                                    color = if (isActive) Color.White else MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Metadata Details text below
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = currentScene.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${currentScene.category} • Key subject focus: '${currentScene.primarySubject}'",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentScene.summary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Real-time custom focus scanner options
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "🔍 Refine AI Neural Restoration (Optional):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = "Refine the neural scanner with a particular focus. Ask what dhotis were worn, how the fuel ovens burned, where revolutionaries hid the flyers, or details of custom sweet-molds.",
                    fontSize = 9.5.sp,
                    lineHeight = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = focusFieldText,
                    onValueChange = { focusFieldText = it },
                    placeholder = { Text("e.g. restore colors of Satyajit's sketches, ask of street tea brand...", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("focus_refinement_input"),
                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        if (!isRecoloring) {
                            filterType = 2 // automatically shift to colorized mode
                            viewModel.generateArchivalReimagination(currentScene.title, currentScene.era, focusFieldText)
                        }
                    })
                )
            }
        }

        // Trigger analysis Button or Loading status bar
        if (isRecoloring) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                Text(
                    text = "Scanning silver-halide granules... Reconstructing decade soundscapes...",
                    fontSize = 9.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Button(
                onClick = {
                    filterType = 2 // Shift to restored color card filter
                    viewModel.generateArchivalReimagination(currentScene.title, currentScene.era, focusFieldText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("reconstruct_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restore",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (focusFieldText.isBlank()) "⚡ RECOLOR & SENSORY RECONSTRUCT VIA AI"
                           else "⚡ REFINE SCAN WITH FOCUS TOPIC",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Antique Scroll styling for displaying the Generative AI Restoration Text
        AnimatedVisibility(
            visible = archivalReimagination.isNotEmpty(),
            enter = expandVertically(animationSpec = spring()) + fadeIn(animationSpec = spring()),
            exit = shrinkVertically(animationSpec = spring()) + fadeOut(animationSpec = spring())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFCBB693), // classic copper-gold board
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(Color(0xFFFCF9F2)) // classic warm paper parchment tone
                    .padding(14.dp)
            ) {
                // Scroll Header Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (focusFieldText.isBlank()) "⚜️ HISTORICAL COLORIZED CHRONICLE"
                               else "⚜️ SPECIFIC AI RECONSTRUCTION REPORT",
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF634D2E), // Rich coffee paper text
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Aahar Neural Core",
                        fontSize = 8.sp,
                        color = Color(0xFF8A714A),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                }

                Divider(
                    color = Color(0xFFEADBCE),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                androidx.compose.foundation.text.selection.SelectionContainer {
                    Text(
                        text = archivalReimagination,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontSize = 11.5.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF382A1B), // very high contrast warm text
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "❧ Restored utilizing Gemini\\'s cognitive chronological memory. ☙",
                        fontSize = 8.5.sp,
                        color = Color(0xFF8A714A),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

// Draw custom high-quality vector illustrations in Compose canvas based on historical scene
@Composable
fun ArchivalIllustrationDrawn(
    sceneId: String,
    filterType: Int,
    modifier: Modifier = Modifier
) {
    // Select styling color based on filters
    val inkColor = when (filterType) {
        0 -> Color(0xFFCCCCCC) // monochromatic negative brights
        1 -> Color(0xFFC0A47B) // washed sepia tones
        else -> Color(0xFFF99E44) // colorized base tones
    }
    
    val accentColor = when (filterType) {
        0 -> Color(0xFF555555)
        1 -> Color(0xFF8B6C43)
        else -> Color(0xFF0F9D58) // Green accent for colorized
    }

    val baseBrushColor = when (filterType) {
        0 -> Color(0xFF222222)
        1 -> Color(0xFF3E311F)
        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Common background dust/noise texture dots drawing
        kotlin.random.Random(42).let { rand ->
            repeat(16) {
                val dx = rand.nextFloat() * width
                val dy = rand.nextFloat() * height
                drawCircle(
                    color = inkColor.copy(alpha = 0.15f),
                    radius = (1f + rand.nextFloat() * 2f).dp.toPx(),
                    center = Offset(dx, dy)
                )
            }
        }

        // Draw specifics based on target eatery scene
        when (sceneId) {
            "coffee_house_1953" -> {
                // Draw colonial high window panes
                drawRect(
                    color = inkColor.copy(alpha = 0.15f),
                    size = androidx.compose.ui.geometry.Size(width * 0.2f, height * 0.6f),
                    topLeft = Offset(width * 0.15f, height * 0.1f)
                )
                drawRect(
                    color = inkColor.copy(alpha = 0.15f),
                    size = androidx.compose.ui.geometry.Size(width * 0.2f, height * 0.6f),
                    topLeft = Offset(width * 0.65f, height * 0.1f)
                )
                // Window frames line structures
                drawLine(
                    color = inkColor.copy(alpha = 0.3f),
                    start = Offset(width * 0.25f, height * 0.1f),
                    end = Offset(width * 0.25f, height * 0.7f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = inkColor.copy(alpha = 0.3f),
                    start = Offset(width * 0.75f, height * 0.1f),
                    end = Offset(width * 0.75f, height * 0.7f),
                    strokeWidth = 1.dp.toPx()
                )

                // Spinning retro colonial ceiling fan in the middle
                drawCircle(
                    color = inkColor.copy(alpha = 0.7f),
                    radius = 8.dp.toPx(),
                    center = Offset(width * 0.5f, height * 0.15f)
                )
                // Rotating blades (3 thin segments)
                drawLine(
                    color = inkColor.copy(alpha = 0.5f),
                    start = Offset(width * 0.5f, height * 0.15f),
                    end = Offset(width * 0.5f - 40.dp.toPx(), height * 0.12f),
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = inkColor.copy(alpha = 0.5f),
                    start = Offset(width * 0.5f, height * 0.15f),
                    end = Offset(width * 0.5f + 40.dp.toPx(), height * 0.12f),
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = inkColor.copy(alpha = 0.5f),
                    start = Offset(width * 0.5f, height * 0.15f),
                    end = Offset(width * 0.5f, height * 0.15f + 30.dp.toPx()),
                    strokeWidth = 3.dp.toPx()
                )

                // High wooden table in foreground with overlapping circles representing porcelain cups
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.2f, height * 0.85f)
                        lineTo(width * 0.8f, height * 0.85f)
                        lineTo(width * 0.75f, height * 0.95f)
                        lineTo(width * 0.25f, height * 0.95f)
                        close()
                    },
                    color = accentColor.copy(alpha = 0.3f)
                )
                // Porcelain teacup circles on table
                drawCircle(
                    color = inkColor,
                    radius = 11.dp.toPx(),
                    center = Offset(width * 0.42f, height * 0.88f)
                )
                drawCircle(
                    color = accentColor,
                    radius = 5.dp.toPx(),
                    center = Offset(width * 0.42f, height * 0.88f)
                )
                drawCircle(
                    color = inkColor,
                    radius = 9.dp.toPx(),
                    center = Offset(width * 0.58f, height * 0.87f)
                )

                // Cigarette smoke swirls rising beautifully
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.42f, height * 0.82f)
                        quadraticBezierTo(width * 0.45f, height * 0.65f, width * 0.4f, height * 0.5f)
                        quadraticBezierTo(width * 0.35f, height * 0.35f, width * 0.42f, height * 0.25f)
                    },
                    color = inkColor.copy(alpha = 0.25f),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            "paramount_1932" -> {
                // Draw historic wall mirrors (three long tall arcs)
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.15f, height * 0.65f)
                        lineTo(width * 0.15f, height * 0.15f)
                        quadraticBezierTo(width * 0.25f, height * 0.05f, width * 0.35f, height * 0.15f)
                        lineTo(width * 0.35f, height * 0.65f)
                        close()
                    },
                    color = inkColor.copy(alpha = 0.15f)
                )
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.65f, height * 0.65f)
                        lineTo(width * 0.65f, height * 0.15f)
                        quadraticBezierTo(width * 0.75f, height * 0.05f, width * 0.85f, height * 0.15f)
                        lineTo(width * 0.85f, height * 0.65f)
                        close()
                    },
                    color = inkColor.copy(alpha = 0.15f)
                )

                // Mounted antler/deer head trophy in the dead center
                drawCircle(
                    color = accentColor.copy(alpha = 0.6f),
                    radius = 10.dp.toPx(),
                    center = Offset(width * 0.5f, height * 0.3f)
                )
                // Antlers drawing with paths
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.48f, height * 0.26f)
                        quadraticBezierTo(width * 0.42f, height * 0.18f, width * 0.35f, height * 0.2f)
                        moveTo(width * 0.44f, height * 0.22f)
                        lineTo(width * 0.43f, height * 0.13f)
                        
                        moveTo(width * 0.52f, height * 0.26f)
                        quadraticBezierTo(width * 0.58f, height * 0.18f, width * 0.65f, height * 0.2f)
                        moveTo(width * 0.56f, height * 0.22f)
                        lineTo(width * 0.57f, height * 0.13f)
                    },
                    color = inkColor,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Classic tall tall glass of Daab Sherbet on a wood coaster
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.46f, height * 0.65f)
                        lineTo(width * 0.54f, height * 0.65f)
                        lineTo(width * 0.56f, height * 0.9f)
                        lineTo(width * 0.44f, height * 0.9f)
                        close()
                    },
                    color = accentColor.copy(alpha = 0.4f)
                )
                // Liquid inside
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.465f, height * 0.72f)
                        lineTo(width * 0.535f, height * 0.72f)
                        lineTo(width * 0.55f, height * 0.88f)
                        lineTo(width * 0.45f, height * 0.88f)
                        close()
                    },
                    color = inkColor.copy(alpha = 0.6f)
                )
                // Lemon slice float icon
                drawCircle(
                    color = if (filterType == 2) Color(0xFFFEEA3B) else inkColor,
                    radius = 8.dp.toPx(),
                    center = Offset(width * 0.58f, height * 0.75f)
                )
            }
            "mitra_cafe_1948" -> {
                // Private plywood wooden cabin partitions (parallel vertical slats)
                repeat(4) { idx ->
                    val pos = width * (0.05f + idx * 0.3f)
                    drawRect(
                        color = accentColor.copy(alpha = 0.15f),
                        topLeft = Offset(pos, 0f),
                        size = androidx.compose.ui.geometry.Size(15.dp.toPx(), height * 0.95f)
                    )
                }

                // Hanging heavy curtain rings and wire spanning across
                drawLine(
                    color = inkColor.copy(alpha = 0.4f),
                    start = Offset(0f, height * 0.25f),
                    end = Offset(width, height * 0.25f),
                    strokeWidth = 1.dp.toPx()
                )
                repeat(5) { idx ->
                    drawCircle(
                        color = inkColor,
                        radius = 4.dp.toPx(),
                        center = Offset(width * (0.15f + idx * 0.18f), height * 0.25f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Hot Kettle on a plate in foreground
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.4f, height * 0.78f)
                        lineTo(width * 0.6f, height * 0.78f)
                        lineTo(width * 0.62f, height * 0.9f)
                        lineTo(width * 0.38f, height * 0.9f)
                        close()
                    },
                    color = accentColor.copy(alpha = 0.5f)
                )
                // Kettle handle loop
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.42f, height * 0.78f)
                        quadraticBezierTo(width * 0.5f, height * 0.6f, width * 0.58f, height * 0.78f)
                    },
                    color = inkColor,
                    style = Stroke(width = 2.dp.toPx())
                )
                // Cutlet Plate circle next to kettle
                drawCircle(
                    color = inkColor,
                    radius = 16.dp.toPx(),
                    center = Offset(width * 0.7f, height * 0.85f)
                )
                drawCircle(
                    color = accentColor,
                    radius = 10.dp.toPx(),
                    center = Offset(width * 0.7f, height * 0.85f)
                )
            }
            "bhim_nag_1885" -> {
                // Cooke & Kelvey Victorian wooden clock on back wall
                drawRoundRect(
                    color = accentColor.copy(alpha = 0.3f),
                    topLeft = Offset(width * 0.4f, height * 0.15f),
                    size = androidx.compose.ui.geometry.Size(width * 0.2f, height * 0.5f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                )
                // Clock Dial circle
                drawCircle(
                    color = Color.White.copy(alpha = 0.8f),
                    radius = 16.dp.toPx(),
                    center = Offset(width * 0.5f, height * 0.3f)
                )
                // Clock hands pointing in fine details
                drawLine(
                    color = Color.Black,
                    start = Offset(width * 0.5f, height * 0.3f),
                    end = Offset(width * 0.5f, height * 0.3f - 10.dp.toPx()),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(width * 0.5f, height * 0.3f),
                    end = Offset(width * 0.5f + 8.dp.toPx(), height * 0.32f),
                    strokeWidth = 1.5.dp.toPx()
                )
                // Pendulum bob loop below clock face
                drawLine(
                    color = inkColor,
                    start = Offset(width * 0.5f, height * 0.4f),
                    end = Offset(width * 0.5f, height * 0.52f),
                    strokeWidth = 2.dp.toPx()
                )
                drawCircle(
                    color = inkColor,
                    radius = 4.dp.toPx(),
                    center = Offset(width * 0.5f, height * 0.54f)
                )

                // Large copper vats storing sweets (overlapping concentric oval molds)
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.15f, height * 0.88f)
                        lineTo(width * 0.85f, height * 0.88f)
                        quadraticBezierTo(width * 0.5f, height * 0.99f, width * 0.15f, height * 0.88f)
                    },
                    color = accentColor.copy(alpha = 0.6f)
                )

                // Plated rows of sweet Sandeshes (small stacked grids of squares)
                repeat(4) { row ->
                    repeat(3) { col ->
                        drawCircle(
                            color = if (filterType == 2) Color(0xFFF3E5F5) else inkColor,
                            radius = 4.dp.toPx(),
                            center = Offset(
                                width * 0.32f + row * 14.dp.toPx(),
                                height * 0.76f + col * 7.dp.toPx()
                            )
                        )
                    }
                }
            }
            "dacres_lane_1941" -> {
                // Draw alley building architecture (converging perspective wall lines)
                drawPath(
                    path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(width * 0.28f, height * 0.6f)
                        lineTo(width * 0.28f, height * 0.95f)
                        lineTo(0f, height)
                        close()
                    },
                    color = accentColor.copy(alpha = 0.15f)
                )
                drawPath(
                    path = Path().apply {
                        moveTo(width, 0f)
                        lineTo(width * 0.72f, height * 0.6f)
                        lineTo(width * 0.72f, height * 0.95f)
                        lineTo(width, height)
                        close()
                    },
                    color = accentColor.copy(alpha = 0.15f)
                )

                // Huge steaming copper cauldron boiling stew in center alley position
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.38f, height * 0.92f)
                        quadraticBezierTo(width * 0.5f, height * 0.98f, width * 0.62f, height * 0.92f)
                        lineTo(width * 0.66f, height * 0.7f)
                        lineTo(width * 0.34f, height * 0.7f)
                        close()
                    },
                    color = accentColor.copy(alpha = 0.6f)
                )
                // Huge steam waves rising out of the stew cooker pot
                drawPath(
                    path = Path().apply {
                        moveTo(width * 0.44f, height * 0.67f)
                        quadraticBezierTo(width * 0.42f, height * 0.58f, width * 0.48f, height * 0.45f)
                        moveTo(width * 0.5f, height * 0.66f)
                        quadraticBezierTo(width * 0.53f, height * 0.55f, width * 0.47f, height * 0.42f)
                        moveTo(width * 0.56f, height * 0.68f)
                        quadraticBezierTo(width * 0.54f, height * 0.59f, width * 0.59f, height * 0.48f)
                    },
                    color = inkColor.copy(alpha = 0.4f),
                    style = Stroke(width = 3.dp.toPx())
                )

                // Golden buttery toast stick block drawn next to broth pot
                drawRoundRect(
                    color = if (filterType == 2) Color(0xFFFFB300) else inkColor,
                    topLeft = Offset(width * 0.75f, height * 0.72f),
                    size = androidx.compose.ui.geometry.Size(18.dp.toPx(), 22.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                )
            }
        }
    }
}

// ==========================================
// 6. ADDA MOOD TRACKER SCREEN
// ==========================================

data class AddaMood(
    val id: String,
    val name: String,
    val bengaliName: String,
    val icon: String,
    val description: String,
    val badgeColor: Color,
    val exampleFuel: String
)

@Composable
fun AddaMoodTrackerScreen(
    viewModel: CulinaryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val moods = remember {
        listOf(
            AddaMood("intellectual", "Aateli (Academic)", "আঁতেল", "🎓", "High-brow discussions on Ray, French cinema, Marxism, or revolutionary histories.", Color(0xFF3F51B5), "Black Coffee House Infusion & biscuits"),
            AddaMood("creative", "Saraswat (Artistic)", "সারস্বত", "🎭", "Poetry reading, song trials, script updates, or local neighborhood theater rehearsals.", Color(0xFFE91E63), "Hot Darjeeling tea & lacy egg fish Kabiraji"),
            AddaMood("secret", "Swadeshi (Conspiratorial)", "স্বদেশী", "🤫", "Whispered private plots, political gossips, or underground plans behind booth curtains.", Color(0xFFD84315), "Cold Daab Sherbet (Coconut water syrup)"),
            AddaMood("fiery", "Goshthi (Passionate Debate)", "গোষ্ঠী", "⚽", "Heated debates on Mohun Bagan vs East Bengal or current local affairs.", Color(0xFFC62828), "Steaming light-pepper broth & crisp toast"),
            AddaMood("lazy", "Lyadh (Nostalgic)", "ল্যাদখোর", "☕", "Unhurried, sweet conversations on a road rock, clay-cup tea, and nostalgic topics.", Color(0xFFED6C02), "Cardamom tea in Bhaanr (clay cup) & biscuits")
        )
    }

    var selectedMoodIndex by remember { mutableStateOf(0) }
    val currentMood = moods[selectedMoodIndex]

    // Companion structures
    val companionOptions = remember {
        listOf(
            "👥 Sole Scholar (Self-reflection)",
            "👥 Romantic Jodi (Duo discussion)",
            "👥 Goshthi Crew (3-5 Friends)",
            "👥 Biplobi Rebels (6+ Crowd)"
        )
    }
    var selectedCompanionIndex by remember { mutableStateOf(2) }

    // Fuel structures
    val fuelOptions = remember {
        listOf(
            "☕ Bhaanr-er-Cha (Clay Cup Chai)",
            "☕ Filter Coffee Infusion",
            "🍹 Coconut Daab Sherbet",
            "🍤 Lacy Fish Kabiraji Cutlets",
            "🍬 Syrup Ledikeni Sweets"
        )
    }
    var selectedFuelIndex by remember { mutableStateOf(0) }

    // Custom companion notes
    var companionNotes by remember { mutableStateOf("") }

    // Collect flow states from viewmodel
    val addaSessions by viewModel.addaSessions.collectAsStateWithLifecycle()
    val isGeneratingPrescription by viewModel.isGeneratingPrescription.collectAsStateWithLifecycle()
    val generatedPrescription by viewModel.generatedPrescription.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Visual Banner Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "🎭 KHEYAAL: ADDA MOOD DECODING LAB",
                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.2.sp
            )
        }

        // Introduction text block
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "In Kolkata, an Adda is not mere gossip—it is an intellectual sanctuary. Sync your behavioral frequencies, specify your gathering size, fuel preference, and let our neurological engine generate matching suggestions and custom debate topics!",
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Horizontal selections of moods with Bengali glyphs
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Select your Adda Vibe (Mood State):",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(moods.size) { idx ->
                    val moodItem = moods[idx]
                    val isSelected = idx == selectedMoodIndex
                    
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) moodItem.badgeColor.copy(alpha = 0.12f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) moodItem.badgeColor
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedMoodIndex = idx
                                viewModel.clearGeneratedPrescription()
                            }
                            .padding(10.dp)
                            .testTag("adda_mood_card_${moodItem.id}")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = moodItem.icon,
                                    fontSize = 18.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(moodItem.badgeColor.copy(alpha = 0.15f))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = moodItem.bengaliName,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = moodItem.badgeColor
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = moodItem.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = moodItem.description,
                                fontSize = 9.sp,
                                lineHeight = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Selection variables (Companions & Fuel Options)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "⚙️ Configure Adda Blueprint Parameters",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Companions selection list
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Who is joining the assembly?",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        companionOptions.forEachIndexed { index, option ->
                            val isChosen = index == selectedCompanionIndex
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isChosen) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                    .clickable {
                                        selectedCompanionIndex = index
                                        viewModel.clearGeneratedPrescription()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.split(" ")[1], // just the text part
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), thickness = 1.dp)

                // Fuel Selection list
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Primary intellectual fuel choice:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        fuelOptions.forEachIndexed { index, option ->
                            val isChosen = index == selectedFuelIndex
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isChosen) MaterialTheme.colorScheme.secondary
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                    .clickable {
                                        selectedFuelIndex = index
                                        viewModel.clearGeneratedPrescription()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.substringAfter(" "), // just fuel title
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), thickness = 1.dp)

                // Notes about companions
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Identify the companion names / specific groups (optional):",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = companionNotes,
                        onValueChange = { companionNotes = it },
                        placeholder = { Text("e.g. College mates, local Para seniors, theater crew...", fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("companion_notes_field"),
                        textStyle = LocalTextStyle.current.copy(fontSize = 11.sp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                    )
                }
            }
        }

        // Generating prescription State Indicator or trigger Button
        if (isGeneratingPrescription) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(3.dp)),
                    color = currentMood.badgeColor,
                    trackColor = currentMood.badgeColor.copy(alpha = 0.1f)
                )
                Text(
                    text = "Distilling tea aromas... Brewing debate topics for ${currentMood.name}...",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = currentMood.badgeColor
                )
            }
        } else {
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.generateAddaPrescription(
                        moodName = currentMood.name,
                        companions = "${companionOptions[selectedCompanionIndex].substringAfter(" ")} [${if (companionNotes.isNotBlank()) companionNotes else "unnamed gang"}]",
                        fuelType = fuelOptions[selectedFuelIndex].substringAfter(" ")
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("prescribe_adda_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = currentMood.badgeColor)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Prescribe",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⚡ CONCOCT MY CHRONICLED ADDA PATH",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Expandable AI generated result Display
        AnimatedVisibility(
            visible = generatedPrescription.isNotEmpty(),
            enter = expandVertically(animationSpec = spring()) + fadeIn(animationSpec = spring()),
            exit = shrinkVertically(animationSpec = spring()) + fadeOut(animationSpec = spring())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFFA1887F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(Color(0xFFFAF6EE)) // nostalgic ivory warm background
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚜️ OFFICIAL ADDA MOOD PRESCRIPTION",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Aahar Lab AI",
                        fontSize = 8.sp,
                        color = Color(0xFF8D6E63),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(
                    color = Color(0xFFEFE6D5),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                androidx.compose.foundation.text.selection.SelectionContainer {
                    Text(
                        text = generatedPrescription,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontSize = 11.5.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF2E1C16),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        viewModel.saveAddaSession(
                            moodName = currentMood.name,
                            moodIcon = currentMood.icon,
                            companions = "${companionOptions[selectedCompanionIndex].substringAfter(" ")} [${if (companionNotes.isNotBlank()) companionNotes else "unnamed notes"}]",
                            fuelType = fuelOptions[selectedFuelIndex].substringAfter(" "),
                            prescription = generatedPrescription
                        )
                        android.widget.Toast.makeText(context, "Logged in your Adda Journal! 🖋️☕", android.widget.Toast.LENGTH_SHORT).show()
                        viewModel.clearGeneratedPrescription()
                        companionNotes = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_journal_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Save",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "💾 SYNC & RECORD IN JOURNAL HISTORY",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Recorded Addas Book List (Local Room DB Records)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📜 THE ADDA ARCHIVE JOURNAL (${addaSessions.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (addaSessions.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            viewModel.clearAllAddaSessionsHistory()
                            android.widget.Toast.makeText(context, "All history cleared.", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(
                            text = "Purge Archive",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (addaSessions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "☕ No logged addas in database yet.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Configure above, request a prescription, and synchronize your session to start logging history!",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                // List of sessions
                addaSessions.forEach { session ->
                    var isExpanded by remember { mutableStateOf(false) }
                    
                    val formattedDate = remember(session.timestamp) {
                        val date = java.util.Date(session.timestamp)
                        val format = java.text.SimpleDateFormat("MMM dd, yyyy • HH:mm", java.util.Locale.getDefault())
                        format.format(date)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("journal_card_${session.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = session.moodIcon.ifEmpty { "☕" },
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Adda: ${session.moodName}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = formattedDate,
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.deleteAddaSession(session) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "👥 COMPANIONS",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = session.companions,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "⚡ FUEL SOURCE",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = session.fuelType,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Expandable Prescription Detail Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                    .clickable { isExpanded = !isExpanded }
                                    .padding(vertical = 6.dp, horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isExpanded) "📖 Tap to Hide Guide Blueprint" else "📖 Tap to Reveal Guide Blueprint",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.PlayArrow,
                                    contentDescription = "Arrow",
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFAF6EE)) // Nostalgic ivory
                                        .border(0.5.dp, Color(0xFFD7CCC8), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = session.recommendation,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                        fontSize = 10.5.sp,
                                        lineHeight = 15.sp,
                                        color = Color(0xFF3E2723)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


