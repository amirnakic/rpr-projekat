BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `vacation` (
	`id`	INTEGER,
	`start_of_vacation`	DATE,
	`end_of_vacation`	DATE,
	`employee`	INTEGER,
	FOREIGN KEY(`employee`) REFERENCES `employee`(`id`),
	PRIMARY KEY(`id`)
);
CREATE TABLE IF NOT EXISTS `unpaid_leave` (
	`id`	INTEGER,
	`start_of_absence`	DATE,
	`end_of_absence`	DATE,
	`employee`	INTEGER,
	PRIMARY KEY(`id`),
	FOREIGN KEY(`employee`) REFERENCES `employee`(`id`)
);
CREATE TABLE IF NOT EXISTS `sick_leave` (
	`id`	INTEGER,
	`start_of_absence`	DATE,
	`end_of_absence`	DATE,
	`employee`	INTEGER,
	FOREIGN KEY(`employee`) REFERENCES `employee`(`id`),
	PRIMARY KEY(`id`)
);
CREATE TABLE IF NOT EXISTS `salary` (
	`id`	INTEGER,
	`base`	INTEGER,
	`coefficient`	INTEGER,
	`taxes`	INTEGER,
	`contributions`	INTEGER,
	`meal_allowances`	INTEGER,
	`date`	DATE,
	`employee`	INTEGER,
	FOREIGN KEY(`employee`) REFERENCES `employee`(`id`),
	PRIMARY KEY(`id`)
);
CREATE TABLE IF NOT EXISTS `employee` (
	`id`	INTEGER,
	`name`	TEXT,
	`surname`	TEXT,
	`phone_number`	TEXT,
	`email_address`	TEXT,
	`role`	TEXT,
	`qualifications`	TEXT,
	`work_experience`	TEXT,
	`vacation_days_per_year`	INTEGER,
	`date_of_birth`	DATE,
	`date_of_employment`	DATE,
	`vacation`	INTEGER,
	`sick_leave`	INTEGER,
	`unpaid_leave`	INTEGER,
	`department`	INTEGER,
	PRIMARY KEY(`id`),
	FOREIGN KEY(`department`) REFERENCES `department`(`id`)
);
CREATE TABLE IF NOT EXISTS `department` (
	`id`	INTEGER,
	`name`	TEXT,
	`current_number_of_employees`	INTEGER,
	`maximum_number_of_employees`	INTEGER,
	PRIMARY KEY(`id`)
);
COMMIT;
