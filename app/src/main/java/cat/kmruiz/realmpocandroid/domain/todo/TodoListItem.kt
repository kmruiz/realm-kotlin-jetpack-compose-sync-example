package cat.kmruiz.realmpocandroid.domain.todo

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.*

open class TodoListItem(
    @PrimaryKey open var _id: ObjectId,
    var description: String,
    var category: String,
    var statusName: String,
    var ownerId: String,
    var whenCreated: Date,
    var whenChanged: Date
) : RealmObject {
    fun nextStatus() {
        this.status = TodoListItemStatus.nextOf(this.status)
    }

    constructor(): this(ObjectId(), "", "", "", "", Date(), Date()) {}

    var status: TodoListItemStatus
        get() = TodoListItemStatus.valueOf(statusName);
        set(value) { statusName = value.name }
}

object TodoListItemFactory {
    fun createNew(description: String, category: String, ownerId: String) = TodoListItem(
        ObjectId(),
        description,
        category,
        TodoListItemStatus.PENDING.name,
        ownerId,
        Date(),
        Date()
    )
}