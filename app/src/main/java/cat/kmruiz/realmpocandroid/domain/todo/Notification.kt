package cat.kmruiz.realmpocandroid.domain.todo

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.*

open class Notification(
    @PrimaryKey open var _id: ObjectId?,
    var description: String,
    var ownerId: String
) : RealmObject {
    constructor() : this(ObjectId(), "", "")
}