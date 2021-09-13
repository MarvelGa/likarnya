use likarnya202117;
INSERT INTO categories  (title)  VALUES ( 'THERAPIST'),
                                        ('SURGEON'),
                                        ('TRAUMATOLOGIST'),
                                        ('DERMATOLOGIST'),
                                        ('NEUROPATHOLOGIST'),
                                        ('OTOLARYNGOLOGIST'),
                                        ('OPHTHALMOLOGIST');

INSERT INTO users  (email, first_name, last_name, `password`, `role`)
VALUES
( 'adminTest@gmail.com', 'Vasil', 'Vasilev', '8938a619894013bd7717ebd3d21cc0b8', 'ADMIN');

INSERT INTO users  (email, first_name, last_name, `password`, `role`, category_id)
VALUES
( 'dermatolog@gmail.com', 'Ivan', 'Ivanov', '8938a619894013bd7717ebd3d21cc0b8', 'DOCTOR', 4),
( 'terapist@gmail.com', 'Petro', 'Petrov', '8938a619894013bd7717ebd3d21cc0b8', 'DOCTOR', 1);