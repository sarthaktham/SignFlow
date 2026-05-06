package com.example.signsync.ui.theme.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signsync.AuthViewModel
import com.example.signsync.DarkBlue
import com.example.signsync.GlassyButtonColor
import com.example.signsync.MediumPoppins
import com.example.signsync.R
import com.example.signsync.Routes
import com.example.signsync.WhiteText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // Injected shared ViewModel[cite: 6]
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }

    // Access the live display name from your ViewModel
    val userName by authViewModel.displayName

    LaunchedEffect(Unit) { isVisible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Image[cite: 7]
        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color.White,
                    drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                    modifier = Modifier.width(300.dp)
                ) {
                    // --- Drawer Header ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkBlue)
                            .padding(horizontal = 24.dp, vertical = 40.dp)
                    ) {
                        Column {
                            // Updated to show the dynamic name from UsernameScreen[cite: 7]
                            Text(
                                text = "Hi, $userName",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                color = Color.White
                            )
                            Text(
                                text = "SignFlow Premium",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Drawer Navigation Items ---[cite: 7]
                    NavigationDrawerItem(
                        label = { Text("Instructions", fontWeight = FontWeight.Medium) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Routes.INSTRUCTION)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Settings", fontWeight = FontWeight.Medium) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Routes.SETTING)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Help & Feedback", fontWeight = FontWeight.Medium) },
                        icon = { Icon(Icons.Default.HelpOutline, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Routes.FEEDBACK)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray)

                    NavigationDrawerItem(
                        label = { Text("About Us", fontWeight = FontWeight.Medium) },
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Routes.ABOUT_US)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            },
            content = {
                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("SignFlow", fontFamily = MediumPoppins, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = WhiteText) },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = WhiteText)
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)) {

                        // 2. Modern Bento Grid Layout[cite: 7]
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(animationSpec = tween(800)) + slideInVertically(initialOffsetY = { 40 })
                        ) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)) {
                                Text(
                                    "Your voice,\nin motion.",
                                    fontSize = 38.sp,
                                    lineHeight = 44.sp,
                                    color = WhiteText,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )

                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    BentoCard("Learn", "Start Basics", Modifier.weight(1f)) { /* TODO */ }
                                    BentoCard("Practice", "Daily Goal", Modifier.weight(1f)) { /* TODO */ }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                BentoCard(
                                    stringResource(R.string.sign_dictionary),
                                    stringResource(R.string.search_2000_gestures), Modifier
                                        .fillMaxWidth()
                                        .height(90.dp)) { /* TODO */ }

                                Spacer(modifier = Modifier.height(12.dp))

                                BentoCard(
                                    title = stringResource(R.string.our_innovation),
                                    subtitle = stringResource(R.string.how_it_works),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp),
                                    isHighlight = true
                                ) {
                                    navController.navigate(Routes.INNOVATION)
                                }
                            }
                        }

                        // 3. START INTERACTION (Bottom Fixed Action)[cite: 7]
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 24.dp, vertical = 40.dp)
                        ) {
                            PulseActionButton(text = "Start Interaction") {
                                navController.navigate(Routes.DETECTION)
                            }
                        }
                    }
                }
            }
        )
    }
}

// Helper Composable for Bento Cards[cite: 7]
@Composable
fun BentoCard(title: String, subtitle: String, modifier: Modifier, isHighlight: Boolean = false, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) GlassyButtonColor.copy(alpha = 0.95f) else GlassyButtonColor.copy(alpha = 0.75f)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = DarkBlue, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(subtitle, color = DarkBlue.copy(alpha = 0.6f), fontSize = 13.sp)
        }
    }
}

// Helper Composable for Pulsing Action Button[cite: 7]
@Composable
fun PulseActionButton(text: String, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.04f,
        animationSpec = infiniteRepeatable(animation = tween(1200), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
        elevation = ButtonDefaults.buttonElevation(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = WhiteText)
            Spacer(Modifier.width(10.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = WhiteText)
        }
    }
}