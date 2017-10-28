DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS contact;

CREATE TABLE project (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  hyperlink TEXT,
  hyperlink_description TEXT,
  image_filepath TEXT
);

CREATE TABLE contact (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  subject TEXT NOT NULL,
  contact_details TEXT,
  message_body TEXT NOT NULL
);

-- FOOD PROJECTS
INSERT INTO project(title, description, hyperlink, hyperlink_description, image_filepath) VALUES
  ("Beef Bibimbap", "A Korean staple which is a lot of fun to mix up & oh so delicious! I cooked the recipe from Nadia Lim's cookbook 'Easy Weeknight Meals' (+ an egg) but below is a link to her recipe in from her 'My Food Bag' collection.", "https://www.myfoodbag.co.nz/recipes/details/3629-Korean-Beef-Bibimbap-Bowl-with-Toasted-Sesame-Seeds", "Find the recipe here", "Multimedia/Food/3.jpg"),
  ("Fruit salad jelly in watermelon slices", "A fun, pretty and different way of making sure you get your 5+ a day! Empty a watermelon, full it with fruit and set it with gelatin!", "https://tipbuzz.com/fruity-watermelon-jello/", "Find the recipe and a video here", "Multimedia/Food/4.jpg"),
  ("Peanut and Sesame brittle", "Tasty sweet treat from the Dumpling Sisters which is a fancy addition to ice cream and can keep well - although it may not keep long with how much you want to eat! ", "http://dumplingsisters.com/recipes/peanut-and-sesame-brittle","Find the recipe here", "Multimedia/Food/10.jpg");

-- CARD PROJECTS
INSERT INTO project (title, description, hyperlink, hyperlink_description, image_filepath) VALUES
  ("Pop-up Box card", "I made this pop up box card and decorated it to look like a favourite family photo. My mum loved it and decided to also make it!", "https://m.youtube.com/watch?v=GiiGXr0UyEQ", "Watch the video tutorial here", "Multimedia/Cards/4.jpg"),
  ("Money candles", "For a creative way of gifting money, I created this card which includes money that has been rolled up to look like birthday candles! ", "", "", "Multimedia/Cards/5.jpg"),
  ("Impressive pop-up peony card", "If you want to make a 'WOW' card, this is it! Peter Dahmen has created and kindly shared his design with us all! On the front of the card for my mum, I had written 'Thanks Mum for helping us grow and...'", "https://peterdahmen.de/en/2013/07/23/peony/", "Find Peter Dahmen's tutorial and template here", "Multimedia/Cards/12.jpg"),
  ("Rush Hour puzzle card", "Challenging puzzle card my brother and I had fun making. Based off the Rush Hour puzzle game, the final red car locked the card shut and the card could only be opened once the puzzle was solved and red car came out!", "", "", "Multimedia/Cards/14.jpg");

-- BAKING
INSERT INTO project(title, description, hyperlink, hyperlink_description, image_filepath) VALUES
  ("Pandan Chiffon Cupcakes", "I'm an novice with both pandan and chiffon but I gave it a go! Looked great straight out of the oven then shrunk! Topped it with coconut frosting with a piece of peach, lychee and desiccated coconut.", "http://iamthedreambaker.blogspot.co.nz/2014/04/pandan-chiffon-cupcakes.html?m=1", "Find the cupcake recipe here", "Multimedia/Baking/1.jpg"),
  ("Reindeer cookies", "Fun for Christmas holidays! Cute peanut butter flavoured reindeer cookie", "http://buddingbaketress.blogspot.co.nz/2010/12/peanut-butter-reindeer-cookies.html?m=1", "Find the recipe here", "Multimedia/Baking/2.jpg"),
  ("Cookie Monster cupcakes", "One of the many cute Cookie Monster cupcakes that are online. I did these ones by dying desiccated coconut with blue food colouring, toasting it a little to dry it out, icing the cupcake with blue icing then rolling the top of the cupcake in the coconut. White chocolate button with black icing for eyes and half a homemade cookie filling his mouth.", "", "", "Multimedia/Baking/3.jpg");

-- CRAFTS
INSERT INTO project(title, description, hyperlink, hyperlink_description, image_filepath) VALUES
  ("Stewart Island cross-stitch bookmark", "During an amazing trip to Stewart Island at the bottom of New Zealand I found this Stewart Island cross-stitch bookmark kit which I completed to help us always remember the fun times on our holiday there", "https://nzfabs.com/products/cross-stitch-bookmark-stewart-island", "You can also get this bookmark here", "Multimedia/Crafts/2.jpg");

INSERT INTO project(title, description, hyperlink, hyperlink_description, image_filepath) VALUES ("Spongebob Squarepants Cake", "I made this cake for a Spongebob fan. The jellyfish is made from REAL jelly and the rest of the decoration has been done using butter-icing", "", "", "Multimedia/Baking/22.jpg");

INSERT INTO contact(subject, contact_details, message_body) VALUES ("Welcome to My Weekend Project", "google@gmail.com", "Welcome to the World Wide Web");