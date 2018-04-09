SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE roles;
TRUNCATE TABLE users;
TRUNCATE TABLE user_roles;
TRUNCATE TABLE images;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO roles (role_id, permission, role_name) VALUES (1, 'nixmash:all', 'admin');
INSERT INTO roles (role_id, permission, role_name) VALUES (2, 'nixmash:view', 'user');

INSERT INTO users (user_id, username, email, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id, PASSWORD, version) VALUES (1, 'bob', 'bob@aol.com', 'Bob', 'Planter', 1, 0, 0, 0, 0, '0000000000000000', 'SITE', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 0);
INSERT INTO users (user_id, username, email, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id, PASSWORD, version) VALUES (2, 'ken', 'ken@aol.com', 'Ken', 'Stark', 1, 0, 0, 0, 0, '0000000000000000', 'SITE', 'a4e63bcacf6c172ad84f9f4523c8f1acaf33676fa76d3258c67b7e7bbf16d777', 0);

INSERT INTO user_roles (id, user_id, role_id) VALUES (1, 1, 1);
INSERT INTO user_roles (id, user_id, role_id) VALUES (2, 1, 2);
INSERT INTO user_roles (id, user_id, role_id) VALUES (3, 2, 2);

