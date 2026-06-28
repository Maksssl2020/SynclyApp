package pl.skomunikacja.synclyapp

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.UserProfileUpdateRequest
import pl.skomunikacja.synclyapp.ui.components.AvatarUploadDialog
import pl.skomunikacja.synclyapp.ui.components.ProfileAvatar
import pl.skomunikacja.synclyapp.ui.components.ProfileEditInput
import pl.skomunikacja.synclyapp.ui.components.ProfileStatCard
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateToSignInScreen: () -> Unit
) {
    val context = LocalContext.current
    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val avatarUploadState by viewModel.state.collectAsState()
    var showAvatarUploadDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authenticationData?.userId) {
        authenticationData?.userId?.let { userId ->
            viewModel.fetchUserProfileData(userId)
        }
    }

    var isEditMode by remember { mutableStateOf(false) }
    var displayName by remember { mutableStateOf(userProfile?.displayName ?: "") }
    var username by remember { mutableStateOf(userProfile?.username ?: "") }
    var email by remember { mutableStateOf(userProfile?.email ?: "") }
    var bio by remember { mutableStateOf(userProfile?.bio ?: "") }
    var website by remember { mutableStateOf(userProfile?.website ?: "") }
    var location by remember { mutableStateOf(userProfile?.location ?: "") }
    var avatar by remember { mutableStateOf(userProfile?.avatar) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userProfile) {
        userProfile?.let {
            displayName = it.displayName ?: ""
            username = it.username ?: ""
            email = it.email ?: ""
            bio = it.bio ?: ""
            website = it.website ?: ""
            location = it.location ?: ""
            avatar = it.avatar
        }
    }

    println(userProfile)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            ProfileAvatar(
                base64Image = avatar?.imageData,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                initials = ""
            )
            if (isEditMode) {
                IconButton(
                    onClick = {
                        showAvatarUploadDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Black300)
                        .border(2.dp, White100, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Change Avatar",
                        tint = White100,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (isEditMode) {
            Spacer(modifier = Modifier.height(24.dp))

            ProfileEditInput(
                label = "Display Name",
                value = displayName,
                onValueChange = { displayName = it }
            )

            ProfileEditInput(
                label = "Username",
                value = username,
                onValueChange = { username = it }
            )

            ProfileEditInput(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )

            ProfileEditInput(
                label = "Bio",
                value = bio,
                onValueChange = { bio = it },
                maxLines = 3
            )

            ProfileEditInput(
                label = "Website",
                value = website,
                onValueChange = { website = it },
                keyboardType = KeyboardType.Uri
            )

            ProfileEditInput(
                label = "Location",
                value = location,
                onValueChange = { location = it }
            )

        } else {
            Text(
                text = displayName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White100,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "@$username",
                fontSize = 16.sp,
                color = Gray300,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (bio.isNotEmpty()) {
                Text(
                    text = bio,
                    fontSize = 14.sp,
                    color = White100,
                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                )
            }

            if (location.isNotEmpty()) {
                Text(
                    text = "📍 $location",
                    fontSize = 14.sp,
                    color = Gray300,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (website.isNotEmpty()) {
                Text(
                    text = "🌐 $website",
                    fontSize = 14.sp,
                    color = Teal100,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        if (!isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatCard("Posts", (userProfile?.postsCount ?: 0).toString())
                ProfileStatCard("Friends", (userProfile?.friendsCount ?: 0).toString())
                ProfileStatCard("Followers", (userProfile?.followersCount ?: 0).toString())
            }
        }

        if (isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { isEditMode = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Gray300
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        if (authenticationData != null) {
                            coroutineScope.launch {
                                viewModel.updateUserProfile(
                                    userId = authenticationData!!.userId,
                                    data = UserProfileUpdateRequest(
                                        username = username,
                                        email = email,
                                        displayName = displayName,
                                        bio = bio,
                                        location = location,
                                        website = website
                                    )
                                )
                            }
                        }
                        isEditMode = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal100,
                        contentColor = Black300
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", fontWeight = FontWeight.Medium)
                }
            }
        } else {
            Button(
                onClick = { isEditMode = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal100,
                    contentColor = Black300
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.logout()
                    onNavigateToSignInScreen()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red100,
                    contentColor = White100
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    if (showAvatarUploadDialog) {
        AvatarUploadDialog(
            uploadState = avatarUploadState,
            handleFileChange = { uri: Uri? ->
               viewModel.handleFileChange(uri)
            },
            onDismiss = {
                showAvatarUploadDialog = false
                viewModel.clearSelectedAvatar()
            },
            onSaveAvatar = {
                showAvatarUploadDialog = false
                coroutineScope.launch {
                    if (authenticationData?.userId != null) {
                        viewModel.uploadAvatar(context = context, userId = authenticationData!!.userId)
                    }
                }
            }
        )
    }

}



