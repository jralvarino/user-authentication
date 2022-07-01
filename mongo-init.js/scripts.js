conn = new Mongo();
db = conn.getDB("authentication");

db.sequences.insert({_id: 'users_sequence', value: 1})
db.users.insert({ _id: 1, name: 'xpto', email: 'admin@xpto.com', password: '$2a$10$xEbwwPxpmOaI9sDbDrahXeqaNr6caV9CAXH8PBxnCbhmNxNWVj3Vm', address: 'Rua xyz',  profile: 'ADMIN'})
