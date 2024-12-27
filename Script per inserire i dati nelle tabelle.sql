-- Inserting sample data into the customer table
INSERT INTO customers (name, surname, date_of_birth, gender, active_subscription) 
VALUES 
    ('John', 'Doe', '1990-05-15', 'Male', 'yearly'),
    ('Jane', 'Smith', '1985-12-10', 'Female', 'monthly'),
    ('Michael', 'Johnson', '1978-08-25', 'Male', 'daily'),
    ('Anna', 'Ferrari', '1988-07-25', 'Female', 'daily'),
    ('Paolo', 'Gallo', '1992-04-12', 'Male', 'yearly'),
    ('Elena', 'Moretti', '1980-11-30', 'Female', 'monthly');

-- Inserting sample data into the equipment table
INSERT INTO equipments (name, description, availability, purchase_date) 
VALUES 
    ('Treadmill', 'Cardio equipment for running or walking', 5, '2023-02-20'),
    ('Dumbbells', 'Weight training equipment for strength exercises', 10, '2023-01-10'),
    ('Yoga Mat', 'Mat for yoga and stretching exercises', 20, '2023-03-05');

-- Inserting sample data into the trainer table
INSERT INTO trainers (name, surname, specialization, work_hours) 
VALUES 
    ('Alice', 'Anderson', 'Personal Trainer', 40),
    ('Bob', 'Brown', 'Yoga Instructor', 30),
    ('Charlie', 'Chaplin', 'Strength Coach', 45);

-- Inserting sample data into the training_program table
INSERT INTO training_programs (training_type, duration, intensity, customer_id, trainer_id) 
VALUES 
    ('Weight Training', 60, 'Hard', 1, 3),
    ('Cardio', 45, 'Medium', 2, 1),
    ('Yoga', 90, 'Easy', 3, 2),
    ('Pilates', 60, 'Medium', 4, 1),  -- Anna Ferraro con Laura Neri
    ('Allenamento cardio', 45, 'Easy', 5, 2),  -- Paolo Gallo con Antonio Russo
    ('Yoga', 60, 'Hard', 6, 1);  -- Elena Moretti con Sara Esposito;
