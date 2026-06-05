package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.AaharDatabase
import com.example.data.database.ChatMessageEntity
import com.example.data.database.FavoriteSpot
import com.example.data.database.AddaSessionEntity
import com.example.data.database.SearchQueryEntity
import com.example.data.model.FoodTrail
import com.example.data.model.HeritageDish
import com.example.data.model.HeritageSpot
import com.example.data.repository.CulinaryRepository
import com.example.ui.translation.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Explore : Screen()
    object Trails : Screen()
    object AIConcierge : Screen()
    object Favorites : Screen()
}

class CulinaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CulinaryRepository

    init {
        val database = AaharDatabase.getDatabase(application)
        repository = CulinaryRepository(database.aaharDao())
    }

    // --- Navigation State ---
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Explore)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // --- Language State ---
    private val _isBengali = MutableStateFlow(false)
    val isBengali: StateFlow<Boolean> = _isBengali.asStateFlow()

    fun toggleLanguage() {
        _isBengali.value = !_isBengali.value
    }

    // --- Active Selection Modals ---
    private val _selectedSpot = MutableStateFlow<HeritageSpot?>(null)
    val selectedSpot: StateFlow<HeritageSpot?> = _selectedSpot.asStateFlow()

    private val _selectedDish = MutableStateFlow<HeritageDish?>(null)
    val selectedDish: StateFlow<HeritageDish?> = _selectedDish.asStateFlow()

    private val _selectedTrail = MutableStateFlow<FoodTrail?>(null)
    val selectedTrail: StateFlow<FoodTrail?> = _selectedTrail.asStateFlow()

    fun selectSpot(spot: HeritageSpot?) {
        _selectedSpot.value = spot
    }

    fun selectDish(dish: HeritageDish?) {
        _selectedDish.value = dish
    }

    fun selectTrail(trail: FoodTrail?) {
        _selectedTrail.value = trail
    }

    // --- Seed Static Lists ---
    val dishes: List<HeritageDish> = repository.getDishes()
    val spots: List<HeritageSpot> = repository.getSpots()
    val trails: List<FoodTrail> = repository.getTrails()

    // --- Search State & Reactive Filtering ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val recentSearchQueries: StateFlow<List<SearchQueryEntity>> = repository.recentSearchQueries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveSearchQuery(queryText: String) {
        if (queryText.isNotBlank()) {
            viewModelScope.launch {
                repository.saveSearchQuery(queryText)
            }
        }
    }

    fun deleteSearchQuery(queryText: String) {
        viewModelScope.launch {
            repository.deleteSearchQuery(queryText)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }

    val filteredDishes: StateFlow<List<HeritageDish>> = _searchQuery
        .map { query ->
            if (query.isBlank()) {
                dishes
            } else {
                dishes.filter { dish: HeritageDish ->
                    val isBengaliName = dish.localizedName(true)
                    val isBengaliEra = dish.localizedEra(true)
                    val isBengaliStory = dish.localizedStory(true)
                    dish.name.contains(query, ignoreCase = true) ||
                    dish.era.contains(query, ignoreCase = true) ||
                    dish.originStory.contains(query, ignoreCase = true) ||
                    dish.keyEateries.any { it.contains(query, ignoreCase = true) } ||
                    isBengaliName.contains(query, ignoreCase = true) ||
                    isBengaliEra.contains(query, ignoreCase = true) ||
                    isBengaliStory.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), dishes)

    val filteredSpots: StateFlow<List<HeritageSpot>> = _searchQuery
        .map { query ->
            if (query.isBlank()) {
                spots
            } else {
                spots.filter { spot: HeritageSpot ->
                    val isBengaliName = spot.localizedName(true)
                    val isBengaliDesc = spot.localizedDescription(true)
                    val isBengaliIconic = spot.localizedIconicFor(true)
                    val isBengaliLoc = spot.localizedLocation(true)
                    val isBengaliTrivia = spot.localizedTrivia(true)
                    spot.name.contains(query, ignoreCase = true) ||
                    spot.description.contains(query, ignoreCase = true) ||
                    spot.established.contains(query, ignoreCase = true) ||
                    spot.iconicFor.contains(query, ignoreCase = true) ||
                    spot.location.contains(query, ignoreCase = true) ||
                    spot.trivia.contains(query, ignoreCase = true) ||
                    isBengaliName.contains(query, ignoreCase = true) ||
                    isBengaliDesc.contains(query, ignoreCase = true) ||
                    isBengaliIconic.contains(query, ignoreCase = true) ||
                    isBengaliLoc.contains(query, ignoreCase = true) ||
                    isBengaliTrivia.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), spots)

    // --- Persistent Favorites ---
    val favoriteSpots: StateFlow<List<FavoriteSpot>> = repository.favoriteSpots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Tracks which spot IDs are favorited to show instant checkmarks
    val favoriteSpotIdsSet: StateFlow<Set<String>> = repository.favoriteSpots
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun toggleFavorite(spot: HeritageSpot) {
        viewModelScope.launch {
            repository.toggleFavoriteSpot(spot)
        }
    }

    // --- Persistent Chat / Concierge State ---
    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        
        viewModelScope.launch {
            // Save user message
            repository.saveChatMessage("user", text)
            
            // Activate loading spinner
            _isGenerating.value = true
            
            // Get historic transcript to pass context to Gemini
            val currentHistory = chatMessages.value
            
            // Fetch Gemini response
            val reply = repository.generateStoryResponse(text, currentHistory)
            
            // Save model response
            repository.saveChatMessage("concierge", reply)
            
            // Turn off spinner
            _isGenerating.value = false
        }
    }

    fun sendSeedPrompt(text: String) {
        // Direct trigger for prompt chips
        sendChatMessage(text)
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // --- Archival Gallery AI Reimagination State ---
    private val _archivalReimagination = MutableStateFlow<String>("")
    val archivalReimagination: StateFlow<String> = _archivalReimagination.asStateFlow()

    private val _isRecoloring = MutableStateFlow(false)
    val isRecoloring: StateFlow<Boolean> = _isRecoloring.asStateFlow()

    fun generateArchivalReimagination(sceneTitle: String, era: String, customFocus: String) {
        viewModelScope.launch {
            _isRecoloring.value = true
            val prompt = if (customFocus.isBlank()) {
                """
                    As Aahar Culinary Concierge, perform an "AI Photographic Colorization and Sensory Archival Restoration" of the historical scene: '$sceneTitle' ($era).
                    
                    In your response, construct a rich, poetic, and highly descriptive sensory narrative that simulates "restoring full physical details and colors" from an old black-and-white archival photo:
                    
                    Provide the restoration in 3 distinct, beautifully formatted sections with clear retro bullet points:
                    1. 🎨 VISUAL COLORIZATION REPORT: Reconstruct the vibrant color palette, textures, lighting, and smoke patterns (e.g., the exact warm wood luster of the booths, the deep amber of hot tea, the sepia dust floating in shafts of sunlight, dhotis or colonial coats worn).
                    2. 🔊 HISTORICAL SOUNDSCAPE: Describe what was heard at that moment (e.g. clinking tea saucers, heated debates in Bengali, crackling gramophone in the corner, heavy tram bells outside).
                    3. 👅 SENSORY FLAVOR RECONSTRUCTION: Describe the exact taste, aroma, and temperature physics of the iconic food served in that era (e.g. the hot, sharp mustard pungency, the lacy, crispy web of the Kabiraji egg, the coolness of the rose-scented coconut nectar).
                    
                    Make the narration incredibly poetic, immersive, and historical. Keep it focused on the selected scene and historical authenticity.
                """.trimIndent()
            } else {
                """
                    As Aahar Culinary Concierge, perform an "AI Photographic Colorization and Sensory Archival Restoration" of the historical scene: '$sceneTitle' ($era) focusing specifically on: '$customFocus'.
                    
                    In your response, construct a rich, poetic, and highly descriptive sensory narrative that simulates "restoring full physical details and colors" from an old black-and-white archival photo, specifically analyzing and describing how the focus area is visually and historically reconstructed:
                    
                    Provide the restoration in 3 distinct, beautifully formatted sections with clear retro bullet points:
                    1. 🎨 COLORIZED VISUAL FOCUS: Analyze and restore the colors, positions, details, and lighting of the requested focus area ('$customFocus') within the scene.
                    2. 🔊 CORRESPONDING SOUNDSCAPE: Describe any auditory details connected to this restored focus area.
                    3. 👅 SENSORY RECONSTRUCTION: Reconstruct the tactile or culinary sensations related to the restored focus.
                    
                    Make the narration incredibly poetic, immersive, and historical. Keep it focused on the selected scene and historical authenticity.
                """.trimIndent()
            }
            val response = repository.generateStoryResponse(prompt)
            _archivalReimagination.value = response
            _isRecoloring.value = false
        }
    }

    fun clearArchivalReimagination() {
        _archivalReimagination.value = ""
    }

    // --- Adda Mood Tracker States & Operations ---
    val addaSessions: StateFlow<List<AddaSessionEntity>> = repository.addaSessions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isGeneratingPrescription = MutableStateFlow(false)
    val isGeneratingPrescription: StateFlow<Boolean> = _isGeneratingPrescription.asStateFlow()

    private val _generatedPrescription = MutableStateFlow("")
    val generatedPrescription: StateFlow<String> = _generatedPrescription.asStateFlow()

    fun generateAddaPrescription(moodName: String, companions: String, fuelType: String) {
        viewModelScope.launch {
            _isGeneratingPrescription.value = true
            val prompt = """
                Perform a detailed and immersive cultural "Kolkata Adda's Mood Prescription" for an upcoming session:
                - Mood style: $moodName
                - Companions / Gang type: $companions
                - Fuel / Tea / Coffee style: $fuelType

                In your response, assume the role of Aahar Culinary Concierge. Create an elegant, culturally rich, and poetic guide with 3 sections formatting with clear bullet points of antique look:
                1. 🎨 VEIN OF THE ADDA: Describe the cultural philosophy and vibe of this mood in Kolkata (e.g., if it is Swadeshi, reference historic revolutionary tea cabins; if "Fiery Debate", reference East Bengal vs Mohun Bagan debates).
                2. 📍 RECOMMENDED VENUE: Suggest exactly 1 or 2 real legacy places in Kolkata that fit this configuration (e.g. Paramount, Dilkhusha, Coffee House, Chitto Babur Dokan).
                3. ✍️ 3 TAILORED ADDA DEBATE TOPICS: Write 3 highly engaging, culturally authentic intellectual topics of conversation/debate for the companions to talk about (e.g., Satyajit Ray vs Ritwik Ghatak, the role of mustard oil in modern versus traditional Bengali cooking, or local neighborhood soccer matches).

                Keep it highly engaging, poetic, yet concise. Welcome them with a warm greeting.
            """.trimIndent()

            val response = repository.generateStoryResponse(prompt)
            _generatedPrescription.value = response
            _isGeneratingPrescription.value = false
        }
    }

    fun saveAddaSession(moodName: String, moodIcon: String, companions: String, fuelType: String, prescription: String) {
        viewModelScope.launch {
            val session = AddaSessionEntity(
                moodName = moodName,
                moodIcon = moodIcon,
                companions = companions,
                fuelType = fuelType,
                recommendation = prescription
            )
            repository.saveAddaSession(session)
        }
    }

    fun deleteAddaSession(session: AddaSessionEntity) {
        viewModelScope.launch {
            repository.deleteAddaSession(session)
        }
    }

    fun clearAllAddaSessionsHistory() {
        viewModelScope.launch {
            repository.clearHistoryAddaSessions()
        }
    }

    fun clearGeneratedPrescription() {
        _generatedPrescription.value = ""
    }

    // Custom Factory for Instantiation
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CulinaryViewModel::class.java)) {
                return CulinaryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
