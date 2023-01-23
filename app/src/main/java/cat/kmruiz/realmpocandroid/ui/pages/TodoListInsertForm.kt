package cat.kmruiz.realmpocandroid.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import cat.kmruiz.realmpocandroid.domain.todo.TodoListItemFactory
import cat.kmruiz.realmpocandroid.infrastructure.realm.RealmInstance
import cat.kmruiz.realmpocandroid.infrastructure.todo.TodoListItemRepository
import cat.kmruiz.realmpocandroid.ui.composables.TodoListItemForm
import kotlinx.coroutines.launch

@Composable
fun TodoListInsertForm(navHostController: NavHostController, listItemRepository: TodoListItemRepository) {
    val coroutineScope = rememberCoroutineScope()

    TodoListItemForm(navHostController = navHostController, onSubmit = { category, description ->
        coroutineScope.launch {
            val user = RealmInstance.getAppServices().currentUser!!

            listItemRepository.saveNew(
                TodoListItemFactory.createNew(description, category, user.id)
            )
        }
    })
}