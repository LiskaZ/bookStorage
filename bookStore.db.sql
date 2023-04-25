BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "books" (
	"id"	INTEGER NOT NULL UNIQUE,
	"author"	TEXT NOT NULL,
	"title"	TEXT NOT NULL,
	"description"	TEXT NOT NULL,
	"language"	INTEGER NOT NULL,
	"type"	INTEGER NOT NULL,
	"rating"	INTEGER NOT NULL,
	"key1"	TEXT,
	"key2"	TEXT,
	"key3"	TEXT,
	"key4"	TEXT,
	"key5"	TEXT,
	"key6"	TEXT,
	"key7"	TEXT,
	FOREIGN KEY("key1") REFERENCES "keywords"("id"),
	FOREIGN KEY("key2") REFERENCES "keywords"("id"),
	FOREIGN KEY("key5") REFERENCES "keywords"("id"),
	FOREIGN KEY("key3") REFERENCES "keywords"("id"),
	FOREIGN KEY("key4") REFERENCES "keywords"("id"),
	FOREIGN KEY("key6") REFERENCES "keywords"("id"),
	FOREIGN KEY("key7") REFERENCES "keywords"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
)
CREATE TABLE IF NOT EXISTS "keywords" (
	"id"	INTEGER NOT NULL UNIQUE,
	"keyword"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("id" AUTOINCREMENT)
)
COMMIT;
