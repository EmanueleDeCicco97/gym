DROP TABLE if exists public.customers;

CREATE TABLE public.customers (
	id bigserial NOT NULL,
	active_subscription varchar(255) NULL,
	date_of_birth date NOT NULL,
	gender varchar(255) NULL,
	"name" varchar(255) NOT NULL,
	surname varchar(255) NOT NULL,
	CONSTRAINT customers_pkey PRIMARY KEY (id)
);


DROP table if exists public.equipments;

CREATE TABLE public.equipments (
	id bigserial NOT NULL,
	availability int4 NOT NULL,
	description varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	purchase_date date NULL,
	CONSTRAINT equipments_pkey PRIMARY KEY (id)
);



DROP TABLE if exists public.trainers;

CREATE TABLE public.trainers (
	id bigserial NOT NULL,
	"name" varchar(255) NOT NULL,
	specialization varchar(255) NOT NULL,
	surname varchar(255) NOT NULL,
	work_hours int4 NULL,
	CONSTRAINT trainers_pkey PRIMARY KEY (id)
);


DROP table if exists public.training_programs;

CREATE TABLE public.training_programs (
	id bigserial NOT NULL,
	duration int4 NULL,
	intensity varchar(255) NULL,
	training_type varchar(255) NULL,
	customer_id int8 NULL,
	trainer_id int8 NULL,
	CONSTRAINT training_programs_pkey PRIMARY KEY (id),
	CONSTRAINT fkfwij35dyb4fsh9l8uxs9xw1w9 FOREIGN KEY (trainer_id) REFERENCES public.trainers(id),
	CONSTRAINT fkojpgsadkp3rso1snadsa2v6d0 FOREIGN KEY (customer_id) REFERENCES public.customers(id)
);