TRUNCATE TABLE clients CASCADE;

ALTER SEQUENCE clients_id_seq RESTART WITH 1;
ALTER SEQUENCE contracts_id_seq RESTART WITH 1;
ALTER SEQUENCE accounts_id_seq RESTART WITH 1;
ALTER SEQUENCE phone_numbers_id_seq RESTART WITH 1;

INSERT INTO clients (full_name, type)
VALUES ('Иванов И. И.', 0);
INSERT INTO clients (full_name, type)
VALUES ('ОАО ''Общество Гигантских растений''', 1);

INSERT INTO contracts (number, client_id)
VALUES (123, 2);
INSERT INTO contracts (number, client_id)
VALUES (321, 1);
INSERT INTO contracts (number, client_id)
VALUES (666, 1);

INSERT INTO accounts (number, contract_id)
VALUES (42, 1);
INSERT INTO accounts (number, contract_id)
VALUES (33, 1);
INSERT INTO accounts (number, contract_id)
VALUES (21, 2);

INSERT INTO phone_numbers (number, account_id)
VALUES ('+7 (800) 555-35-35', 2);
INSERT INTO phone_numbers (number, account_id)
VALUES ('+7 (911) 111-22-33', 1);
INSERT INTO phone_numbers (number, account_id)
VALUES ('+7 (123) 456-78-90', 2);