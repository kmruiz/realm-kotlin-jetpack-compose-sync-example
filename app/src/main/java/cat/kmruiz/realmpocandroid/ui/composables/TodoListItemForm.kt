package cat.kmruiz.realmpocandroid.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cat.kmruiz.realmpocandroid.ui.theme.RealmPocAndroidTheme

@Composable
fun TodoListItemForm(navHostController: NavHostController, onSubmit: (category: String, description: String) -> Unit) {
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
                    Text(text = "Category")
                }

                Column {
                    OutlinedTextField(value = category, onValueChange = { category = it }, singleLine = true)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
                    Text(text = "Description")
                }

                Column {
                    OutlinedTextField(value = description, onValueChange = { description = it }, singleLine = true)
                }
            }
        }

        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        onSubmit(category, description)
                        category = ""
                        description = ""
                        navHostController.navigate("home")
                    }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TodoListItemFormPreview() {
    RealmPocAndroidTheme {
        TodoListItemForm(rememberNavController()) { category, description ->

        }
    }
}