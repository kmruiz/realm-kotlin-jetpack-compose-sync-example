package cat.kmruiz.realmpocandroid.ui.pages

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cat.kmruiz.realmpocandroid.domain.todo.TodoListItem
import cat.kmruiz.realmpocandroid.infrastructure.realm.RealmInstance
import cat.kmruiz.realmpocandroid.infrastructure.todo.TodoListItemRepository
import cat.kmruiz.realmpocandroid.ui.composables.TodoListItemComponent
import io.realm.Realm
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoListPage(navHostController: NavHostController, listItemRepository: TodoListItemRepository) {
    val coroutineScope = rememberCoroutineScope()
    var itemList by remember { mutableStateOf(listOf<TodoListItem>()) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    fun updateList() {
        Log.i("TodoListPage", "Loading...")
        loading = true
        try {
            itemList = listItemRepository.all()
        } catch (e: Throwable) {
            Log.e("TodoListPage", "Could not load Todo List", e)
        }
        Log.i("TodoListPage", "Loaded Item List")
        loading = false
    }

    LaunchedEffect(key1 = "loading") {
        updateList()
    }

    LaunchedEffect(key1 = "listeningItems") {
        val flow = listItemRepository.allChanges().debounce(100.milliseconds)
        flow.collect {
            updateList()
        }
    }

    LaunchedEffect(key1 = "listeningNotifications") {
        val flowOfNotifications = listItemRepository.allNotifications()
        flowOfNotifications.collect {
            when (it) {
                is UpdatedResults -> {
                    it.insertions.forEach { idx ->
                        val notification = it.list.get(idx)
                        Toast.makeText(context, notification.description, Toast.LENGTH_LONG).show()
                    }
                } else -> {

                }
            }
        }
    }

    if (loading) {
        Text("Loading...")
    } else {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier
                .verticalScroll(ScrollState(0))
                .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    itemList.forEach {
                        Row(modifier = Modifier.padding(1.dp, 6.dp)) {
                            TodoListItemComponent(it, onNextStatus = {
                                coroutineScope.launch {
                                    listItemRepository.writingOn(it) {
                                        nextStatus()
                                    }

                                    updateList()
                                }
                            })
                        }
                    }
                }
            }

            Row {
                Button(onClick = { navHostController.navigate("form") }) {
                    Text("New Item")
                }
            }
        }
    }
}