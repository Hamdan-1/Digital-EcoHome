package com.trailblazers.digitalecohome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home // Use appropriate icons
import androidx.compose.material.icons.filled.Info // Placeholder, use actual icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings // Placeholder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
// import androidx.compose.ui.res.painterResource // Keep if you use drawable resources later
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination // <-- **FIX 1: Import findStartDestination**
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.trailblazers.digitalecohome.ui.gallery.GalleryScreen
import com.trailblazers.digitalecohome.ui.home.HomeScreen
import com.trailblazers.digitalecohome.ui.slideshow.SlideshowScreen
import com.trailblazers.digitalecohome.ui.theme.DigitalEcoHomeTheme // Import your theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Remove any specific unused import reported by the IDE (e.g., at line 20)

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class) // Needed for setContent if using M3 components directly? Usually not, but harmless.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DigitalEcoHomeTheme { // Apply your custom theme
                AppNavigation() // Call the main navigation composable
            }
        }
    }
}

// Data class to represent a navigation item in the drawer
data class NavDrawerItem(
    val route: String,
    val labelResId: Int, // String resource ID for the label
    val icon: ImageVector // Material Icon
    // Or use painterResource for drawables: val iconResId: Int
)

// Define the items for the navigation drawer
val drawerItems = listOf(
    NavDrawerItem(AppDestinations.HOME, R.string.menu_home, Icons.Default.Home),
    NavDrawerItem(AppDestinations.GALLERY, R.string.menu_gallery, Icons.Default.Info), // Replace Info icon
    NavDrawerItem(AppDestinations.SLIDESHOW, R.string.menu_slideshow, Icons.Default.Settings) // Replace Settings icon
    // Add more items here
)

// Opt-in needed here because ModalNavigationDrawer, Scaffold, TopAppBar etc. are experimental M3 APIs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()

    // Get current back stack entry to determine the selected route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Drawer Header (replaces nav_header_main.xml)
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally // Or Start
                ) {
                    // Example Header: Replace with your design
                    // Image(painter = painterResource(id = R.drawable.your_logo), contentDescription = "App Logo")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleMedium)
                    Text(text = "user@example.com", style = MaterialTheme.typography.bodySmall) // Example user info
                }
                // **FIX 3: Use HorizontalDivider instead of deprecated Divider**
                HorizontalDivider() // Separator
                Spacer(Modifier.height(12.dp))

                // Drawer Items (replaces menu/activity_main_drawer.xml)
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = stringResource(id = item.labelResId)) },
                        label = { Text(stringResource(id = item.labelResId)) },
                        selected = currentRoute == item.route, // Highlight the selected item
                        onClick = {
                            scope.launch { drawerState.close() } // Close drawer on item click
                            if (currentRoute != item.route) { // Avoid navigating to the same screen
                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        // **FIX 2: This syntax for saveState should be correct. Ensure imports are right.**
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item // <-- FIX 6: Typo fixed
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item // <-- FIX 6: Typo fixed
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // Dynamically set title based on current screen
                        val titleResId = drawerItems.find { it.route == currentRoute }?.labelResId ?: R.string.app_name
                        Text(stringResource(id = titleResId), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    },
                    navigationIcon = {
                        // Hamburger icon to open the drawer
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = stringResource(R.string.navigation_drawer_open) // Accessibility
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors( // Use mediumTopAppBarColors or largeTopAppBarColors if needed
                        containerColor = MaterialTheme.colorScheme.primary, // Example color
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { innerPadding -> // Content padding provided by Scaffold
            // Navigation Host - Define the screens
            NavHost(
                navController = navController,
                startDestination = AppDestinations.HOME, // Your starting screen route
                modifier = Modifier.padding(innerPadding) // Apply padding from Scaffold
            ) {
                composable(AppDestinations.HOME) {
                    HomeScreen(/* Pass necessary args/ViewModels */) // <-- FIX 6: Typo fixed
                }
                composable(AppDestinations.GALLERY) {
                    GalleryScreen(/* Pass necessary args/ViewModels */) // <-- FIX 6: Typo fixed
                }
                composable(AppDestinations.SLIDESHOW) {
                    SlideshowScreen(/* Pass necessary args/ViewModels */) // <-- FIX 6: Typo fixed
                }
                // Add more composable destinations here for other screens
            }
        }
    }
}


// Preview for the main App structure (Optional but helpful)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DigitalEcoHomeTheme {
        AppNavigation()
    }
}