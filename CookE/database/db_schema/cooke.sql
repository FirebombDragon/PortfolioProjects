CREATE TABLE IF NOT EXISTS `user` (
    `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(20) NOT NULL,
    `salt` varchar(32) NOT NULL,
    `password` varchar(128),
    `first_name` varchar(20),
    `last_name` varchar(20),
    `email` varchar(30),
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `user`;
INSERT INTO `user` (`user_id`, `username`, `salt`, `password`, `first_name`, `last_name`, `email`) VALUES
    (1, 'student', 'vy6zt6ttguoxwt2rntr8tdn99wxrcv0h', 'd6ee915e0ef570ca68500d37adfb8f4309ea76b6da4ca529515c10f60c1c4a1ada5ef910ce93c27195e5cd905db9457782ca1ac10fd3bedf01ce0cbbb9125d82', 'Stu', 'Dent', 'student@email.com');

CREATE TABLE IF NOT EXISTS `recipe` (
    `recipe_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `recipe_name` varchar(50) NOT NULL,
    `owner_id` int(10) unsigned NULL DEFAULT NULL,
    PRIMARY KEY (`recipe_id`),
    FOREIGN KEY (`owner_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `recipe`;
INSERT INTO `recipe` (`recipe_id`, `recipe_name`, `owner_id`) VALUES
    (1, 'Simple Salad', 1),
    (2, 'Taco Meat', 1);

CREATE TABLE IF NOT EXISTS `instruction` (
    `instruct_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `instr_recipe_id` int(10) unsigned NOT NULL,
    `step_num` int(10) unsigned NOT NULL,
    `step` varchar(500) NOT NULL,
    PRIMARY KEY (`instruct_id`),
    FOREIGN KEY (`instr_recipe_id`) REFERENCES `recipe` (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `instruction`;
INSERT INTO `instruction` VALUES
    (1, 1, 1, 'Place mixed greens into a bowl'),
    (2, 1, 2, 'Pour olive oil and salt into bowl'),
    (3, 1, 3, 'Mix thoroughly'),
    (4, 2, 1, 'Brown ground beef in a pan for 6-7 minutes.  Break up throughout'),
    (5, 2, 2, 'Drain oil'),
    (6, 2, 3, 'Pour in water and seasoning.  Raise heat until boil'),
    (7, 2, 4, 'Lower heat, let simmer while stirring occasionally');

CREATE TABLE IF NOT EXISTS `ingredient` (
    `ing_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `amount` decimal(10,2) NOT NULL,
    `unit` varchar(20) NOT NULL,
    PRIMARY KEY (`ing_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `ingredient`;
INSERT INTO `ingredient` (`ing_id`, `name`, `amount`, `unit`) VALUES
    (1, 'Mixed Greens', 5, 'cups'),
    (2, 'Olive Oil', 0.25, 'cups'),
    (3, 'Salt', 1, 'tsp'),
    (4, 'Ground Beef', 1, 'lb'),
    (5, 'Water', 0.75, 'cups'),
    (6, 'Taco Seasoning', 0.25, 'cups');

CREATE TABLE IF NOT EXISTS `recipe_ingredient` (
    `ri_recipe_id` int(10) unsigned NOT NULL,
    `ri_ing_id` int(10) unsigned NOT NULL,
    PRIMARY KEY (`ri_recipe_id`,`ri_ing_id`),
    FOREIGN KEY (`ri_ing_id`) REFERENCES `ingredient` (`ing_id`),
    FOREIGN KEY (`ri_recipe_id`) REFERENCES `recipe` (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `recipe_ingredient`;
INSERT INTO `recipe_ingredient` (`ri_recipe_id`, `ri_ing_id`) VALUES
  (1, 1),
  (1, 2),
  (1, 3),
  (2, 4),
  (2, 5),
  (2, 6);

CREATE TABLE IF NOT EXISTS `user_shopping` (
    `shop_user_id` int(10) unsigned NOT NULL,
    `shop_ing_id` int(10) unsigned NOT NULL,
    PRIMARY KEY (`shop_user_id`,`shop_ing_id`),
    FOREIGN KEY (`shop_ing_id`) REFERENCES `ingredient` (`ing_id`),
    FOREIGN KEY (`shop_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `user_shopping`;
INSERT INTO `user_shopping` (`shop_user_id`, `shop_ing_id`) VALUES
    (1, 1),
    (1, 4),
    (1, 6);
