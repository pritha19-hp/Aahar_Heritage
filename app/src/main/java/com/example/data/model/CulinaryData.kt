package com.example.data.model

data class HeritageDish(
    val id: String,
    val name: String,
    val era: String,
    val originStory: String,
    val keyEateries: List<String>,
    val imageUrl: String
)

data class HeritageSpot(
    val id: String,
    val name: String,
    val description: String,
    val established: String,
    val iconicFor: String,
    val location: String,
    val trivia: String,
    val imageUrl: String
)

data class FoodTrail(
    val id: String,
    val name: String,
    val theme: String,
    val description: String,
    val estimatedTime: String,
    val stops: List<String>, // List of HeritageSpot IDs
    val backstory: String
)

object CulinaryDatabaseSeeds {
    val dishes = listOf(
        HeritageDish(
            id = "rossogolla",
            name = "Rossogolla",
            era = "19th Century Renaissance (1868)",
            originStory = "Invented by the legendary confectioner Nobin Chandra Das in his Bagbazar workshop in 1868. Seeking to create a sweet that wasn't dry, he boiled circles of fresh cottage cheese (chhena) in sweet boiling sugar syrup. The spongy, syrup-laden sphere was a scientific marvel of texture, and it quickly became the crown jewel of Bengal's confectionery identity, breaking traditional limits of caste and social circles.",
            keyEateries = listOf("K.C. Das (Dharmatala)", "Chittaranjan Confectionery (Shambazar)", "Bhim Chandra Nag (Bowbazar)"),
            imageUrl = "https://images.unsplash.com/photo-1589135061699-23194a2861c8?w=500&auto=format&fit=crop" // Beautiful sweet/syrup photo representation
        ),
        HeritageDish(
            id = "kobiraji_cutlet",
            name = "Kobiraji Cutlet",
            era = "Colonial Era Confluence",
            originStory = "A fascinating adaptation of the British cutlet to appeal to Bengali tastes. Legendary story says when a patron requested something spectacular, the chef dipped a mutton or chicken fillet in breadcrumbs and covered it in a dramatic, lacy mesh of whisked deep-fried egg. The dish became colloquially known as 'Kobiraji'—which is the local phonetic evolution of the English term 'Coverage' (covered in egg nest).",
            keyEateries = listOf("Mitra Cafe (Shovabazar)", "Dilkhusha Cabin (College Street)", "Chitto Babur Dokan (Dacres Lane)"),
            imageUrl = "https://images.unsplash.com/photo-1599487488170-d11ec9c172f0?w=500&auto=format&fit=crop" // Fried protein lacy egg representation
        ),
        HeritageDish(
            id = "kolkata_biryani",
            name = "Kolkata Mughlai Biryani",
            era = "Nawabi Exile (1856)",
            originStory = "Born from heartbreak when Nawab Wajid Ali Shah, the last Nawab of Awadh, was exiled by the British to Metiabruz near Kolkata. Strapped for finances but refusing to compromise on elegance, his Royal chefs added slow-cooked fragrant potatoes (Aloo) and boiled eggs. The potato absorbed the majestic juices of ghee, meat, saffron, and screwpine water (Kewra), elevating a simple starch to the soulful signature of Kolkata's unique biryani heritage.",
            keyEateries = listOf("Royal Indian Hotel (Chitpur)", "Shiraz Golden Restaurant (Park Circus)", "Aminia (New Market)"),
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "hakka_noodles",
            name = "Hakka Chinese Cuisine",
            era = "Late 18th Century Migration",
            originStory = "Kolkata is the undisputed cradle of Indian-Chinese cuisine. In the late 1700s, Tong Achew, a Chinese trader, settled near Budge Budge (renamed Achipur). Over generations, the Hakka Chinese community established Tiretta Bazaar and Tangra. Seeking to please local palates, they blended traditional Cantonese and Hakka stir-fry techniques with hot Indian green chillies, dark soy sauce, ginger, and garlic, creating an iconic global food sub-culture.",
            keyEateries = listOf("Eau Chew (Chandni Chowk - India's oldest run Chinese restaurant)", "Beijing (Tangra)", "Tiretta Bazaar Morning Market"),
            imageUrl = "https://images.unsplash.com/photo-1563245372-f21724e3856d?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "mughlai_paratha",
            name = "Mughlai Paratha",
            era = "Mughal Subah Bengal (16th Century)",
            originStory = "According to food lore, Jahangir's chef, Adil Hafiz Usmani, developed this when the Emperor grew bored of traditional flatbreads. It traveled with governors to Bengal as part of the royal entourage. It is a soft dough stretched paper-thin, stuffed with eggs, fresh green chillies, coriander, and minced meat, then folded as an envelope and shallow-fried in bubbling ghee to crisp golden perfection.",
            keyEateries = listOf("Anadi Cabin (Dharmatala)", "Dilkhusha Cabin (College Street)"),
            imageUrl = "https://images.unsplash.com/photo-1626132647523-66f5bf380027?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "kathi_roll",
            name = "Kathi Roll",
            era = "Late British Raj (1932)",
            originStory = "Invented at Nizam's in the bustling New Market area as a clever way to serve dry, juicy grilled mutton kebabs to British patrons who didn't want to get grease on their immaculate hands. The chefs wrapped the hot skewers of kebab in a greasy, paranya-kissed paratha and secured it with clean parchment paper. The word 'Kathi' references the thin bamboo skewers used to grill the meat, capturing the essence of local street-cooking ingenuity.",
            keyEateries = listOf("Nizam's (New Market)", "Kusum Rolls (Park Street)", "Campari (Gariahat)"),
            imageUrl = "https://images.unsplash.com/photo-1626132647523-66f5bf380027?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "puchka",
            name = "Kolkata Puchka",
            era = "Ancient Magadha Origins to Street-Corners of Bengal",
            originStory = "Unlike its North Indian cousin golgappa, the Kolkata Puchka is a masterclass in sensory contrast. Made of whole-wheat semolina shells fried to crispy lightness, it is filled with a spiced mixture of mashed potatoes, freshly ground black salt, yellow peas, cumin, and chopped green chillies. It is then submerged in cold, dark tamarind water infused with fresh mint and Gondhoraj lime juice, providing a spectacular, tangy explosion in a single mouthful.",
            keyEateries = listOf("Dilipda’s Puchka Stall (Vivekananda Park)", "Maharaja Puchka (Southern Avenue)", "Pravesh Puchka (Alipore)"),
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "jhalmuri",
            name = "Jhalmuri",
            era = "World War II Confluence",
            originStory = "A humble snack that attained cult popularity in the narrow lanes of Kolkata during World War II, feeding soldiers and workers. Vendors dynamically throw crunchy puffed rice (muri) with raw, pungent mustard oil, raw onions, fresh coriander, green chillies, boiled potatoes, sprouted peas, roasted peanuts, and a secret house-ground spice blend, before serving it in a traditional fold of newspaper known as 'thonga'.",
            keyEateries = listOf("Maidan Street Vendors (Outside Victoria Memorial)", "Nehru Children's Museum Crossing", "College Street Crossing Vendors"),
            imageUrl = "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "jolbhora_sandesh",
            name = "Jolbhora Sandesh",
            era = "19th Century Zamindari Splendor (1850s)",
            originStory = "Invented by Suryakumar Modak in Rishra, Hooghly, in the mid-19th century. According to lore, a local Zamindar (landlord) requested a confectionery marvel to playfully surprise his newly-wed son-in-law. Modak crafted a beautiful, palm-sized, dry-cottage-cheese (Sandesh) shell shaped like a talshash (palm-fruit kernel), but secretly injected liquid sweet rose syrup into its dense core. When the son-in-law bit into it, the liquid erupted, soaking his fine garments to the amusement of the court. Today, generational confectioners preserve this magnificent 'pak' (cooking control) technique, inserting aromatic liquid Nolen Gur (seasonal date palm jaggery) during winters and rose elixir in summers without cracking the delicate shell.",
            keyEateries = listOf("Girish Chandra Dey & Nakur Chandra Nandy (Hatibagan)", "Bhim Chandra Nag (Bowbazar)", "Balaram Mullick & Radharaman Mullick"),
            imageUrl = "https://images.unsplash.com/photo-1589135061699-23194a2861c8?w=500&auto=format&fit=crop"
        ),
        HeritageDish(
            id = "devilled_crab",
            name = "Victorian Devilled Crab",
            era = "Mid-20th Century Anglo-Indian Peak",
            originStory = "A masterclass in Anglo-Indian integration, where succulent sweet crabmeat is hand-picked, sautéed in sharp local mustard (kasundi) accents and black pepper, folded in creamy bechamel sauce, stuffed back into the natural crab shell, and baked under a thick coat of golden cheese and breadcrumbs.",
            keyEateries = listOf("Mocambo (Park Street Area)", "Tollygunge Club", "Saturday Club"),
            imageUrl = "https://images.unsplash.com/photo-1599487488170-d11ec9c172f0?w=500&auto=format&fit=crop"
        )
    )

    val spots = listOf(
        HeritageSpot(
            id = "mitra_cafe",
            name = "Mitra Cafe",
            description = "A grand institution of North Kolkata's 'Cabin Culture'. The legendary rendezvous point for actors, freedom fighters, and poets, Mitra Cafe's nostalgic wood benches are forever packed.",
            established = "1920",
            iconicFor = "Mutton Kobiraji Cutlet, Fish Diamond Fry",
            location = "Shovabazar Crossing, North Kolkata",
            trivia = "The name 'Mitra' means friend—embodying Bengal's deep-rooted warmth and the tradition of 'Adda' (unhurried intellectual chats over hot chai).",
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "royal_indian",
            name = "Royal Indian Hotel",
            description = "A culinary jewel tucked away in the chaotic bylanes of Chitpur. Famous for preserving Wajid Ali Shah's authentic, potato-free royal Awadhi Biryani and legendary mutton chaap cooked over charcoal stoves since the turn of the century.",
            established = "1905",
            iconicFor = "Royal Mutton Biryani, Shahi Mutton Chaap",
            location = "Chitpur, near Nakhoda Masjid, Central Kolkata",
            trivia = "Unlike typical Kolkata Biryani, Royal continues to serve Avadhi-style biryani WITHOUT potato, staying strictly true to the Lucknow court recipes of Wajid Ali Shah's chef lineage.",
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "coffee_house",
            name = "Indian Coffee House",
            description = "The absolute epicenter of Bengal's intellectual, political, and artistic identity. Under towering double-height ceilings, countless radical socio-cultural movements, novels, films, and revolutions were planned over cups of infusion black coffee.",
            established = "1942 (Albert Hall since 1876)",
            iconicFor = "Mutton Cutlet, Cold Coffee, Infusion tea",
            location = "College Street (Boipara), North Central Kolkata",
            trivia = "Regulars included Nobel laureate Amartya Sen, legendary filmmaker Satyajit Ray, and iconic poet Sunil Gangopadhyay.",
            imageUrl = "https://images.unsplash.com/photo-1498804103079-a6351b050096?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "bhim_nag",
            name = "Bhim Chandra Nag",
            description = "One of Kolkata's oldest sweet shops. Bhim Nag is legendary for inventing the 'Ledikeni' sweet in honor of Lady Canning, the vicereine of India, as well as classic Nolen Gur (date palm jaggery) Sondesh.",
            established = "1826",
            iconicFor = "Nolen Gur Sondesh, Ashu Sandesh",
            location = "Bowbazar, Central Kolkata",
            trivia = "When the famous English clockmaker Cooke & Kelvey visited the shop in 1858, they were astounded that sweet makers couldn't read English clocks. They customized a mechanical clock with Bengali numerals specifically for the shop, which still hangs on its walls!",
            imageUrl = "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "eau_chew",
            name = "Eau Chew",
            description = "Nestled in Central Kolkata, Eau Chew is India's oldest continuously operating, family-run Chinese restaurant. Here, fourth-generation descendents of the Huang family prepare recipes using handmade sauces.",
            established = "1927",
            iconicFor = "Chimney Soup, Roast Pork, Josephine Noodles",
            location = "Ganesh Chandra Avenue, Central Kolkata",
            trivia = "The Josephine Noodles were invented ad-hoc by the legendary matriarch Josephine Huang to feed a large guest who wanted 'something thick and flavorful.'",
            imageUrl = "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "nizams",
            name = "Nizam's",
            description = "The absolute birthground of the globally revered Kathi Roll. Located in the animated alleys of Hoggs Market (New Market), Nizam's continues to serve succulent skewers of roasted mutton seekh wrapped in flaky parathas.",
            established = "1932",
            iconicFor = "Nizam Mutton seekh paratha roll",
            location = "New Market Area, Central Kolkata",
            trivia = "The roll was created so British shoppers could feast on seekh kebabs while browsing markets without leaving greasy spices on their hands or linen clothes.",
            imageUrl = "https://images.unsplash.com/photo-1543353071-10c8ba85a904?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "dilipda_puchka",
            name = "Dilipda's Puchka Crossing",
            description = "A southern citadel of Puchka purists. Situated opposite Vivekananda Park, Dilipda offers crisp semolina puchka filled with potato mash and bathed in chilled, tart lime juice water.",
            established = "1972",
            iconicFor = "Special Gondhoraj Alloo Dum Puchka, Churmur",
            location = "Vivekananda Park, South Kolkata",
            trivia = "Dilipda was one of the early street vendors who systematically introduced Gondhoraj lemon leaf zest and rock-salt blends to differentiate from classic tamarind stocks.",
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "nakur_nandy",
            name = "Girish Chandra Dey & Nakur Chandra Nandy",
            description = "A legendary 180-year-old sanctuary of Sandesh craftsmanship tucked in the bylanes of Hatibagan. They remain fiercely loyal to manual cottage style wood-moulding of Sandesh. Their confectioners maintain centuries-old 'pak' secrets, managing chhena textures without machine assembly.",
            established = "1844",
            iconicFor = "Jolbhora Sandesh, Chocolate Sandesh, Parijat Sandesh",
            location = "Hatibagan, North Kolkata",
            trivia = "Their Sandesh are so historically celebrated that they were flown to London for Abhishek Bachchan & Aishwarya Rai's grand wedding feast, and have been favored by generations of Nobel laureates and freedom fighters.",
            imageUrl = "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "flurys",
            name = "Flurys",
            description = "An iconic, ninety-year-old tearoom nested on Park Street. Founded by Mr. and Mrs. J. Flurys in 1927, it became the undisputed epicenter of British-style morning teas, high confectionery, cream cakes, and Swiss chocolates under the colonial elite.",
            established = "1927",
            iconicFor = "Five-Star English Hot Breakfast, Rum Balls, Peach Melba",
            location = "Park Street, Central Kolkata",
            trivia = "At its peak, Flurys was known as the 'Queen of Park Street' where British administrators, royal families, and Bengal's high society met. Their legendary Rum Balls and Almond Pastries remain an emotional Christmas ritual for residents.",
            imageUrl = "https://images.unsplash.com/photo-1498804103079-a6351b050096?w=500&auto=format&fit=crop"
        ),
        HeritageSpot(
            id = "mocambo",
            name = "Mocambo",
            description = "A legendary restaurant on Free School Street that introduced India to the glamorous theatrics of fine continental dining in the 1950s. Its trademark red velvet chairs, low-hung silk lampshades, and live band created a legendary sanctuary for Anglo-Indian, British, and classic Teutonic dishes.",
            established = "1956",
            iconicFor = "Devilled Crab, Baked Alaska, Beckti Mauniere",
            location = "Park Street Area, Central Kolkata",
            trivia = "Mocambo was the first restaurant in Kolkata to introduce the sizzling flambé style at tables, where waiters dressed in immaculate white bandhgala jackets served sizzling baked Alaska.",
            imageUrl = "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=500&auto=format&fit=crop"
        )
    )

    val trails = listOf(
        FoodTrail(
            id = "colonial_cabins",
            name = "Colonial Cabin & Chat Adda Trail",
            theme = "19th Century Renaissance & Intellectual Adda",
            description = "Walk through the historic 'Boipara' of College Street and North Kolkata's charming alleyways. Visit wood-paneled cabins that fed secret independence movements over lacy egg cutlets and deep-drip dark coffee.",
            estimatedTime = "3 Hours",
            stops = listOf("coffee_house", "mitra_cafe", "bhim_nag"),
            backstory = "This trail takes you back to an era where Bengal's creative minds and revolutionists gathered under covert private tables (Cabins) to escape colonial police surveillance while pioneering high-quality literary modernism."
        ),
        FoodTrail(
            id = "nawabi_royal",
            name = "Alleys of Wajid Ali: The Nawab's Feast",
            theme = "Royal Awadhi & Mughlai Confluence",
            description = "Trace the scent of sandalwood, kewra rose water, and slow-cooking spices. Journey through historic Bowbazar, Chitpur, and central commercial lanes to discover biryanis, chaaps, and sweet parathas.",
            estimatedTime = "4 Hours",
            stops = listOf("royal_indian"),
            backstory = "When Wajid Ali Shah's court shifted to Metiabruz, the soul of Lucknow merged permanently with the spirit of Bengal, yielding a golden era of spiced meat, slow mutton broths, and the legendary aromatic potato."
        ),
        FoodTrail(
            id = "silk_road_chinatown",
            name = "Silk & Soy: Tangra Chinatown Trail",
            theme = "Chinese-Bengali Fusion Migration",
            description = "Explore Central Chandni lanes and the old Chinese quarter in Tiretta Bazaar. Engage in the culinary cross-pollination of authentic Canton sauces with the fiery red chillies of Bengal.",
            estimatedTime = "3 Hours",
            stops = listOf("eau_chew"),
            backstory = "The Hakka Chinese who fled civil unrest in China built leather tanneries in Tangra. They cooked traditional family dishes but added local green chillies, ginger paste, and mustard accents to satisfy Bengali food lovers, creating a cultural bridge that remains iconic."
        ),
        FoodTrail(
            id = "street_food_safari",
            name = "Street Food Safari: Lanes of Spice & Tang",
            theme = "Street Ingenuity & Spicy Textures",
            description = "Wander through the historic squares and street alleys where Kolkata's iconic rolls, puchkas, and crunchy jhalmuri were forged. Enjoy mustard oils, flaky paratha ghee, and mint-and-tamarind Gondhoraj water.",
            estimatedTime = "2.5 Hours",
            stops = listOf("nizams", "dilipda_puchka"),
            backstory = "Kolkata’s culinary genius resides not just in grand estates or wood-paneled dining rooms, but in the vibrant, open theater of her active streets, utilizing simple starches to create global sensations."
        ),
        FoodTrail(
            id = "secrets_of_sweets",
            name = "Secrets of Confectionery: The Sweet Renaissance",
            theme = "Generational Cottage-Cheese Craftsmanship",
            description = "Delve deep into the holy trinity of Bengal's cottage-cheese sweet revolution. From the liquid-bursting Jolbhora Sandesh to the spongy, syrup-boiled Rossogolla, discover the precise physical chemistry of Kolkata's historic sweet shops.",
            estimatedTime = "2.5 Hours",
            stops = listOf("bhim_nag", "nakur_nandy"),
            backstory = "Prior to the 19th Century, Bengali sweets were mostly made from sugar, rice flour, and coconut. The Portuguese taught locals curdling processes, which Bengali confectioners (Moiras) brilliantly transformed into 'Chhena', birthing a golden confectionery renaissance."
        ),
        FoodTrail(
            id = "colonial_aristocracy",
            name = "Colonial Grandeur & Continental Feasts",
            theme = "Victorian Tea Rooms & Anglo-Indian Dining Room",
            description = "Stroll down the tree-lined avenues of Park Street and Free School lanes to revel in the golden age of European-style high tea, live jazz banquet halls, and spectacular flambe desserts.",
            estimatedTime = "3 Hours",
            stops = listOf("flurys", "mocambo"),
            backstory = "From Anglo-Indian breakfast tables in country cottages to live band dinners in the mid-century, Kolkata refined European baking and culinary service with local tastes, establishing an iconic culture that still defines Park Street's festive seasons."
        )
    )
}

