db = db.getSiblingDB('urlshortener')

db.createUser({
    user: 'user',
    pwd: 'pass',
    roles: [
      {
        role: 'dbOwner',
        db: 'urlshortener',
    },
  ],
});
