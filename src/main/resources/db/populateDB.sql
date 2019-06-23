DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;


ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id) VALUES
('2019-01-08 10:05:06', 'Завтрак User', 500, 100000),
('2019-01-08 14:00:00', 'Обед User', 800, 100000),
('2019-01-08 20:05:06', 'Ужин User', 800, 100000),
('2019-01-08 09:05:06', 'Завтрак Admin', 1000, 100001),
('2019-01-08 13:05:06', 'Обед Admin', 1000, 100001),
('2019-01-08 19:05:06', 'ужин Admin', 1000, 100001);