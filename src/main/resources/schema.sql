

CREATE TABLE IF NOT EXISTS clients
(
    id        BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(256) NOT NULL CHECK ( char_length(full_name) > 1),
    type      integer
);

CREATE TABLE IF NOT EXISTS contracts
(
    id        BIGSERIAL PRIMARY KEY,
    number    bigint,
    client_id bigint NOT NULL REFERENCES clients (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS accounts
(
    id          BIGSERIAL PRIMARY KEY,
    number      bigint,
    contract_id bigint NOT NULL REFERENCES contracts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS phone_numbers
(
    id         BIGSERIAL PRIMARY KEY,
    number     VARCHAR(20) NOT NULL,
    account_id bigint      NOT NULL REFERENCES accounts (id) ON DELETE CASCADE
);