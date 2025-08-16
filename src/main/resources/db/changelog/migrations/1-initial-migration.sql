-- liquibase formatted sql
-- changeset codetest:1-initial-migration.sql

-- Write your database setup SQL here
-- db/changelog/migrations/1-initial-migration.sql
CREATE TABLE insurance (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           personal_number VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE policy (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        insurance_id BIGINT NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        postal_code VARCHAR(10) NOT NULL,
                        start_date DATE NOT NULL,
                        end_date DATE,
                        premium DECIMAL(10, 2) NOT NULL,
                        FOREIGN KEY (insurance_id) REFERENCES insurance(id) ON DELETE CASCADE
);
