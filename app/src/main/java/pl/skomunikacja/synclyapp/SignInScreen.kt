package pl.skomunikacja.synclyapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash
import compose.icons.fontawesomeicons.solid.Lock
import compose.icons.fontawesomeicons.solid.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.SignInViewModel

@Composable
fun SignInScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: SignInViewModel = viewModel(),
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Syncly",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Teal100,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Sign in to your's account",
            fontSize = 16.sp,
            color = Gray300,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", color = Gray300) },
            leadingIcon = {
                Icon(
                    FontAwesomeIcons.Solid.User,
                    contentDescription = "Username",
                    tint = Gray300,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                cursorColor = Teal100,
                focusedLabelColor = Teal100
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Gray300) },
            leadingIcon = {
                Icon(
                    FontAwesomeIcons.Solid.Lock,
                    contentDescription = "Password",
                    tint = Gray300,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) FontAwesomeIcons.Solid.EyeSlash else FontAwesomeIcons.Solid.Eye,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Gray300,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Teal100,
                unfocusedBorderColor = Gray500,
                focusedTextColor = White100,
                unfocusedTextColor = White100,
                cursorColor = Teal100,
                focusedLabelColor = Teal100
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    handleSignIn(
                        username,
                        password,
                        context = context,
                        viewModel = viewModel,
                        coroutineScope = coroutineScope,
                        onStart = {isLoading = true},
                        onSuccess = {onLoginSuccess()},
                        onError = {isLoading = false}
                    );
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Teal100,
                contentColor = Black300
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && username.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Black300,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Gray300,
                fontSize = 14.sp
            )
            Text(
                text = "Sign Up",
                color = Teal100,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}

fun handleSignIn(
    username: String,
    password: String,
    context: Context,
    viewModel: SignInViewModel,
    coroutineScope: CoroutineScope,
    onStart: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {}
) {
    onStart()

    coroutineScope.launch {
        val response = viewModel.signIn(username, password)

        if (response != null) {
            Toast.makeText(context, "Hello, ${response.username}!", Toast.LENGTH_SHORT).show()
            onSuccess()
        } else {
            Toast.makeText(context, "Authentication failed. Please try again.", Toast.LENGTH_LONG).show()
            onError()
        }
    }
}