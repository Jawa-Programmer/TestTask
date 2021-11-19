DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS contracts CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS phone_numbers;

CREATE TABLE clients
(
    id        BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(256) NOT NULL CHECK ( char_length(full_name) > 1),
    type      integer
);

CREATE TABLE contracts
(
    id        BIGSERIAL PRIMARY KEY,
    number    bigint,
    client_id bigint NOT NULL REFERENCES clients (id) ON DELETE CASCADE
);

CREATE TABLE accounts
(
    id          BIGSERIAL PRIMARY KEY,
    number      bigint,
    contract_id bigint NOT NULL REFERENCES contracts (id) ON DELETE CASCADE
);

CREATE TABLE phone_numbers
(
    id         BIGSERIAL PRIMARY KEY,
    number     VARCHAR(20) NOT NULL,
    account_id bigint      NOT NULL REFERENCES accounts (id) ON DELETE CASCADE
);
