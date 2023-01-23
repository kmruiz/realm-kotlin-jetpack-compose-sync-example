package cat.kmruiz.realmpocandroid.infrastructure.todo

import android.util.Log
import cat.kmruiz.realmpocandroid.domain.todo.Notification
import cat.kmruiz.realmpocandroid.domain.todo.TodoListItem
import cat.kmruiz.realmpocandroid.infrastructure.realm.RealmInstance
import io.realm.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.platform.ensureNeverFrozen
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.syncSession
import kotlinx.coroutines.flow.collect

class TodoListItemRepository {
    private val realm by lazy { RealmInstance.getRealm() }

    suspend fun initialize() {
        val subs = realm.subscriptions
        subs.update {
            add(realm.query<TodoListItem>(), "todo-list")
            add(realm.query<Notification>(), "notifications")
        }

        subs.waitForSynchronization()
        subs.refresh()
    }

    fun all() =  realm.query<TodoListItem>()
        .find()
        .toList()

    fun allChanges() = realm.query<TodoListItem>().asFlow()
    fun allNotifications() = realm.query<Notification>().asFlow()

    suspend fun saveNew(item: TodoListItem) = realm.write {
        copyToRealm(item, UpdatePolicy.ALL)
    }

    suspend fun writingOn(item: TodoListItem, callback: TodoListItem.() -> Unit) {
        realm.write {
            val itemToChange = findLatest(item)!!
            callback(itemToChange)
            copyToRealm(itemToChange, UpdatePolicy.ALL)
        }
    }
}