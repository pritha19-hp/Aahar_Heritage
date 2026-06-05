package com.example

import android.content.Context
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.ui.viewmodel.CulinaryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Aahar Concierge", appName)
  }

  @Test
  fun `test search filter in viewmodel`() = runBlocking {
    val application = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = CulinaryViewModel(application)
    
    // Set query to "Mughal"
    viewModel.setSearchQuery("Mughal")
    
    val dishes = viewModel.filteredDishes.first()
    
    // Check that results contain Mughal-themed items
    assertTrue(dishes.isNotEmpty())
    assertTrue(dishes.any { dish ->
        dish.name.contains("Mughal", ignoreCase = true) ||
        dish.era.contains("Mughal", ignoreCase = true) ||
        dish.originStory.contains("Mughal", ignoreCase = true)
    })
  }
}
