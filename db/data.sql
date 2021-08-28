use likarnya20211;
INSERT INTO categories  (title)  VALUES ( 'THERAPIST'),
                                        ('SURGEON'),
                                        ('TRAUMATOLOGIST'),
                                        ('DERMATOLOGIST'),
                                        ('NEUROPATHOLOGIST'),
                                        ('OTOLARYNGOLOGIST'),
                                        ('OPHTHALMOLOGIST');

INSERT INTO users  (email, first_name, last_name, `password`, `role`)
VALUES
( 'adminTest@gmail.com', 'Admin', 'Admin', '8938a619894013bd7717ebd3d21cc0b8', 'ADMIN');