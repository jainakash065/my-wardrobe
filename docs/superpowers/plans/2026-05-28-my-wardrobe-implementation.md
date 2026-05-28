# My Wardrobe Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the first usable local Android version of My Wardrobe: a private wardrobe catalog with a one-second launch animation, photo-first item capture, batch review, search, filters, and item details.

**Architecture:** Use a native single-module Android app with Kotlin and Jetpack Compose. Keep the app split into focused feature packages: `launch`, `wardrobe`, `capture`, `review`, `itemdetail`, plus shared `data`, `domain`, `navigation`, and `theme` packages. Persist wardrobe metadata in Room and store image files in app-private local storage.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Navigation Compose, Room, Kotlin coroutines and Flow, Android Photo Picker, CameraX or Activity Result camera capture, Coil for image loading, JUnit, kotlinx-coroutines-test, Room in-memory tests, Compose UI tests.

---

## Source Spec

Implement from: `docs/superpowers/specs/2026-05-28-my-wardrobe-product-design.md`

V1 priorities:

- Local-first Android app for Samsung Galaxy Z Flip 5.
- About one-second launch animation of a girl opening her wardrobe.
- Wardrobe home screen with search, category chips, filters, photo grid, and camera action.
- Single-item capture and batch capture.
- Gallery import as part of V1.
- Review Queue for untagged batch photos.
- Item Detail screen with required and optional fields.
- No cloud sync, accounts, social sharing, AI outfit generation, or worn-history analytics.

## File Structure

Create this structure under the Android module:

```text
app/src/main/java/com/jainakash/mywardrobe/
  AppContainer.kt
  MainActivity.kt
  MyWardrobeApp.kt
  navigation/
    AppRoute.kt
    MyWardrobeNavHost.kt
  theme/
    Color.kt
    Theme.kt
    Type.kt
  launch/
    LaunchScreen.kt
    LaunchViewModel.kt
  wardrobe/
    WardrobeScreen.kt
    WardrobeViewModel.kt
    WardrobeUiState.kt
    WardrobeFilters.kt
  capture/
    CaptureScreen.kt
    CaptureViewModel.kt
    ImageCaptureController.kt
  review/
    ReviewQueueScreen.kt
    ReviewQueueViewModel.kt
  itemdetail/
    ItemDetailScreen.kt
    ItemDetailViewModel.kt
    ItemFormState.kt
  data/
    AppDatabase.kt
    WardrobeItemDao.kt
    WardrobeItemEntity.kt
    WardrobeItemMapper.kt
    WardrobeRepository.kt
    LocalWardrobeRepository.kt
    ImageStorage.kt
    LocalImageStorage.kt
  domain/
    WardrobeItem.kt
    WardrobeItemDraft.kt
    WardrobeCategory.kt
    SearchWardrobeItems.kt
```

Create tests under:

```text
app/src/test/java/com/jainakash/mywardrobe/
  data/
  domain/
  wardrobe/
  review/
  itemdetail/

app/src/androidTest/java/com/jainakash/mywardrobe/
  navigation/
  wardrobe/
  capture/
```

## Task 1: Scaffold Native Android Project

**Files:**

- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/jainakash/mywardrobe/MainActivity.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/MyWardrobeApp.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/theme/Color.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/theme/Theme.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/theme/Type.kt`

- [ ] **Step 1: Create the Android project shell**

Use Android Studio's Empty Activity template or create the files manually with package `com.jainakash.mywardrobe`, minimum SDK 26, target SDK from the installed Android SDK, Kotlin, and Jetpack Compose enabled.

The package and app label must be:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="My Wardrobe"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyWardrobe">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

- [ ] **Step 2: Add required dependencies**

Configure `app/build.gradle.kts` with dependencies for Compose Material 3, Navigation Compose, Room runtime/compiler, Kotlin coroutines, Coil Compose, lifecycle ViewModel Compose, CameraX or AndroidX Activity Result APIs, JUnit, coroutines test, Room testing, and Compose UI tests.

The dependency categories must support these imports in later tasks:

```kotlin
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.room.Database
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
```

- [ ] **Step 3: Add the app entry point**

`MainActivity.kt` should call the app root:

```kotlin
package com.jainakash.mywardrobe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jainakash.mywardrobe.theme.MyWardrobeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWardrobeTheme {
                MyWardrobeApp()
            }
        }
    }
}
```

`MyWardrobeApp.kt` starts with a temporary text root until navigation is added in Task 5:

```kotlin
package com.jainakash.mywardrobe

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MyWardrobeApp() {
    Text("My Wardrobe")
}
```

- [ ] **Step 4: Verify the scaffold builds**

Run:

```bash
./gradlew assembleDebug
```

Expected: build succeeds and creates `app/build/outputs/apk/debug/app-debug.apk`.

- [ ] **Step 5: Commit**

```bash
git add settings.gradle.kts build.gradle.kts gradle.properties app
git commit -m "chore: scaffold android app"
```

## Task 2: Add Domain Model And Search Rules

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/domain/WardrobeCategory.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/domain/WardrobeItem.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/domain/WardrobeItemDraft.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/domain/SearchWardrobeItems.kt`
- Create: `app/src/test/java/com/jainakash/mywardrobe/domain/SearchWardrobeItemsTest.kt`

- [ ] **Step 1: Write search tests**

Create `SearchWardrobeItemsTest.kt`:

```kotlin
package com.jainakash.mywardrobe.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchWardrobeItemsTest {
    private val items = listOf(
        WardrobeItem(
            id = 1,
            photoPath = "/photos/blue-saree.jpg",
            name = "Blue silk saree",
            category = WardrobeCategory.SAREE,
            color = "Blue",
            occasion = "Wedding",
            fabric = "Silk",
            season = "Festive",
            notes = "Gold border"
        ),
        WardrobeItem(
            id = 2,
            photoPath = "/photos/black-kurti.jpg",
            name = "Black kurti",
            category = WardrobeCategory.KURTI,
            color = "Black",
            occasion = "Office",
            fabric = "Cotton",
            season = "Summer",
            notes = ""
        )
    )

    @Test
    fun `blank query returns all items`() {
        assertEquals(items, SearchWardrobeItems.apply(items, " "))
    }

    @Test
    fun `query matches name category color occasion fabric season and notes`() {
        assertEquals(listOf(items[0]), SearchWardrobeItems.apply(items, "gold"))
        assertEquals(listOf(items[0]), SearchWardrobeItems.apply(items, "saree"))
        assertEquals(listOf(items[1]), SearchWardrobeItems.apply(items, "office"))
        assertEquals(listOf(items[1]), SearchWardrobeItems.apply(items, "cotton"))
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```bash
./gradlew testDebugUnitTest --tests com.jainakash.mywardrobe.domain.SearchWardrobeItemsTest
```

Expected: fails because domain classes do not exist.

- [ ] **Step 3: Implement domain classes**

Create `WardrobeCategory.kt`:

```kotlin
package com.jainakash.mywardrobe.domain

enum class WardrobeCategory(val displayName: String) {
    SAREE("Saree"),
    KURTI("Kurti"),
    TOP("Top"),
    DRESS("Dress"),
    DUPATTA("Dupatta"),
    BOTTOM("Bottom"),
    OUTERWEAR("Jacket or outerwear"),
    ACCESSORY("Accessory"),
    OTHER("Other")
}
```

Create `WardrobeItem.kt`:

```kotlin
package com.jainakash.mywardrobe.domain

data class WardrobeItem(
    val id: Long,
    val photoPath: String,
    val name: String,
    val category: WardrobeCategory,
    val color: String,
    val occasion: String,
    val fabric: String,
    val season: String,
    val notes: String
)
```

Create `WardrobeItemDraft.kt`:

```kotlin
package com.jainakash.mywardrobe.domain

data class WardrobeItemDraft(
    val photoPath: String,
    val name: String = "",
    val category: WardrobeCategory = WardrobeCategory.OTHER,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = "",
    val notes: String = ""
) {
    val hasRequiredFields: Boolean
        get() = photoPath.isNotBlank() && name.isNotBlank() && color.isNotBlank()
}
```

Create `SearchWardrobeItems.kt`:

```kotlin
package com.jainakash.mywardrobe.domain

object SearchWardrobeItems {
    fun apply(items: List<WardrobeItem>, query: String): List<WardrobeItem> {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) return items

        return items.filter { item ->
            listOf(
                item.name,
                item.category.displayName,
                item.color,
                item.occasion,
                item.fabric,
                item.season,
                item.notes
            ).any { value -> value.lowercase().contains(normalizedQuery) }
        }
    }
}
```

- [ ] **Step 4: Verify test passes**

Run:

```bash
./gradlew testDebugUnitTest --tests com.jainakash.mywardrobe.domain.SearchWardrobeItemsTest
```

Expected: pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/domain app/src/test/java/com/jainakash/mywardrobe/domain
git commit -m "feat: add wardrobe domain model"
```

## Task 3: Add Room Persistence And Repository

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/data/WardrobeItemEntity.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/data/WardrobeItemDao.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/data/WardrobeItemMapper.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/data/AppDatabase.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/data/WardrobeRepository.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/data/LocalWardrobeRepository.kt`
- Create: `app/src/test/java/com/jainakash/mywardrobe/data/WardrobeItemMapperTest.kt`

- [ ] **Step 1: Write mapper test**

Create `WardrobeItemMapperTest.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import org.junit.Assert.assertEquals
import org.junit.Test

class WardrobeItemMapperTest {
    @Test
    fun `maps domain item to entity and back`() {
        val item = WardrobeItem(
            id = 42,
            photoPath = "/files/photo.jpg",
            name = "Gold dupatta",
            category = WardrobeCategory.DUPATTA,
            color = "Gold",
            occasion = "Wedding",
            fabric = "Silk",
            season = "Festive",
            notes = "Pair with blue saree"
        )

        val entity = item.toEntity()
        assertEquals(item, entity.toDomain())
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```bash
./gradlew testDebugUnitTest --tests com.jainakash.mywardrobe.data.WardrobeItemMapperTest
```

Expected: fails because data classes and mapper functions do not exist.

- [ ] **Step 3: Implement Room entity, mapper, DAO, database, and repository**

Create `WardrobeItemEntity.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wardrobe_items")
data class WardrobeItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photoPath: String,
    val name: String,
    val category: String,
    val color: String,
    val occasion: String,
    val fabric: String,
    val season: String,
    val notes: String
)
```

Create `WardrobeItemMapper.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft

fun WardrobeItem.toEntity(): WardrobeItemEntity = WardrobeItemEntity(
    id = id,
    photoPath = photoPath,
    name = name,
    category = category.name,
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)

fun WardrobeItemDraft.toEntity(): WardrobeItemEntity = WardrobeItemEntity(
    photoPath = photoPath,
    name = name,
    category = category.name,
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)

fun WardrobeItemEntity.toDomain(): WardrobeItem = WardrobeItem(
    id = id,
    photoPath = photoPath,
    name = name,
    category = WardrobeCategory.valueOf(category),
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)
```

Create `WardrobeItemDao.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WardrobeItemDao {
    @Query("SELECT * FROM wardrobe_items ORDER BY name COLLATE NOCASE ASC")
    fun observeItems(): Flow<List<WardrobeItemEntity>>

    @Query("SELECT * FROM wardrobe_items WHERE id = :id")
    fun observeItem(id: Long): Flow<WardrobeItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WardrobeItemEntity): Long

    @Update
    suspend fun update(item: WardrobeItemEntity)

    @Delete
    suspend fun delete(item: WardrobeItemEntity)
}
```

Create `AppDatabase.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WardrobeItemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wardrobeItemDao(): WardrobeItemDao
}
```

Create `WardrobeRepository.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.flow.Flow

interface WardrobeRepository {
    fun observeItems(): Flow<List<WardrobeItem>>
    fun observeItem(id: Long): Flow<WardrobeItem?>
    suspend fun saveDraft(draft: WardrobeItemDraft): Long
    suspend fun updateItem(item: WardrobeItem)
    suspend fun deleteItem(item: WardrobeItem)
}
```

Create `LocalWardrobeRepository.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalWardrobeRepository(
    private val dao: WardrobeItemDao
) : WardrobeRepository {
    override fun observeItems(): Flow<List<WardrobeItem>> =
        dao.observeItems().map { entities -> entities.map { it.toDomain() } }

    override fun observeItem(id: Long): Flow<WardrobeItem?> =
        dao.observeItem(id).map { it?.toDomain() }

    override suspend fun saveDraft(draft: WardrobeItemDraft): Long =
        dao.insert(draft.toEntity())

    override suspend fun updateItem(item: WardrobeItem) {
        dao.update(item.toEntity())
    }

    override suspend fun deleteItem(item: WardrobeItem) {
        dao.delete(item.toEntity())
    }
}
```

- [ ] **Step 4: Verify tests pass**

Run:

```bash
./gradlew testDebugUnitTest --tests com.jainakash.mywardrobe.data.WardrobeItemMapperTest
```

Expected: pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/data app/src/test/java/com/jainakash/mywardrobe/data
git commit -m "feat: add local wardrobe persistence"
```

## Task 4: Add Local Image Storage

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/data/ImageStorage.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/data/LocalImageStorage.kt`
- Create: `app/src/test/java/com/jainakash/mywardrobe/data/LocalImageStorageTest.kt`

- [ ] **Step 1: Write storage contract test**

Create a JVM-friendly test using a temporary folder:

```kotlin
package com.jainakash.mywardrobe.data

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class LocalImageStorageTest {
    @Test
    fun `stores bytes as private wardrobe image file`() {
        val root = createTempDir()
        val storage = LocalImageStorage(root)

        val path = storage.saveBytes("photo.jpg", byteArrayOf(1, 2, 3))

        val saved = File(path)
        assertTrue(saved.exists())
        assertTrue(saved.parentFile.name == "wardrobe-images")
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run:

```bash
./gradlew testDebugUnitTest --tests com.jainakash.mywardrobe.data.LocalImageStorageTest
```

Expected: fails because `LocalImageStorage` does not exist.

- [ ] **Step 3: Implement storage**

Create `ImageStorage.kt`:

```kotlin
package com.jainakash.mywardrobe.data

interface ImageStorage {
    fun saveBytes(displayName: String, bytes: ByteArray): String
}
```

Create `LocalImageStorage.kt`:

```kotlin
package com.jainakash.mywardrobe.data

import java.io.File
import java.util.UUID

class LocalImageStorage(
    private val filesDir: File
) : ImageStorage {
    override fun saveBytes(displayName: String, bytes: ByteArray): String {
        val directory = File(filesDir, "wardrobe-images")
        directory.mkdirs()

        val extension = displayName.substringAfterLast('.', "jpg")
        val file = File(directory, "${UUID.randomUUID()}.$extension")
        file.writeBytes(bytes)
        return file.absolutePath
    }
}
```

- [ ] **Step 4: Verify test passes**

Run:

```bash
./gradlew testDebugUnitTest --tests com.jainakash.mywardrobe.data.LocalImageStorageTest
```

Expected: pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/data/ImageStorage.kt app/src/main/java/com/jainakash/mywardrobe/data/LocalImageStorage.kt app/src/test/java/com/jainakash/mywardrobe/data/LocalImageStorageTest.kt
git commit -m "feat: add private image storage"
```

## Task 5: Add Navigation And Launch Animation

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/navigation/AppRoute.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/navigation/MyWardrobeNavHost.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/launch/LaunchScreen.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/launch/LaunchViewModel.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/MyWardrobeApp.kt`

- [ ] **Step 1: Add route definitions**

Create `AppRoute.kt`:

```kotlin
package com.jainakash.mywardrobe.navigation

sealed class AppRoute(val route: String) {
    data object Launch : AppRoute("launch")
    data object Wardrobe : AppRoute("wardrobe")
    data object Capture : AppRoute("capture")
    data object ReviewQueue : AppRoute("review")
    data object ItemDetail : AppRoute("item/{itemId}") {
        fun create(itemId: Long): String = "item/$itemId"
    }
}
```

- [ ] **Step 2: Add launch screen**

Create `LaunchScreen.kt`:

```kotlin
package com.jainakash.mywardrobe.launch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(onFinished: () -> Unit) {
    var open by remember { mutableStateOf(false) }
    val doorRotation by animateFloatAsState(
        targetValue = if (open) -18f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "wardrobeDoor"
    )

    LaunchedEffect(Unit) {
        open = true
        delay(1000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Opening your wardrobe",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.offset(y = 96.dp)
        )
        Text(
            text = "Girl + wardrobe animation asset",
            modifier = Modifier
                .size(width = 220.dp, height = 180.dp)
                .rotate(doorRotation)
        )
    }
}
```

This temporary animation establishes the one-second launch behavior. Task 10 replaces the temporary visual with the final lightweight asset while preserving the same timing and navigation contract.

- [ ] **Step 3: Add navigation host with temporary screens**

Create `MyWardrobeNavHost.kt`:

```kotlin
package com.jainakash.mywardrobe.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainakash.mywardrobe.launch.LaunchScreen

@Composable
fun MyWardrobeNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Launch.route
    ) {
        composable(AppRoute.Launch.route) {
            LaunchScreen(
                onFinished = {
                    navController.navigate(AppRoute.Wardrobe.route) {
                        popUpTo(AppRoute.Launch.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoute.Wardrobe.route) { Text("Wardrobe") }
        composable(AppRoute.Capture.route) { Text("Capture") }
        composable(AppRoute.ReviewQueue.route) { Text("Review Queue") }
        composable(AppRoute.ItemDetail.route) { Text("Item Detail") }
    }
}
```

Modify `MyWardrobeApp.kt`:

```kotlin
package com.jainakash.mywardrobe

import androidx.compose.runtime.Composable
import com.jainakash.mywardrobe.navigation.MyWardrobeNavHost

@Composable
fun MyWardrobeApp() {
    MyWardrobeNavHost()
}
```

- [ ] **Step 4: Verify build**

Run:

```bash
./gradlew assembleDebug
```

Expected: pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe
git commit -m "feat: add launch flow and navigation"
```

## Task 6: Build Wardrobe Home Screen

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/wardrobe/WardrobeUiState.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/wardrobe/WardrobeFilters.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/wardrobe/WardrobeViewModel.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/wardrobe/WardrobeScreen.kt`
- Create: `app/src/test/java/com/jainakash/mywardrobe/wardrobe/WardrobeViewModelTest.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/AppContainer.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/MyWardrobeApp.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/navigation/MyWardrobeNavHost.kt`

- [ ] **Step 1: Write ViewModel search/filter test**

Create `WardrobeViewModelTest.kt` with a fake repository:

```kotlin
package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WardrobeViewModelTest {
    @Test
    fun `query filters visible wardrobe items`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/blue.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "Wedding", "Silk", "Festive", ""),
                WardrobeItem(2, "/black.jpg", "Black kurti", WardrobeCategory.KURTI, "Black", "Office", "Cotton", "Summer", "")
            )
        )
        val viewModel = WardrobeViewModel(repository)

        viewModel.onQueryChanged("office")

        assertEquals(listOf("Black kurti"), viewModel.uiState.value.items.map { it.name })
    }

    private class FakeWardrobeRepository(items: List<WardrobeItem>) : WardrobeRepository {
        private val state = MutableStateFlow(items)
        override fun observeItems(): Flow<List<WardrobeItem>> = state
        override fun observeItem(id: Long): Flow<WardrobeItem?> = MutableStateFlow(state.value.firstOrNull { it.id == id })
        override suspend fun saveDraft(draft: WardrobeItemDraft): Long = 0
        override suspend fun updateItem(item: WardrobeItem) = Unit
        override suspend fun deleteItem(item: WardrobeItem) = Unit
    }
}
```

- [ ] **Step 2: Implement state and ViewModel**

Create `WardrobeUiState.kt`:

```kotlin
package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem

data class WardrobeUiState(
    val query: String = "",
    val selectedCategory: WardrobeCategory? = null,
    val items: List<WardrobeItem> = emptyList()
)
```

Create `WardrobeFilters.kt`:

```kotlin
package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory

data class WardrobeFilters(
    val category: WardrobeCategory? = null,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = ""
)
```

Create `WardrobeViewModel.kt`:

```kotlin
package com.jainakash.mywardrobe.wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.SearchWardrobeItems
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WardrobeViewModel(
    private val repository: WardrobeRepository
) : ViewModel() {
    private val allItems = mutableListOf<WardrobeItem>()
    private val _uiState = MutableStateFlow(WardrobeUiState())
    val uiState: StateFlow<WardrobeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeItems().collect { items ->
                allItems.clear()
                allItems.addAll(items)
                applyFilters()
            }
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        applyFilters()
    }

    fun onCategorySelected(category: WardrobeCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val searched = SearchWardrobeItems.apply(allItems, state.query)
        val filtered = state.selectedCategory?.let { category ->
            searched.filter { it.category == category }
        } ?: searched
        _uiState.value = state.copy(items = filtered)
    }
}
```

- [ ] **Step 3: Implement wardrobe UI**

Create `WardrobeScreen.kt`:

```kotlin
package com.jainakash.mywardrobe.wardrobe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardrobeScreen(
    state: WardrobeUiState,
    onQueryChanged: (String) -> Unit,
    onCategorySelected: (WardrobeCategory?) -> Unit,
    onAddClicked: () -> Unit,
    onItemClicked: (Long) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChanged,
                label = { Text("Search wardrobe") }
            )
            CategoryRow(state.selectedCategory, onCategorySelected)
            if (state.items.isEmpty()) {
                Text("No clothes found")
            } else {
                WardrobeGrid(state.items, onItemClicked)
            }
        }
    }
}

@Composable
private fun CategoryRow(
    selectedCategory: WardrobeCategory?,
    onCategorySelected: (WardrobeCategory?) -> Unit
) {
    Button(onClick = { onCategorySelected(null) }) {
        Text("All")
    }
    WardrobeCategory.entries.forEach { category ->
        AssistChip(
            onClick = { onCategorySelected(category) },
            label = { Text(category.displayName) }
        )
    }
}

@Composable
private fun WardrobeGrid(items: List<WardrobeItem>, onItemClicked: (Long) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 12.dp)
    ) {
        items(items) { item ->
            Card(onClick = { onItemClicked(item.id) }, modifier = Modifier.padding(6.dp)) {
                AsyncImage(model = item.photoPath, contentDescription = item.name)
                Text(item.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
```

- [ ] **Step 4: Add simple app container and wire screen into navigation**

Create `AppContainer.kt`:

```kotlin
package com.jainakash.mywardrobe

import android.content.Context
import androidx.room.Room
import com.jainakash.mywardrobe.data.AppDatabase
import com.jainakash.mywardrobe.data.LocalImageStorage
import com.jainakash.mywardrobe.data.LocalWardrobeRepository
import com.jainakash.mywardrobe.data.WardrobeRepository

class AppContainer(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "my-wardrobe.db"
    ).build()

    val wardrobeRepository: WardrobeRepository =
        LocalWardrobeRepository(database.wardrobeItemDao())

    val imageStorage: LocalImageStorage =
        LocalImageStorage(context.filesDir)
}
```

Modify `MyWardrobeApp.kt` to create the container once and pass it to navigation:

```kotlin
package com.jainakash.mywardrobe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jainakash.mywardrobe.navigation.MyWardrobeNavHost

@Composable
fun MyWardrobeApp() {
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }
    MyWardrobeNavHost(appContainer)
}
```

Modify `MyWardrobeNavHost` to accept `AppContainer`, create `WardrobeViewModel(appContainer.wardrobeRepository)`, and replace the temporary Wardrobe text with `WardrobeScreen`.

- [ ] **Step 5: Verify**

Run:

```bash
./gradlew testDebugUnitTest assembleDebug
```

Expected: tests pass and app builds.

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/wardrobe app/src/test/java/com/jainakash/mywardrobe/wardrobe app/src/main/java/com/jainakash/mywardrobe/navigation
git commit -m "feat: add wardrobe home screen"
```

## Task 7: Add Item Form And Detail Editing

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/itemdetail/ItemFormState.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/itemdetail/ItemDetailViewModel.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/itemdetail/ItemDetailScreen.kt`
- Create: `app/src/test/java/com/jainakash/mywardrobe/itemdetail/ItemFormStateTest.kt`

- [ ] **Step 1: Write validation test**

Create `ItemFormStateTest.kt`:

```kotlin
package com.jainakash.mywardrobe.itemdetail

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemFormStateTest {
    @Test
    fun `form is valid only when required fields are present`() {
        assertFalse(ItemFormState(photoPath = "/photo.jpg", name = "", color = "").isValid)
        assertFalse(ItemFormState(photoPath = "", name = "Blue saree", color = "Blue").isValid)
        assertTrue(ItemFormState(photoPath = "/photo.jpg", name = "Blue saree", color = "Blue").isValid)
    }
}
```

- [ ] **Step 2: Implement form state**

Create `ItemFormState.kt`:

```kotlin
package com.jainakash.mywardrobe.itemdetail

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItemDraft

data class ItemFormState(
    val photoPath: String = "",
    val name: String = "",
    val category: WardrobeCategory = WardrobeCategory.OTHER,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = "",
    val notes: String = ""
) {
    val isValid: Boolean
        get() = photoPath.isNotBlank() && name.isNotBlank() && color.isNotBlank()

    fun toDraft(): WardrobeItemDraft = WardrobeItemDraft(
        photoPath = photoPath,
        name = name,
        category = category,
        color = color,
        occasion = occasion,
        fabric = fabric,
        season = season,
        notes = notes
    )
}
```

- [ ] **Step 3: Implement item detail screen**

Create a Compose form with image preview, required fields at the top, optional fields below, Save button disabled until `isValid`, and Delete action for existing items.

Required UI labels:

```text
Name
Category
Color
Occasion
Fabric
Season
Notes
Save
Delete
```

- [ ] **Step 4: Verify**

Run:

```bash
./gradlew testDebugUnitTest assembleDebug
```

Expected: pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/itemdetail app/src/test/java/com/jainakash/mywardrobe/itemdetail
git commit -m "feat: add item detail form"
```

## Task 8: Add Capture And Gallery Import

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/capture/CaptureScreen.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/capture/CaptureViewModel.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/capture/ImageCaptureController.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/navigation/MyWardrobeNavHost.kt`

- [ ] **Step 1: Implement capture choices**

`CaptureScreen` must show these actions:

```text
Add one item
Batch capture
Import from gallery
```

Single item should route to the item form after an image is saved. Batch capture should save images as incomplete drafts and route to Review Queue.

- [ ] **Step 2: Implement gallery import**

Use Android Photo Picker for gallery import. For each selected URI:

1. Open an input stream with `ContentResolver`.
2. Read bytes.
3. Store bytes through `LocalImageStorage`.
4. Create a draft with `photoPath` and blank metadata.

- [ ] **Step 3: Implement camera capture**

Use Activity Result APIs or CameraX. Save the captured image through `LocalImageStorage` so the app always owns its private copy.

- [ ] **Step 4: Verify on device or emulator**

Run:

```bash
./gradlew assembleDebug
```

Then install on a device:

```bash
./gradlew installDebug
```

Expected: user can import one image from gallery and see it enter the item form or review flow.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/capture app/src/main/java/com/jainakash/mywardrobe/navigation
git commit -m "feat: add image capture and import"
```

## Task 9: Add Review Queue

**Files:**

- Create: `app/src/main/java/com/jainakash/mywardrobe/review/ReviewQueueViewModel.kt`
- Create: `app/src/main/java/com/jainakash/mywardrobe/review/ReviewQueueScreen.kt`
- Create: `app/src/test/java/com/jainakash/mywardrobe/review/ReviewQueueViewModelTest.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/navigation/MyWardrobeNavHost.kt`

- [ ] **Step 1: Add incomplete-item filtering**

Review Queue should show items where required metadata is missing:

```kotlin
fun needsReview(item: WardrobeItem): Boolean =
    item.name.isBlank() || item.color.isBlank()
```

- [ ] **Step 2: Write review filtering test**

Create `ReviewQueueViewModelTest.kt`:

```kotlin
package com.jainakash.mywardrobe.review

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewQueueViewModelTest {
    @Test
    fun `items missing required metadata need review`() {
        assertTrue(needsReview(WardrobeItem(1, "/a.jpg", "", WardrobeCategory.OTHER, "", "", "", "", "")))
        assertFalse(needsReview(WardrobeItem(2, "/b.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "", "", "", "")))
    }
}
```

- [ ] **Step 3: Implement Review Queue UI**

Each row/card should show:

```text
Photo preview
Missing: name/color
Complete details
Delete
```

The `Complete details` action routes to Item Detail.

- [ ] **Step 4: Verify**

Run:

```bash
./gradlew testDebugUnitTest assembleDebug
```

Expected: pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/jainakash/mywardrobe/review app/src/test/java/com/jainakash/mywardrobe/review app/src/main/java/com/jainakash/mywardrobe/navigation
git commit -m "feat: add review queue"
```

## Task 10: Polish V1 Flow And Verify On Samsung Flip 5

**Files:**

- Modify: `app/src/main/java/com/jainakash/mywardrobe/theme/Color.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/theme/Theme.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/launch/LaunchScreen.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/wardrobe/WardrobeScreen.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/capture/CaptureScreen.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/review/ReviewQueueScreen.kt`
- Modify: `app/src/main/java/com/jainakash/mywardrobe/itemdetail/ItemDetailScreen.kt`

- [ ] **Step 1: Apply visual direction**

Use a warm but restrained palette. Avoid making the app one-note pink or purple. The visual feel should be calm, private, and practical.

Suggested base roles:

```kotlin
val WardrobeRose = Color(0xFFB85C6E)
val WardrobeTeal = Color(0xFF2F7D7E)
val WardrobeInk = Color(0xFF242124)
val WardrobeMist = Color(0xFFF7F3F0)
```

- [ ] **Step 2: Replace temporary launch visual with final lightweight asset**

Use either:

- A Lottie JSON animation stored in `app/src/main/assets/wardrobe_opening.json`.
- A lightweight Compose/vector animation if no asset is ready.

The animation must remain about one second and must navigate to Wardrobe automatically.

- [ ] **Step 3: Verify core user journeys**

Manual checklist on emulator and Samsung Galaxy Z Flip 5:

```text
Launch animation appears for about one second.
Wardrobe opens after animation.
Empty wardrobe state is clear.
Add one item from gallery works.
Batch import creates review items.
Review Queue shows missing metadata.
Completing required fields removes item from Review Queue.
Search finds "blue saree", "black kurti", and occasion/fabric terms.
Item Detail edits and deletes an item.
App works after force close and reopen.
```

- [ ] **Step 4: Run final verification**

Run:

```bash
./gradlew testDebugUnitTest assembleDebug
```

Expected: tests pass and debug APK builds.

- [ ] **Step 5: Commit**

```bash
git add app
git commit -m "feat: polish v1 wardrobe flow"
```

## Final Push

After all tasks are complete:

```bash
git status --short
git push github master
```

Expected: working tree is clean and GitHub contains the completed V1 implementation.
