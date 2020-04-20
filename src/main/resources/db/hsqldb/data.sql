-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');

INSERT INTO users(username,password,enabled) VALUES ('amine','12345',TRUE);
INSERT INTO authorities VALUES ('amine','owner');

INSERT INTO users(username,password,enabled) VALUES ('pablo','12345',TRUE);
INSERT INTO authorities VALUES ('pablo','owner');

INSERT INTO users(username,password,enabled) VALUES ('owner','owner',TRUE);
INSERT INTO authorities VALUES ('owner','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO users(username,password,enabled) VALUES ('vet2','12345',TRUE);
INSERT INTO authorities VALUES ('vet2','veterinarian');


INSERT INTO vets VALUES (1, 'James', 'Carter', 'vet1');
INSERT INTO vets VALUES (2, 'Helen', 'Leary', 'vet1');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas', 'vet1');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega', 'vet1');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens', 'vet1');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins', 'vet1');
INSERT INTO vets VALUES (7, 'Vet2', 'vet2', 'vet2');


INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);
INSERT INTO vet_specialties VALUES (7, 2);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO status VALUES (1, 'PENDING');
INSERT INTO status VALUES (2, 'ACCEPTED');
INSERT INTO status VALUES (3, 'REJECTED');

INSERT INTO room VALUES(1,'Room1',2,4);
INSERT INTO room VALUES(2,'Room2',1,2);
INSERT INTO room VALUES(3,'Room3',3,1);
INSERT INTO room VALUES(4,'Room4',4,5);


INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');
INSERT INTO owners VALUES (11, 'Amine', 'Chaghir', 'C/San Francisco n3', 'Zalamea de la Serena', '6085555487', 'amine');
INSERT INTO owners VALUES (12, 'Prueba', 'Prueba', '110 W. Liberty St.', 'Madison', '666666666', 'owner');
INSERT INTO owners VALUES (13, 'Pablo', 'Reneses', '110 W. Liberty St.', 'Sevilla', '666665432', 'pablo');


INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (18, 'Lena', '2017-05-11', 2, 13);

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (14, 'Pet Cat', '2012-06-08', 1, 11);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (15, 'Pet Snake', '2018-07-08', 4, 11);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (16, 'Pet Dog', '2017-06-18', 2, 12);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (17, 'Pet Bird', '2012-06-28', 5, 12);


INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (5, 14, '2020-01-04', 'spayed');


INSERT INTO cause(id, title, description, deadline, money, status_id, username) VALUES (1, 'First Cause', 'This is my first cause', '2020-12-30', 10000.00, 1, 'owner1');
INSERT INTO cause(id, title, description, deadline, money, status_id, username) VALUES (2, 'Second Cause', 'This is my second cause', '2020-06-20', 10000.00, 3, 'owner1');
INSERT INTO cause(id, title, description, deadline, money, status_id, username) VALUES (3, 'Third Cause', 'This is my third cause', '2020-10-30', 10000.00, 2, 'owner1');
INSERT INTO cause(id, title, description, deadline, money, status_id, username) VALUES (4, 'Prueba Cause', 'This is my third cause', '2020-10-30', 10000.00, 2, 'admin1');

INSERT INTO donation VALUES (1, 'true',5000.0,NULL,3,'admin1');
INSERT INTO donation VALUES (2, 'false',2000.0,NULL,3,'admin1');
INSERT INTO donation VALUES (3, 'true',1000.0,NULL,3,'admin1');

INSERT INTO reservation VALUES (1,'2020-05-12','2020-06-20','15',11,1,1);
INSERT INTO reservation VALUES (2,'2020-05-20','2020-06-22','16',12,2,1);
INSERT INTO reservation VALUES (3,'2020-06-20','2020-06-22','16',12,2,1);
INSERT INTO reservation VALUES (5,'2020-05-02','2020-06-22','17',12,4,3);

INSERT INTO diagnosis(id, description, date, vet_id, pet_id, visit_id) VALUES (1, 'Todo bien', '2013-01-10', 7, 14, 5);


