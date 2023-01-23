package cat.kmruiz.realmpocandroid.infrastructure.realm

import cat.kmruiz.realmpocandroid.domain.todo.Notification
import cat.kmruiz.realmpocandroid.domain.todo.TodoListItem
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object RealmInstance {
    private val realmName: String = "<your-app-id>"

    private val backgroundAppServices by lazy {
        App.Companion.create(realmName)
    }

    fun getRealm(): Realm {
        val config = SyncConfiguration.Builder(
            getAppServices().currentUser!!, setOf(TodoListItem::class, Notification::class)
        ).name(realmName).build()

        return Realm.open(config)
    }
    fun getAppServices() = backgroundAppServices
}