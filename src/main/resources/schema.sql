CREATE TABLE CATEGORY (
                          id UUID PRIMARY KEY,
                          version INT NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          description VARCHAR(255) NOT NULL
);

CREATE TABLE EMPLOYEES (
                          id UUID PRIMARY KEY,
                          version INT NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE
);