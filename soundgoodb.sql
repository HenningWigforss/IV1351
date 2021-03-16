CREATE TABLE "administrator"
(
	"administrator_id" serial PRIMARY KEY,
	"admin_id_tag" varchar(500) UNIQUE NOT NULL
);


CREATE TABLE "person"
(
	"person_id" serial PRIMARY KEY,
	"person_number" varchar(12) UNIQUE NOT NULL,
	"first_name" varchar(500),
	"last_name" varchar(500),
	"age" int,
	"street" varchar(500),
	"city" varchar(500),
	"zip" varchar(5)
);


CREATE TABLE "instrument_play"
(
	"instrument_play_id" serial PRIMARY KEY,
	"instrument_name" varchar(100)
	
);

CREATE TABLE "lesson_level"
(
	"lesson_level_id" serial PRIMARY KEY,
	"level_name" varchar(50)
	
);

CREATE TABLE "genre"
(
	"genre_id" serial PRIMARY KEY,
	"genre_name" varchar(100)
);

CREATE TABLE "salary_per_lesson"
(
	"salary_per_lesson_id" serial PRIMARY KEY,
	"salary_per_lesson_id_tag" varchar(500) UNIQUE NOT NULL,
	"salary_name" varchar(500),
	"salary_amount" int
);

CREATE TABLE "price_scheme"
(
	"price_scheme_id" serial PRIMARY KEY,
	"price_id_tag" varchar(100) NOT NULL UNIQUE,
	"price_type_name" varchar(100),
	"price" int
);

CREATE TABLE "phone"
(
	"person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE,
	"phone" varchar(100) NOT NULL,
	PRIMARY KEY ("person_id", "phone")

);

CREATE TABLE "email"
(
	"person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE,
	"email" varchar(100) NOT NULL,
	PRIMARY KEY ("person_id", "email")

);

CREATE TABLE "brand"
(
	"brand_id" serial PRIMARY KEY,
	"brand_name" varchar(500)
);



CREATE TABLE "enrollment_application"
(
	"person_id" int NOT NULL REFERENCES "person",
	"application_date" date NOT NULL,
	PRIMARY KEY("person_id", "application_date")
);


CREATE TABLE "skill_level"
(
	"skill_level_id" serial PRIMARY KEY,
	"instrument" varchar(500),
	"level" varchar(500)
);


CREATE TABLE "instrument_stock"
(
	"instrument_stock_id" serial PRIMARY KEY,
	"instrument_play_id" int NOT NULL REFERENCES "instrument_play",
	"brand_id" int NOT NULL REFERENCES "brand",
	"instrument_id_tag" varchar(500) NOT NULL,
	"leased" bit(1),
	"fee" int
	
);

CREATE TABLE "student"
(
	"student_id" serial PRIMARY KEY,
	"person_id" int NOT NULL REFERENCES "person",
	"student_id_tag" varchar(500) NOT NULL,
	"family_tag" varchar(500),
	"number_of_leases" int
);

CREATE TABLE "student_skill_level"
(
	"student_id" int NOT NULL REFERENCES "student",
	"skill_level_id" int NOT NULL REFERENCES "skill_level",
	PRIMARY KEY("student_id", "skill_level_id")
);


CREATE TABLE "leasing"
(
	"leasing_id" serial PRIMARY KEY,
	"leasing_id_tag" varchar(500) NOT NULL,
	"student_id" int NOT NULL REFERENCES "student",
	"instrument_stock_id" int NOT NULL REFERENCES "instrument_stock",
	"lease_date" date,
	"return_date" date,
	"lease_status" varchar(100)
);


CREATE TABLE "instructor"
(
	"instructor_id" serial PRIMARY KEY,
	"instructor_id_tag" varchar(500) NOT NULL,
	"person_id" int NOT NULL REFERENCES "person"
);

CREATE TABLE "instrument_teach"
(
	"instrument_teach_id" serial PRIMARY KEY,
	"type" varchar(500)
);

CREATE TABLE "instructor_instrument_teach"
(
	"instrument_teach_id" int NOT NULL REFERENCES "instrument_teach",
	"instructor_id" int NOT NULL REFERENCES "instructor",
	PRIMARY KEY ("instrument_teach_id", "instructor_id")
);

CREATE TABLE "invoice"
(
	"invoice_id" serial PRIMARY KEY,
	"student_id" int NOT NULL REFERENCES "student",
	"amount" int,
	"month" varchar(20),
	"discount" decimal
);

CREATE TABLE "leasing_invoice"
(
	"leasing_id" int NOT NULL REFERENCES "leasing",
	"invoice_id" int NOT NULL REFERENCES "invoice",
	PRIMARY KEY("leasing_id", "invoice_id")
);

CREATE TABLE "salary"
(
	"salary_id" serial PRIMARY KEY,
	"month" varchar(20),
	"instructor_id" int NOT NULL REFERENCES "instructor"
);

CREATE TABLE "lesson_salary"
(
	"lesson_salary_id" serial PRIMARY KEY,
	"salary_id" int NOT NULL REFERENCES "salary",
	"salary_per_lesson_id" int NOT NULL REFERENCES "salary_per_lesson",
	"skill_level" varchar(100),
	"day" date,
	"type" varchar(100),
	"amount" int
);

CREATE TABLE "parent_contact"
(
	"parent_contact_id" serial PRIMARY KEY,
	"parent_id_tag" varchar(500) NOT NULL,
	"first_name" varchar(500) NOT NULL,
	"last_name" varchar(500)
);

CREATE TABLE "parent_email"
(
	"parent_contact_id" int NOT NULL REFERENCES "parent_contact",
	"parent_email" varchar(500) NOT NULL,
	PRIMARY KEY("parent_contact_id", "parent_email")
);

CREATE TABLE "parent_phone"
(
	"parent_contact_id" int NOT NULL REFERENCES "parent_contact",
	"parent_phone" varchar(500) NOT NULL,
	PRIMARY KEY("parent_contact_id", "parent_phone")
);

CREATE TABLE "person_parent_contact"
(
	"person_id" int NOT NULL REFERENCES "person",
	"parent_contact_id" int NOT NULL REFERENCES "parent_contact",
	PRIMARY KEY("person_id", "parent_contact_id")
);

CREATE TABLE "time_slot"
(
	"time_slot_id" serial PRIMARY KEY,
	"time_slot_id_tag" varchar(500) NOT NULL,
	"instructor_id" int NOT NULL REFERENCES "instructor",
	"lesson_salary_id" int REFERENCES "lesson_salary",
	"lesson_level_id" int REFERENCES "lesson_level",
	"instrument_play_id" int REFERENCES "instrument_play",
	"genre_id" int REFERENCES "genre",
	"time_stamp" timestamp(10) NOT NULL,
	"min_students" int,
	"max_students" int,
	"booked_students" int
);


CREATE TABLE "booking"
(
	"booking_id" serial PRIMARY KEY,
	"booking_id_tag" varchar(500) NOT NULL,
	"student_id" int NOT NULL REFERENCES "student",
	"administrator_id" int NOT NULL REFERENCES "administrator",
	"time_slot_id" int REFERENCES "time_slot"
);

CREATE TABLE "audition_for_advanced"
(
	"booking_id" int PRIMARY KEY REFERENCES "booking"  ON DELETE CASCADE,
	"time_stamp" timestamp(10) NOT NULL,
	"instuctor_id" int NOT NULL REFERENCES "instructor",
	"lesson_salary_id" int REFERENCES "lesson_salary",
	"instrument_play_id" int NOT NULL REFERENCES "instrument_play"
);



CREATE TABLE "lesson"
(
	"booking_id" int PRIMARY KEY REFERENCES "booking" ON DELETE CASCADE,
	"time_stamp" timestamp(10) NOT NULL,
	"instructor_id" int NOT NULL REFERENCES "instructor",
	"lesson_salary_id" int REFERENCES "lesson_salary",
	"instrument_play_id" int REFERENCES "instrument_play",
	"lesson_level_id" int REFERENCES "lesson_level"	
);

CREATE TABLE "lesson_fee"
(
	"booking_id" int PRIMARY KEY REFERENCES "booking" ON DELETE CASCADE,
	"amount" int,
	"month" varchar(20),
	"price_scheme_id" int NOT NULL REFERENCES "price_scheme",
	"invoice_id" int NOT NULL REFERENCES "invoice"
	
);





