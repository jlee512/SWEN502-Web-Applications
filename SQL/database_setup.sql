DROP TABLE IF EXISTS project;

CREATE TABLE project (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  descripton TEXT NOT NULL,
  hyperlink TEXT,
  image_filepath TEXT
);