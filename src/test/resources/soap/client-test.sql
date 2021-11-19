TRUNCATE TABLE clients CASCADE;

ALTER SEQUENCE clients_id_seq RESTART WITH 1; -- что бы после post следующий id был гарантированно 3

INSERT INTO clients (full_name, type)
VALUES ('Иванов И. И.', 0);
INSERT INTO clients (full_name, type)
VALUES ('ОАО ''Общество Гигантских растений''', 1);