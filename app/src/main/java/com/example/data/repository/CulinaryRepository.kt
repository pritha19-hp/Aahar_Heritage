package com.example.data.repository

import com.example.BuildConfig
import com.example.data.database.AaharDao
import com.example.data.database.ChatMessageEntity
import com.example.data.database.FavoriteSpot
import com.example.data.database.AddaSessionEntity
import com.example.data.database.SearchQueryEntity
import com.example.data.model.CulinaryDatabaseSeeds
import com.example.data.model.FoodTrail
import com.example.data.model.HeritageDish
import com.example.data.model.HeritageSpot
import com.example.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CulinaryRepository(private val aaharDao: AaharDao) {

    // --- Static Culinary Data ---
    fun getDishes(): List<HeritageDish> = CulinaryDatabaseSeeds.dishes
    fun getSpots(): List<HeritageSpot> = CulinaryDatabaseSeeds.spots
    fun getTrails(): List<FoodTrail> = CulinaryDatabaseSeeds.trails

    fun getDishById(id: String): HeritageDish? = CulinaryDatabaseSeeds.dishes.find { it.id == id }
    fun getSpotById(id: String): HeritageSpot? = CulinaryDatabaseSeeds.spots.find { it.id == id }
    fun getTrailById(id: String): FoodTrail? = CulinaryDatabaseSeeds.trails.find { it.id == id }

    // --- Favorite Spots Persistence (Room) ---
    val favoriteSpots: Flow<List<FavoriteSpot>> = aaharDao.getFavoriteSpots()

    suspend fun isSpotFavorite(spotId: String): Boolean = withContext(Dispatchers.IO) {
        aaharDao.isFavorite(spotId)
    }

    suspend fun toggleFavoriteSpot(spot: HeritageSpot) = withContext(Dispatchers.IO) {
        val isFav = aaharDao.isFavorite(spot.id)
        if (isFav) {
            aaharDao.deleteFavoriteSpotById(spot.id)
        } else {
            aaharDao.insertFavoriteSpot(
                FavoriteSpot(
                    id = spot.id,
                    name = spot.name,
                    description = spot.description,
                    iconicFor = spot.iconicFor,
                    location = spot.location,
                    era = spot.established,
                    imageUrl = spot.imageUrl
                )
            )
        }
    }

    // --- Chat Flow & Messages Persistence (Room) ---
    val chatMessages: Flow<List<ChatMessageEntity>> = aaharDao.getChatMessages()

    suspend fun saveChatMessage(sender: String, text: String) = withContext(Dispatchers.IO) {
        aaharDao.insertChatMessage(ChatMessageEntity(sender = sender, text = text))
    }

    suspend fun clearChat() = withContext(Dispatchers.IO) {
        aaharDao.clearChatHistory()
    }

    // --- Gemini AI Storytelling Assistant ---
    suspend fun generateStoryResponse(userPrompt: String, chatHistory: List<ChatMessageEntity> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Namoshkar! The Gemini API key is not configured or configured with the default placeholder in .env. To hear my rich storybooks of Kolkata, please enter your genuine API key in AI Studio's Secrets panel."
        }

        val systemInstructionText = """
            You are "Aahar", an intelligent, culturally rich Bengali culinary concierge and master storyteller. Your mission is to guide food lovers, travelers, and cultural tourists through the legendary culinary history of Kolkata.
            
            Guidelines:
            1. Tone: Warm, deeply engaging, proud, conversational, and poetic about Bengal's intellectual, artistic, and epicurean heritage. Use elegant cultural touches (e.g., greet with 'Namoshkar' or 'Greetings of spice and sweetness').
            2. Content: Infuse high-depth historical narratives. Discuss the stories of generational cooks, historic eras (such as the Colonial era, late 19th-century Bengal Renaissance, Nawab Wajid Ali Shah's exile in 1856, or early Chinese migration), and local culture (especially 'Adda' - our unhurried intellectual street gossiping over tea).
            3. Recommendations: Keep suggestions strictly locked to actual, authentic heritage cafes, cabins, sweet shops, and classic joints of Kolkata. Do NOT recommend modern commercial fast-food or standard global diner franchises (like McDonald's, KFC, Pizza Hut, Subway) or modern post-2010 commercial dining cafes, unless the user specifically demands them. Emphasize legacy places like Mitra Cafe, Royal Indian Hotel, Indian Coffee House, Bhim Chandra Nag, Girish Chandra Dey & Nakur Chandra Nandy, K.C. Das, Anadi Cabin, Dilkhusha Cabin, Eau Chew, Paramount Sherari, Shiraz, Allen's Kitchen, Flurys, etc.
            4. Constraints: Keep responses evocative and story-driven, but relatively concise so they fit perfectly in a beautiful mobile screen (use distinct paragraphs and rich bullet points for readability).
        """.trimIndent()

        // Prepare content structure including conversation history
        val contents = mutableListOf<Content>()
        
        // Map historic chat messages to Gemini's expected conversational flow (limit to last 6 for prompt efficiency)
        val shortHistory = chatHistory.takeLast(6)
        shortHistory.forEach { msg ->
            val role = if (msg.sender == "user") "user" else "model"
            contents.add(Content(role = role, parts = listOf(Part(text = msg.text))))
        }
        
        // Add the current prompt
        contents.add(Content(role = "user", parts = listOf(Part(text = userPrompt))))

        val request = GenerateContentRequest(
            contents = contents,
            generationConfig = GenerationConfig(
                temperature = 0.7,
                maxOutputTokens = 1024
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionText)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "My story has run temporarily quiet! Ask me again, and let us talk about the sweet syrup of Nobin Das's Rossogollas."
        } catch (e: Exception) {
            "Namoshkar! It seems the spice lanes of the internet are temporarily blocked: ${e.localizedMessage}. Ask me of history again soon!"
        }
    }

    // --- Adda Sessions Persistence (Room) ---
    val addaSessions: Flow<List<AddaSessionEntity>> = aaharDao.getAddaSessions()

    suspend fun saveAddaSession(session: AddaSessionEntity) = withContext(Dispatchers.IO) {
        aaharDao.insertAddaSession(session)
    }

    suspend fun deleteAddaSession(session: AddaSessionEntity) = withContext(Dispatchers.IO) {
        aaharDao.deleteAddaSession(session)
    }

    suspend fun clearHistoryAddaSessions() = withContext(Dispatchers.IO) {
        aaharDao.clearAllAddaSessions()
    }

    // --- Search Queries Persistence (Room) ---
    val recentSearchQueries: Flow<List<SearchQueryEntity>> = aaharDao.getRecentSearchQueries()

    suspend fun saveSearchQuery(queryText: String) = withContext(Dispatchers.IO) {
        if (queryText.isNotBlank()) {
            aaharDao.insertSearchQuery(SearchQueryEntity(queryText = queryText.trim()))
        }
    }

    suspend fun deleteSearchQuery(queryText: String) = withContext(Dispatchers.IO) {
        aaharDao.deleteSearchQuery(queryText.trim())
    }

    suspend fun clearSearchHistory() = withContext(Dispatchers.IO) {
        aaharDao.clearSearchHistory()
    }
}
