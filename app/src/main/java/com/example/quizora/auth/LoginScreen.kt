package com.example.quizora.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel){
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var error by remember {mutableStateOf<String?>(null)}


    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Text("Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(20.dp))

            EmailInputField(email = email, onEmailChange = {email = it})
            PasswordInputField(password = password, onPasswordChange = {password = it})

            Spacer(modifier = Modifier.height(20.dp))

            LoginButton(
                email = email,
                password = password,
                onError = { error = it },
                navController = navController,
                viewModel = viewModel
            )

            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text("Don't have an account? Register")
            }


            ErrorText(error = error)
        }
    }
}


@Composable
fun EmailInputField(email: String, onEmailChange: (String) -> Unit){
    val maxLength=30
    OutlinedTextField(
        value = email,
        onValueChange = {
            if (it.length <= maxLength){
                onEmailChange(it.trim())
            }
        },
        label = {Text ("Email")},
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}


@Composable
fun PasswordInputField(password: String, onPasswordChange: (String) -> Unit){
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {Text ("Password")},
        modifier = Modifier.fillMaxWidth().height(65.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}


@Composable
fun LoginButton(
    email: String,
    password: String,
    onError: (String) -> Unit,
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    Button(
        onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.login(email, password) { success, message ->
                    if (success) {
                        navController.navigate("student_home")
                    } else {
                        onError(message)
                    }
                }
            } else {
                onError("Please fill in all fields.")
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Login")
    }

}




@Composable
fun ErrorText(error: String?){
    error?.let {
        Spacer(modifier = Modifier.height(10.dp))
        Text(it, color = MaterialTheme.colorScheme.error)
    }
}
