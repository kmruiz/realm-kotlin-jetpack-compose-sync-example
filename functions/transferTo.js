exports = function({ taskId, userId }){
  const todoList = context.services.get("mongodb-atlas").db("androidapp").collection("TodoListItem")
  return todoList.updateOne({ _id: new BSON.ObjectId(taskId) }, { $set: { ownerId: userId } })
};