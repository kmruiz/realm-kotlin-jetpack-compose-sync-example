package cat.kmruiz.realmpocandroid.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import cat.kmruiz.realmpocandroid.domain.todo.TodoListItem
import cat.kmruiz.realmpocandroid.domain.todo.TodoListItemStatus
import cat.kmruiz.realmpocandroid.ui.theme.RealmPocAndroidTheme
import org.mongodb.kbson.BsonObjectId
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListItemComponent(todoListItem: TodoListItem, onNextStatus: (item: TodoListItem) -> Unit) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                TodoListItemStatusBadge(status = todoListItem.status) {
                    onNextStatus(todoListItem)
                }
            }

            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(4.dp)) {
                TodoListItemDescription(text = todoListItem.description)
            }

            Spacer(Modifier.weight(1f))

            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                TodoListItemCategory(category = todoListItem.category);
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TodoListItemLogBar(todoListItem)
        }

    }
}

@Composable
private fun TodoListItemDescription(text: String) {
    Box(modifier = Modifier.padding(4.dp)) {
        Text(text = text)
    }
}

@Composable
private fun TodoListItemCategory(category: String) {
    Box(modifier = Modifier.border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = Color.DarkGray)) {
        Text(text = category, modifier = Modifier.padding(4.dp))
    }
}

@Composable
private fun TodoListItemStatusBadge(status: TodoListItemStatus, onClick: () -> Unit) {
    val initialColor = when (status) {
        TodoListItemStatus.DONE -> Color(30, 128, 30)
        TodoListItemStatus.PENDING -> Color.Gray
        TodoListItemStatus.DISCARDED -> Color(128, 30, 30)
    }

    Box(modifier = Modifier.border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = initialColor).clickable { onClick() }) {
        Text(text = status.name, modifier = Modifier.padding(4.dp), color = initialColor)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TodoListItemLogBar(todoListItem: TodoListItem) {
    val color = Color.Green
    val fmt = DateTimeFormatter.ofPattern("y-MM-d hh:m")

    val created = todoListItem.whenCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val changed = todoListItem.whenChanged.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

    Column {
        Text(text = "Created: ${fmt.format(created)}", color = color, fontSize = 2.em)
    }

    Column {
        Text(text = "Last Change: ${fmt.format(changed)}", color = color, fontSize = 2.em)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TodoListItemPreview() {
    RealmPocAndroidTheme {
        TodoListItemComponent(TodoListItem(org.mongodb.kbson.ObjectId(), "Lorem Ipsum", "Tasks", "PENDING", "", Date(), Date())) {}
    }
}