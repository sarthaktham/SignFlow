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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.signsync.DarkBlue
import com.example.signsync.GlassyButtonColor
import com.example.signsync.MediumPoppins
import com.example.signsync.R
import com.example.signsync.Routes
import com.example.signsync.WhiteText
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { isVisible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Image
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
                            Text(
                                text = "Hi, User",
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

                    // --- Drawer Navigation Items ---

                    // 1. Instructions
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

                    // 2. Settings
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

                    // 3. Help & Feedback
                    NavigationDrawerItem(
                        label = { Text("Help & Feedback", fontWeight = FontWeight.Medium) },
                        icon = { Icon(Icons.Default.HelpOutline, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            // TODO: navController.navigate(Routes.HELP)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray)

                    // 4. About Us (Existing)
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
                            title = { Text("SignFlow", fontFamily = MediumPoppins,fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = WhiteText) },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = WhiteText)
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

                        // 2. Modern Bento Grid Layout
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(animationSpec = tween(800)) + slideInVertically(initialOffsetY = { 40 })
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                                Text(
                                    "Your voice,\nin motion.",
                                    fontSize = 38.sp,
                                    lineHeight = 44.sp,
                                    color = WhiteText,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )

                                // Grid Row 1: Learn & Practice
                                Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    BentoCard("Learn", "Start Basics", Modifier.weight(1f)) { /* TODO: Add Route */ }
                                    BentoCard("Practice", "Daily Goal", Modifier.weight(1f)) { /* TODO: Add Route */ }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Grid Row 2: Dictionary
                                BentoCard("Sign Dictionary", "Search 2000+ gestures", Modifier.fillMaxWidth().height(90.dp)) { /* TODO: Add Route */ }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Grid Row 3: INNOVATION (Restored)
                                BentoCard(
                                    title = "Our Innovation",
                                    subtitle = "How it works",
                                    modifier = Modifier.fillMaxWidth().height(90.dp),
                                    isHighlight = true // Subtle color difference
                                ) {
                                    navController.navigate(Routes.INNOVATION)
                                }
                            }
                        }

                        // 3. START INTERACTION (Bottom Fixed Action)
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

@Composable
fun BentoCard(title: String, subtitle: String, modifier: Modifier, isHighlight: Boolean = false, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) GlassyButtonColor.copy(alpha = 0.95f) else GlassyButtonColor.copy(alpha = 0.75f)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = DarkBlue, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(subtitle, color = DarkBlue.copy(alpha = 0.6f), fontSize = 13.sp)
        }
    }
}

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
        modifier = Modifier.fillMaxWidth().height(70.dp).scale(scale),
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