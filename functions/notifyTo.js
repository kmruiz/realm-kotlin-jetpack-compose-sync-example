exports = function({ ownerId, description, source = 'SYSTEM' }){
  const notifications = context.services.get("mongodb-atlas").db("androidapp").collection("Notification")
  notifications.insertOne({ ownerId, description, source })
};