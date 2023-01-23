package cat.kmruiz.realmpocandroid.domain.todo

enum class TodoListItemStatus {
    PENDING, DONE, DISCARDED;

    companion object {
        fun nextOf(status: TodoListItemStatus) = when (status) {
            PENDING -> DONE
            DONE -> DISCARDED
            DISCARDED -> PENDING
        }
    }
}