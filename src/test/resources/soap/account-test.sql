TRUNCATE TABLE clients CASCADE;

ALTER SEQUENCE clients_id_seq RESTART WITH 1;
ALTER SEQUENCE contracts_id_seq RESTART WITH 1;
ALTER SEQUENCE accounts_id_seq RESTART WITH 1;

INSERT INTO clients (full_name, type)
VALUES ('Иванов И. И.', 0);

INSERT INTO contracts (number, client_id)
VALUES (976, 1);
INSERT INTO contracts (number, client_id)
VALUES (880, 1);

INSERT INTO accounts (number, contract_id)
VALUES (123, 2);
INSERT INTO accounts (number, contract_id)
VALUES (321, 1);