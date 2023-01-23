package cat.kmruiz.realmpocandroid

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.kmruiz.realmpocandroid.infrastructure.realm.RealmInstance
import cat.kmruiz.realmpocandroid.infrastructure.todo.TodoListItemRepository
import cat.kmruiz.realmpocandroid.ui.composables.TodoListItemForm
import cat.kmruiz.realmpocandroid.ui.pages.TodoListInsertForm
import cat.kmruiz.realmpocandroid.ui.pages.TodoListPage
import cat.kmruiz.realmpocandroid.ui.theme.RealmPocAndroidTheme
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appServices = RealmInstance.getAppServices()
        val repository = TodoListItemRepository()

        setContent {
            var loaded by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = "loggingIn") {
                appServices.login(Credentials.emailPassword("example@acme.com", "123456"))
                repository.initialize()
                loaded = true
            }

            val navController = rememberNavController()

            RealmPocAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (loaded) {
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") { TodoListPage(
                                navHostController = navController,
                                listItemRepository = repository
                            ) }
                            composable("form") {
                                TodoListInsertForm(navHostController = navController, listItemRepository = repository)

                            }
                        }
                    }
                }
            }
        }
    }
}