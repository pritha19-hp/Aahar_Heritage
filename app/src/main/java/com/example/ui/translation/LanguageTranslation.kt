package com.example.ui.translation

import com.example.data.model.HeritageSpot
import com.example.data.model.HeritageDish
import com.example.data.model.FoodTrail

object LanguageTranslation {
    private val translationMap = mapOf(
        "Explore" to "অন্বেষণ",
        "Trails" to "খাদ্য যাত্রা",
        "Aahar AI" to "আহার এআই",
        "Bookmarks" to "বুকমার্ক",
        "Search eateries..." to "খাবারের দোকান খুঁজুন...",
        "Search food trials..." to "খাদ্য যাত্রা অনুসন্ধান...",
        "Established" to "স্থাপিত",
        "Established in" to "ঐতিহাসিক কাল থেকে স্থাপিত:",
        "Iconic For" to "যা বিশেষভাবে বিখ্যাত",
        "Location" to "অবস্থান",
        "Trivia & Folklore" to "ইতিহাস ও লোককথা",
        "Ask Concierge Aahar" to "আহার সহায়িকাকে জিজ্ঞাসা করুন",
        "🔎 Search Neural Archives and Eateries" to "🔎 ঐতিহাসিক খাবার ও ভোজনালয় খুঁজুন",
        "Search eateries, dishes, or eras (e.g. Mughal, Colonial, Sweets)" to "খাবারের দোকান, ঐতিহ্যবাহী পদ বা যুগ অনুযায়ী খুঁজুন (যেমন মোগল, কলোনিয়াল, বা মিষ্টি)",
        "Suggested Historical Queries / Eateries:" to "প্রস্তাবিত ঐতিহাসিক অনুসন্ধানসমূহ:",
        "Recent Searches" to "সাম্প্রতিক অনুসন্ধানসমূহ",
        "Clear All" to "সব মুছে ফেলুন",
        "No recent searches found." to "কোনো সাম্প্রতিক অনুসন্ধান পাওয়া যায়নি।",
        "Legendary Dishes" to "ঐতিহাসিক খাবার পদ",
        "Heritage Eateries" to "ঐতিহ্যবাহী ভোজনালয়",
        "🗺️ 'Adda' Map" to "🗺️ আড্ডার মানচিত্র",
        "📷 AI Archival Gallery" to "📷 ঐতিহাসিক গ্যালারি",
        "🎭 Adda Moods" to "🎭 আড্ডার মেজাজ",
        "Mughal" to "মোগলাই",
        "Colonial" to "ঔপনিবেশিক",
        "Sweets" to "মিষ্টি",
        "North Kolkata" to "উত্তর কলকাতা",
        "Anglo-Indian" to "অ্যাংলো-ইন্ডিয়ান",
        "Try searching for 'Mughal', 'Sweets' or 'Colonial'." to "মোগলাই, মিষ্টি বা ঔপনিবেশিক লিখে খোঁজার চেষ্টা করুন।",
        "Try searching for 'College Street', 'North' or 'Chitpur'." to "কলেজ স্ট্রিট, উত্তর কলকাতা বা চিতপুর লিখে খোঁজার চেষ্টা করুন।",
        "No matching legendary dishes found" to "কোনো ঐতিহাসিক খাবার পদ পাওয়া যায়নি",
        "No matching eateries found" to "কোনো ঐতিহ্যবাহী খাবারের দোকান মেলেনি",
        "Specialty" to "বিশেষ আকর্ষণ",
        "LOCATION" to "অবস্থান",
        "CRITICAL SPECIALTIES" to "বিশেষ রন্ধনশৈলী",
        "CULTURAL NARRATIVE" to "ঐতিহাসিক পটভূমি ও সংস্কৃতি",
        "Historic Trivia" to "ঐতিহাসিক তথ্য ও লোককথা",
        "HISTORICAL ORIGIN" to "ঐতিহাসিক সূত্র ও উৎস",
        "AUTHENTIC HERITAGE SPOTS TO ENJOY THIS" to "এটি উপভোগ করার আদি ও খাঁটি ভোজনালয়সমূহ",
        "WALK THE TIMELINE" to "পথরেখা অন্বেষণ করুন",
        "HISTORICAL LEGACY backstory" to "ঐতিহাসিক পটভূমি ও প্রেক্ষাপট",
        "TRAIL TIMELINE STOPS" to "খাদ্য যাত্রার প্রধান স্টপসমূহ",
        "Connect with Aahar AI for Stories ⚜️" to "ঐতিহাসিক গল্পের জন্য আহার এআই-এর সাথে যুক্ত হোন ⚜️",
        "Ask Aahar to Tailor Walk Details ⚜️" to "খাদ্য যাত্রার বিশেষ গাইডলাইনের জন্য আহার এআই-কে বলুন ⚜️",
        "Favorited" to "পছন্দনীয় তালিকায় যুক্ত",
        "Add to Favorites" to "পছন্দনীয় তালিকায় যুক্ত করুন",
        "Play Heritage Audio Legend" to "ঐতিহ্যের অডিও গল্প শুনুন",
        "Stop Audio Story" to "গল্প থামান",
        "Historical Narrative" to "ঐতিহাসিক পটভূমি ও গল্প",
        "Key Heritage Eateries" to "মূল ঐতিহ্যবাহী ভোজনালয়",
        "Era & Provenance" to "যুগ ও উৎস",
        "ESTIMATED EXPLORATION TIME" to "আনুমানিক অন্বেষণ সময়",
        "STOPS ALONG THIS TRAIL" to "এই খাদ্য যাত্রার প্রধান স্টপসমূহ",
        "BACKSTORY & CULTURAL SIGNIFICANCE" to "প্রেক্ষাপট ও সাংস্কৃতিক গুরুত্ব",
        "Kolkata Historical Archival AI" to "কলকাতা ঐতিহাসিক গ্যালারি এআই",
        "Reconstruct black and white sensory archives" to "সাদাকালো স্মৃতি থেকে রঙিন অনুভূতি পুনর্জীবিত করুন",
        "Custom Visual Focus (e.g. warm wood, boiling chai)" to "বিশেষ দৃষ্টি আকর্ষণ (যেমন: চায়ের ধোঁয়া, কাঠের কেবিন)",
        "✨ REIMAGINE ARCHIVAL PHOTO IN BENGAL COLORS" to "✨ রঙিন স্মৃতি ও অনুভূতি পুনর্নির্মাণ করুন",
        "Distilling memories..." to "স্মৃতি রোমন্থন হচ্ছে...",
        "🎨 SENSORY RECONSTRUCTION REPORT" to "🎨 রঙিন দৃশ্য ও শব্দ পুনর্নির্মাণ",
        "Save to Journal" to "দিনলিপিতে সংরক্ষণ করুন",
        "Your AI Culinary Guide" to "আপনার নিজস্ব কুশলী খাবার সহায়িকা",
        "Ask about recipes, cabin secrets, or Nawabi lore of Kolkata..." to "কলকাতার কেবিন কালচার, নবাবী ইতিহাস বা রান্নার প্রণালী জানুন...",
        "Send Prompt" to "বার্তা পাঠান",
        "Clearing memories..." to "মনোযোগ সরানো হচ্ছে...",
        "No favorites saved yet. Highlight spots while browsing to populate your personal list." to "কোনো ঐতিহাসিক স্থান এখনো আপনার তালিকায় যুক্ত করেননি। অন্বেষণের সময় হৃদয় চিহ্ন ট্যাপ করে যুক্ত করুন।",
        "Savor Kolkata's History" to "কলকাতার ইতিহাস আস্বাদন করুন",
        "From high colonial cabins where secret freedom movements were planned over lacy egg cutlets, to 19th-century sugar renaissance that spawned the Rossogolla. Trace the stories of generational cooks in Bengal's unhurried 'Adda' era." to "জালের মতো ডিমের কবিরাজি কাটলেটের আড়ালে ঔপনিবেশিক পুলিশের নজর বাঁচিয়ে স্বদেশের বিপ্লবী আন্দোলনের পরিকল্পনা থেকে শুরু করে ১৯ শতকের স্পঞ্জি রসগোল্লার রেনেসাঁ—আসুন, অন্বেষণ করুন কলকাতার সেই সব মায়াময় খাবারের পেছনের কালজয়ী ইতিহাস।",
        "3 Hours" to "৩ ঘন্টা",
        "4 Hours" to "৪ ঘন্টা",
        "2.5 Hours" to "২.৫ ঘন্টা",
        "Specialty: " to "বিশেষ আকর্ষণ: ",
        "No matching legendary dishes or eateries found" to "মিলে যাওয়া কোনো ঐতিহ্যবাহী পদ বা ভোজনালয় খুঁজে পাওয়া যায়নি"
    )

    fun translate(text: String, isBengali: Boolean): String {
        if (!isBengali) return text
        return translationMap[text] ?: text
    }
}

// HeritageSpot local translation helpers
fun HeritageSpot.localizedName(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "mitra_cafe" -> "মিত্র ক্যাফে"
            "royal_indian", "spot_royal" -> "রয়েল ইন্ডিয়ান হোটেল"
            "coffee_house", "spot_coffee_house" -> "ইন্ডিয়ান কফি হাউস"
            "bhim_nag", "spot_bhim_nag" -> "ভীমচন্দ্র নাগ"
            "eau_chew" -> "ও চিউ"
            "nizams" -> "নিজামস"
            "dilipda_puchka" -> "দিলীপদার ফুচকা"
            "nakur_nandy" -> "গিরিশ চন্দ্র দে এবং নকুর চন্দ্র নন্দী"
            "flurys" -> "ফ্লুরিস"
            "mocambo" -> "مোক্যাম্বো"
            "spot_dilkhusha" -> "দিলখুশা কেবিন"
            "spot_paramount" -> "প্যারামাউন্ট শরবত ও সিরাপ"
            else -> LanguageTranslation.translate(name, isBengali)
        }
    } else {
        name
    }
}

fun HeritageSpot.localizedLocation(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "mitra_cafe" -> "শোভাবাজার ক্রসিং, উত্তর কলকাতা"
            "royal_indian", "spot_royal" -> "চিতপুর রোড (মহাত্মা গান্ধী রোডের সংযোগস্থলের পাশে), জোড়াসাঁকো, কলকাতা"
            "coffee_house", "spot_coffee_house" -> "কলেজ স্ট্রিট, কলকাতা (প্রেসিডেন্সি বিশ্ববিদ্যালয়ের ঠিক বিপরীতে)"
            "bhim_nag", "spot_bhim_nag" -> "বউবাজার, মধ্য কলকাতা"
            "eau_chew" -> "গণেশ চন্দ্র এভিনিউ, মধ্য কলকাতা"
            "nizams" -> "নিউ মার্কেট এলাকা, মধ্য কলকাতা"
            "dilipda_puchka" -> "বিবেকানন্দ পার্ক, দক্ষিণ কলকাতা"
            "nakur_nandy" -> "হাতিবাগান, উত্তর কলকাতা"
            "flurys" -> "পার্ক স্ট্রিট, মধ্য কলকাতা"
            "mocambo" -> "পার্ক স্ট্রিট এলাকা, মধ্য কলকাতা"
            "spot_dilkhusha" -> "কলেজ স্ট্রিট, কলকাতা"
            "spot_paramount" -> "বঙ্কিম চ্যাটার্জি স্ট্রিট (কলেজ স্কয়ারের কোণ), কলেজ স্ট্রিট, কলকাতা"
            else -> LanguageTranslation.translate(location, isBengali)
        }
    } else {
        location
    }
}

fun HeritageSpot.localizedIconicFor(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "mitra_cafe" -> "মটন কবিরাজি কাটলেট, ফিশ ডায়মন্ড ফ্রাই"
            "royal_indian", "spot_royal" -> "লখ্নৌ ঘরানার মটন বিরিয়ানি ও রেশমি কাবাব"
            "coffee_house", "spot_coffee_house" -> "গরম কফি, ফাউল কাটলেট এবং বিপ্লবী আড্ডা"
            "bhim_nag", "spot_bhim_nag" -> "লেডিকেনি সুইট, তালশাঁস সন্দেশ ও জলভরা সন্দেশ"
            "eau_chew" -> "চিমনি স্যুপ, রোস্ট পর্ক, জোসেফাইন নুডুলস"
            "nizams" -> "নিজাম মটন শিক পরাঠা রোল"
            "dilipda_puchka" -> "বিশেষ গন্ধরাজ আলু দম ফুচকা, চুরমুর"
            "nakur_nandy" -> "জলভরা সন্দেশ, চকোলেট সন্দেশ, পারিজাত সন্দেশ"
            "flurys" -> "ফাইভ-স্টার ইংলিশ ব্রেকফাস্ট, রাম বল, পিচ মেলবা"
            "mocambo" -> "ডেভিলড ক্র্যাব, বেকড আলাস্কা, বেকটি মনিয়েরে"
            "spot_dilkhusha" -> "ডিমের কবিরাজি কাটলেট ও দই পোলাও"
            "spot_paramount" -> "ডাবের শরবত ও সুগন্ধি চকোলেট সিরাপ"
            else -> LanguageTranslation.translate(iconicFor, isBengali)
        }
    } else {
        iconicFor
    }
}

fun HeritageSpot.localizedDescription(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "mitra_cafe" -> "উত্তর কলকাতার 'কেবিন কালচার'-এর এক মহিমান্বিত প্রতিষ্ঠান। অভিনেতা, স্বাধীনতা সংগ্রামী ও কবিদের প্রিয় মিলনস্থল মিত্র ক্যাফের নস্টালজিক কাঠের বেঞ্চ সবসময় আড্ডায় মুখরিত থাকে।"
            "royal_indian", "spot_royal" -> "১৮৫৬ সালে অবধের শেষ নবাব ওয়াজিদ আলী শাহর নির্বাসনের রাজকীয় রাঁধুনিদের বংশধরদের দ্বারা ১৯০৫ সালে শুরু হওয়া এই জোড়াসাঁকোর রত্নটি কলকাতার সেরা মুঘাল খাবার পরিবেশন করে।"
            "coffee_house", "spot_coffee_house" -> "একটি ধোঁয়াময় ও ঐতিহ্যবাহী হল যেখানে বাংলা রেনেসাঁ এবং বুদ্ধিজীবীরা মিলিত হতেন। রবীন্দ্রনাথ, সত্যজিৎ রায় এবং বুদ্ধিজীবীদের প্রিয় মিলনস্থল এটি।"
            "bhim_nag", "spot_bhim_nag" -> "১৮২৬ সালে স্থাপিত এই মহান মিষ্টির দোকানটি ভারতের প্রথম গভর্নর-জেনারেলের স্ত্রী লেডি ক্যানিংয়ের জন্য তৈরি স্পেশাল মিষ্টি 'লেডিকেনি' বানিয়ে বিখ্যাত হয়েছিল।"
            "eau_chew" -> "মধ্য কলকাতায় অবস্থিত ও চিউ হলো ভারতের সবচেয়ে পুরনো এবং একটানা চালু থাকা একটি পারিবারিক চাইনিজ রেস্তোরাঁ। এখানে হুয়াং পরিবারের চতুর্থ প্রজন্মের সদস্যরা হাতে তৈরি সস ব্যবহার করে আসল চাইনিজ খাবার পরিবেশন করেন।"
            "nizams" -> "বিশ্বজুড়ে সমাদৃত কাঠি রোলের পরম জন্মস্থান। নিউ মার্কেটের কোলাহলপূর্ণ গলিতে অবস্থিত নিজামস আজও পরোটায় মোড়ানো সুস্বাদু এবং নরম মটন শিক কাবাব পরিবেশন করে চলেছে।"
            "dilipda_puchka" -> "দক্ষিণ কলকাতার ফুচকা রসিকদের এক অন্যতম আস্তানা। বিবেকানন্দ পার্কের বিপরীতে অবস্থিত দিলীপদা মুচমুচে ফুচকার ভেতরে মশলাদার আলুর পুর ভরে হালকা টক লেবুর জল দিয়ে পরিবেশন করেন।"
            "nakur_nandy" -> "হাতিবাগানের গলিতে অবস্থিত একটি ঐতিহ্যবাহী ১৮০ বছরের পুরনো সন্দেশের স্বর্গরাজ্য। যান্ত্রিকতার স্পর্শ ছাড়া আজও তারা কাঠের ছাঁচে হাতে সন্দেশ তৈরি করে চলেছেন।"
            "flurys" -> "পার্ক স্ট্রিটের বিখ্যাত ঐতিহ্যশালী ৯০ বছরের পুরনো একটি টি-রুম। ১৯২৭ সালে জুরাস ফ্লুরিস এবং তার স্ত্রী এটি স্থাপন করেন যা সাহেবি প্রাতরাশ ও চকোলেটের জন্য জনপ্রিয়।"
            "mocambo" -> "১৯৫০-এর দশকে কলকাতার মানুষকে ইউরোপীয় রাজকীয় ধাঁচের খাবারের স্বাদ এনে দেওয়া এক ঐতিহ্যবাহী রেস্তোরাঁ। এর চমৎকার লাল মখমল চেয়ার নস্টালজিক আবহ তৈরি করে।"
            "spot_dilkhusha" -> "একটি চমৎকার ও খাঁটি কেবিন রেস্তোরাঁ যা গত ১০০ বছর ধরে চলে আসছে। ব্রিটিশ পুলিশ এবং স্বদেশী নজরদারী এড়িয়ে বিপ্লবীরা এখানে দেখা করতেন এবং রুটি-কাটলেটের আড়ালে ভারতের স্বাধীনতার নকশা পরিকল্পনা করেছিলেন।"
            "spot_paramount" -> "বিশ্বের অন্যতম সেরা শরবত পার্লার। সুভাষ চন্দ্র বসু থেকে শুরু করে আচার্য প্রফুল্ল চন্দ্র রায় এখানে আড্ডার সময় ডাবের মৃদু ঠান্ডা জল দিয়ে তৃষ্ণা মেটাতেন।"
            else -> LanguageTranslation.translate(description, isBengali)
        }
    } else {
        description
    }
}

fun HeritageSpot.localizedTrivia(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "mitra_cafe" -> "মিত্র নামের অর্থ বন্ধু—বাঙালির আন্তরিক আতিথেয়তা এবং ঐতিহাসিক চায়ের আড্ডার এক পরম নিদর্শন।"
            "royal_indian", "spot_royal" -> "রয়েল বিরিয়ানি তৈরিতে কোনো আলু দেওয়া হয় না, এটি পুরোপুরি নবাবী লখ্নৌ মসলা ও সুগন্ধি ভাতের নিখুঁত মেলবন্ধন।"
            "coffee_house", "spot_coffee_house" -> "মন্না দের গাওয়া কালজয়ী গান 'কফি হাউসের সেই আড্ডাটা আজ আর নেই' এই ঐতিহাসিক স্থানটিকে অমর করে রেখেছে।"
            "bhim_nag", "spot_bhim_nag" -> "বিখ্যাত ঘড়ি নির্মাতা কুক অ্যান্ড কেলভি মিষ্টি কারিগরদের সময় বোঝার সুবিধার্থে বিশেষ বাংলা সংখ্যা লেখা ঘড়ি উপহার দিয়েছিলেন যা আজও এখানকার দেয়ালে ঝুলছে।"
            "eau_chew" -> "এখানকার চর্বিহীন অথচ সুস্বাদু জোসেফাইন নুডুলস মূলত রেস্তোরাঁর প্রধান কারিগর জোসেফাইন হুয়াং এক ক্ষুধার্ত অতিথির জন্য চটজলদি উদ্ভাবন করেছিলেন।"
            "nizams" -> "সাহেবরা যাতে হাত না নোংরা করে বা পোশাকে মশলাদার তেল না লাগিয়ে সহজে কাবাব উপভোগ করতে পারেন, সেই সুবিধার্থেই নিজামসের রাঁধুনিরা এই রোল সংস্করণ গড়েছিলেন।"
            "dilipda_puchka" -> "দিলীপদা প্রথম দিককার একজন ফুচকা বিক্রেতা যিনি তেঁতুল জলের পাশাপাশি ঐতিহ্যবাহী গন্ধরাজ লেবুর রস ও পাতা ব্যবহার করে এই স্বাদের অভূতপূর্ব পরিবর্তন এনেছিলেন।"
            "nakur_nandy" -> "তাঁদের তৈরি সন্দেশ এতটাই আদৃত যে তা মুম্বইয়ের বিনোদন তারকা অভিষেক বচ্চন ও ঐশ্বর্য রাইয়ের রাজকীয় বিয়েতে কলকাতা থেকে বিমানে পাঠিয়ে পরিবেশন করা হয়েছিল।"
            "flurys" -> "ঐতিহাসিকদের মতে, ইংরেজ রাজপুরুষদের উচ্চ আড্ডার অন্যতম প্রিয় আস্তানা ছিল এই পার্ক স্ট্রিট ফ্লুরিস।"
            "mocambo" -> "টেবিলের পাশে এনে খাবার পরিবেশন করার সাথে সাথে আগুন জ্বালিয়ে পরিবেশন করার (flambé) আভিজাত্য মোক্যাম্বোই প্রথম কলকাতায় শুরু করেছিল।"
            "spot_dilkhusha" -> "এর বিখ্যাত 'কবিরাজি' শব্দটি মূলত ইংরেজি 'Coverage' শব্দটির কথ্য বিবর্তন, কারণ কাটলেটের ওপর ডিমের একটি ஜালের মতো আবরণ বা কভারেজ দেওয়া থাকে।"
            "spot_paramount" -> "এই দোকানের জনপ্রিয় ডাবের শরবতের রেসিপিটি মূলত রসায়নবিদ জগদীশ চন্দ্র বসুর তৈরি পরামর্শ থেকে নেওয়া হয়েছিল।"
            else -> LanguageTranslation.translate(trivia, isBengali)
        }
    } else {
        trivia
    }
}

// HeritageDish local translation helpers
fun HeritageDish.localizedName(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "kobiraji_cutlet", "dish_kabiraji" -> "ডিম কবিরাজি কাটলেট"
            "kolkata_biryani", "dish_biryani" -> "কলকাতা মটন বিরিয়ানি"
            "rossogolla", "dish_rossogolla" -> "স্পঞ্জ রসগোল্লা"
            "jolbhora_sandesh", "dish_sandesh" -> "নলেন গুড়ের সন্দেশ"
            "hakka_noodles" -> "হাক্কা চাইনিজ নুডুলস"
            "mughlai_paratha" -> "মোগলাই পরোটা"
            "kathi_roll" -> "কাঠি রোল"
            "puchka" -> "কলকাতা পুচকা"
            "jhalmuri" -> "ঝালমুড়ি"
            "devilled_crab" -> "ভিক্টোরিয়ান ডেভিলড ক্র্যাব"
            "dish_kochuri" -> "ছোলার ডাল দিয়ে হিংয়ের কচুরি"
            else -> LanguageTranslation.translate(name, isBengali)
        }
    } else {
        name
    }
}

fun HeritageDish.localizedEra(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "kobiraji_cutlet", "dish_kabiraji" -> "ঔপনিবেশিক যুগ (১৮৯০-এর দশক)"
            "kolkata_biryani", "dish_biryani" -> "নবাবী নির্বাসন যুগ (১৮৫৬)"
            "rossogolla", "dish_rossogolla" -> "বাংলা মিষ্টির রেনেসাঁ (১৮৬৮)"
            "jolbhora_sandesh", "dish_sandesh" -> "ঐতিহ্যবাহী বাংলার বসন্তকাল"
            "hakka_noodles" -> "১৮ শতকের শেষের চীনা অভিবাসন"
            "mughlai_paratha" -> "১৬ শতকের মোগল সুবা বাংলা"
            "kathi_roll" -> "দেরী ব্রিটিশ রাজ (১৯৩২)"
            "puchka" -> "প্রাচীন মগধ থেকে বাংলার অলিগলি"
            "jhalmuri" -> "দ্বিতীয় বিশ্বযুদ্ধকালীন কালখণ্ড"
            "devilled_crab" -> "২০ শতকের মাঝামাঝি অ্যাংলো-ইন্ডিয়ান সোনালী সময়"
            "dish_kochuri" -> "স্বাধীনতার আগের শতাব্দী প্রাচীন প্রাতরাশ"
            else -> LanguageTranslation.translate(era, isBengali)
        }
    } else {
        era
    }
}

fun HeritageDish.localizedStory(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "kobiraji_cutlet", "dish_kabiraji" -> "ব্রিটিশ কাটলেটকে ভারতীয় স্বাদে রূপান্তরের এক অনবদ্য সৃষ্টি। জনশ্রুতি রয়েছে, এক ভোজনরসিকের বিশেষ অনুরোধে বাবুর্চি মটনের কাটলেটকে সুস্বাদু ডিমের ফাঁপা জালে জাঁকজমকপূর্ণভাবে মুড়ে দিয়েছিলেন, যা পরে 'কভারেজ' বা স্থানীয় মুখে 'কবিরাজি' নামে অমর হয়।"
            "kolkata_biryani", "dish_biryani" -> "১৮৫৬ সালে কলকাতার মেটিয়াবুরুজে নির্বাসিত অবধের নবাব ওয়াজিদ আলী শাহর রাজকীয় ভাঁড়ার থেকে এই সুস্বাদু আহারের উৎপত্তি। কথিত আছে, অর্থ সংকুচিত হওয়ায় নবাবের প্রধান বাবুর্চি মাংসের পরিমাণ কমিয়ে আলু ও সেদ্ধ ডিমের মেলবন্ধন দিয়েছিলেন, যা আজ কলকাতার বিরিয়ানির সিগনেচার রূপ।"
            "rossogolla", "dish_rossogolla" -> "১৮৬৮ সালে বাগবাজারের ময়দা-কনফেকশনার নোবিন চন্দ্র দাস রসগোল্লা আবিষ্কার করে মিষ্টি জগতে বিপ্লব এনেছিলেন। তুলতুলে ছানার গোলাকে চিনির ফুটন্ত রসে ফুটিয়ে এই কোমল অমর সৃষ্টি গড়া হয়।"
            "jolbhora_sandesh", "dish_sandesh" -> "ঐতিহ্যবাহী ছানা ও শীতকালের বিশেষ খেজুরের রস থেকে তৈরি খাঁটি সুগন্ধি নলেন গুড়কে একসাথে ফুটিয়ে ও ছাঁচে ফেলে সন্দেশের অপরূপ রূপ দেওয়া হয়।"
            "hakka_noodles" -> "কলকাতা ও কলকাতার ট্যাংরা হলো ভারতীয় চাইনিজ রান্নার আদি পীঠস্থান। ১৭০০ সালের শেষের দিকে চীনা বণিক টং আচিউ এ দেশে এসে বসতি গড়েন। পরবর্তীতে চীনা জাতিভুক্ত হাক্কা সম্প্রদায় স্থানীয় মানুষের স্বাদ অনুযায়ী কাঁচা লঙ্কা ও আদার ফিউশনে হাক্কা নুডুলস বানিয়ে বিশ্বজুড়ে সমাদৃত এক ফুড-কালচার গড়ে তোলে।"
            "mughlai_paratha" -> "মুঘল বাদশাহ জাহাঙ্গীরের রাঁধুনি আদিল হাফিজ উসমানি রাজকীয় রুচির পরিবর্তনের জন্য এটি তৈরি করেন। পাতলা ময়দার আবরণের ভেতরে ডিম, পেঁয়াজ, কাঁচা লঙ্কা এবং কীমা পুরে দিয়ে ঘিয়ে কড়া করে ভেজে এটি পরিবেশন করা হতো।"
            "kathi_roll" -> "কলকাতার নিউ মার্কেট কোলাহলপূর্ণ এলাকায় অবস্থিত নিজামস দোকানে এই কাঠি রোলের প্রথম সৃষ্টি। সাহেব ক্রেতারা যাতে কেনাকাটার ছলে হাত নোংরা না করে সহজে গরম কাবাব খেতে পারেন, সেই চিন্তা থেকেই পরোটার ভেতরে গ্রিলড কাবাব মুড়িয়ে পরিবেশন করা হতো।"
            "puchka" -> "উত্তর ভারতের গোলগাপ্পার চেয়ে কলকাতার পুচকা অনেক আলাদা। কুড়কুড়ে ময়দা-সুজির আবরণের ভেতরে মশলাদার চটকানো আলুর পুর এবং গন্ধরাজ লেবু নিংড়ানো ঠান্ডা টক তেঁতুল জল দিয়ে এর পরম স্বর্গীয় তৃপ্তি দেওয়া হয়।"
            "jhalmuri" -> "দ্বিতীয় বিশ্বযুদ্ধ চলাকালীন কলকাতার ব্যস্ত গলিপথগুলোতে এটি অত্যন্ত জনপ্রিয় হয়ে ওঠে। মুড়ির সাথে খাঁটি ঝাঁঝালো সর্ষের তেল, পেঁয়াজ কুচি, ধনেপাতা, আলু কাঁচা লঙ্কা ও বিশেষ মশলার সংমিশ্রণে ঠোঙায় করে পরিবেশন করা হয়।"
            "devilled_crab" -> "অ্যাংলো-ইন্ডিয়ান মেলবন্ধনের এক অনন্য সৃষ্টি, যেখানে কাঁকড়ার নরম মাংস কুচিয়ে সর্ষের ঝাঁঝ ও গোলমরিচ দিয়ে সুস্বাদু ক্রিম সস সমেত কাঁকড়ার খোসার ভেতরে পুরে ওপরে চিজ ও বিস্কুটের গুঁড়ো ছড়িয়ে সেঁকে তৈরি করা হয়।"
            "dish_kochuri" -> "কলকাতার ঐতিহ্যবাহী গলির মিষ্টির দোকানগুলোতে সকাল বেলার সেরা জলখাবার। তাজা ময়দা ও হিংয়ের কচুরি সাথে গরম মশলাদার ও মিষ্টি ছোলার ডাল বাঙালির দিন শুরুর পরম সঙ্গী।"
            else -> LanguageTranslation.translate(originStory, isBengali)
        }
    } else {
        originStory
    }
}

// FoodTrail local translation helpers
fun FoodTrail.localizedName(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "colonial_cabins" -> "ঔপনিবেশিক কেবিন ও চায়ের আড্ডা ট্রেইল"
            "nawabi_royal" -> "ওয়াজিদ আলী শাহর গলি: নবাবী ভোজ"
            "silk_road_chinatown" -> "সিল্ক ও সয়াবিন: ট্যাংরা চায়নাটাউন ট্রেইল"
            "street_food_safari" -> "স্ট্রিট ফুড সাফারি: ঝাল ও স্বাদের গলি"
            "secrets_of_sweets" -> "মিষ্টি তৈরির গোপন রহস্য: মিষ্টির রেনেসাঁ"
            "colonial_aristocracy" -> "ঔপনিবেশিক আভিজাত্য ও কন্টিনেন্টাল ভোজ"
            else -> LanguageTranslation.translate(name, isBengali)
        }
    } else {
        name
    }
}

fun FoodTrail.localizedTheme(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "colonial_cabins" -> "১৯ শতকের রেনেসাঁ ও বুদ্ধিজীবীদের আড্ডা"
            "nawabi_royal" -> "রাজকীয় অবধ ও মোগলাই মেলবন্ধন"
            "silk_road_chinatown" -> "চীনা-বাঙালি মিশ্র রন্ধনশৈলী ভ্রমণ"
            "street_food_safari" -> "রাস্তার সাধারণ খাবারের তীব্র ও ঝাল স্বাদ"
            "secrets_of_sweets" -> "প্রজন্মের ছানার মিষ্টি বানানোর বিশেষ কৌশল"
            "colonial_aristocracy" -> "ভিক্টোরিয়ান টি রুম ও অ্যাংলো-ইন্ডিয়ান ডাইনিং"
            else -> LanguageTranslation.translate(theme, isBengali)
        }
    } else {
        theme
    }
}

fun FoodTrail.localizedBackstory(isBengali: Boolean): String {
    return if (isBengali) {
        when (id) {
            "colonial_cabins" -> "এই পথরেখা আপনাকে এমন এক যুগে নিয়ে যাবে যেখানে বাংলার সুজন ও স্বাধীনতা সংগ্রামীরা ঔপনিবেশিক পুলিশ বা গোয়েন্দাদের নজর এড়াতে বন্ধ কেবিনের আড়ালে বসে গরম চা ও লালচে কাটলেটের স্বাদ উপভোগ করার সাথে সাথে ভারতের স্বাধীনতার নকশা আঁকতেন।"
            "nawabi_royal" -> "১৮৫৬ সালে যখন নবাব ওয়াজিদ আলী শাহ নির্বাসিত হয়ে মেটিয়াবুরুজে আসেন, লক্ষ্ণৌয়ের রাজকীয় খাবার সংস্কৃতি কলকাতার মাটির সাথে স্থায়ীভাবে মিশে যায়। এতে জন্ম নেয় সুগন্ধি আলু ও মৃদু সুগন্ধী ভাতে ভরা মেটিয়াবুরুজ ঘরানার এই বিরিয়ানি সংস্কৃতি।"
            "silk_road_chinatown" -> "গৃহযুদ্ধ এড়িয়ে আসা চীনা পরিবারগুলো ট্যাংরাতে প্রথম সুতা ও চামড়ার কারখানা গড়ে তোলে। তারা বাঙালি স্বাদের সাথে তাল মিলিয়ে আদা-রসুন, লাল মরিচ ও সসের মিশেলে এক অসামান্য স্বাদের জন্ম দেয়—যা আজকের বিখ্যাত চাইনিজ-কলকাতা ফিউশন খাবার।"
            "street_food_safari" -> "কলকাতার চমৎকার রন্ধনশৈলী শুধুমাত্র বড় এবং রাজকীয় ভবনে সীমাবদ্ধ ছিল না, বরং তা ছড়িয়ে ছিল এখানকার কর্মব্যস্ত রাস্তার ধারের কড়াই ও কুলেতে। পুচকা, কাঠি রোল ও ঝালমুড়ির মতো সাধারণ খাবার নিয়েই তৈরি হয়েছে এই জাদুকরী স্বাদরেখা।"
            "secrets_of_sweets" -> "১৯ শতকের আগে বাংলার মিষ্টি বেশিরভাগই চিনি, নারকেল ও চালের গুঁড়ি দিয়ে তৈরি হতো। পর্তুগিজদের কাছে ছানা কাটানোর কৌশল জানার পর বাংলার মিষ্টান্ন কারিগরগণ (ময়রারা) তা কাজে লাগিয়ে স্পঞ্জী রসগোল্লা ও মায়াময় সন্দেশ আবিষ্কার করে অনন্য নবজাগরণের সূচনা করেন।"
            "colonial_aristocracy" -> "সাহেবি আমলের পার্ক স্ট্রিটের চমৎকার হোটেল, মিষ্টির দোকান এবং ডাইনিং হলের সুবাস কলকাতার আভিজাত্যকে তুলে ধরে। ইউরোপীয় বেকিং কৌশলকে বাঙালি স্বাদের আদরে রূপ দিয়ে গড়ে উঠেছিল এই অনবদ্য কালচারাল আভিজাত্য।"
            else -> LanguageTranslation.translate(backstory, isBengali)
        }
    } else {
        backstory
    }
}
