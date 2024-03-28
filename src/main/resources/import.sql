INSERT INTO departments (name) VALUES ('Management');
INSERT INTO departments (name) VALUES ('Tech');

INSERT INTO positions (position, salary) VALUES ('Software developer', 5600.00);
INSERT INTO positions (position, salary) VALUES ('Data analyst', 5600.00);
INSERT INTO positions (position, salary) VALUES ('CEO', 7500.00);
INSERT INTO positions (position, salary) VALUES ('Human Resources', 4500.00);

INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Cauê Garcia', 'caue@gmail.com', '2024-cre', '2004-11-08', 1); 
INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Mical Almeida', 'micael@gmail.com', '2024-cre', '2005-08-12', 1); 
INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Ana Cláudia', 'ana@gmail.com', '2024-cre', '2004-11-08', 2); 
INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Enzo Rios', 'enzo@gmail.com', '2024-cre', '2003-06-17', 2); 
INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Peter Parker', 'peter@gmail.com', '2024-cre', '2000-11-10', 4); 
INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Max Wilson', 'max@gmail.com', '2024-cre', '1997-02-01', 4); 
INSERT INTO employees (name, email, credentials, birth_date, position_id) VALUES ('Ricardison Rios', 'ricardison@gmail.com', '2024-cre', '1999-12-12', 3);

INSERT INTO employee_department (department_id, employee_id) VALUES (2, 1);
INSERT INTO employee_department (department_id, employee_id) VALUES (2, 2);
INSERT INTO employee_department (department_id, employee_id) VALUES (2, 3);
INSERT INTO employee_department (department_id, employee_id) VALUES (2, 4);
INSERT INTO employee_department (department_id, employee_id) VALUES (1, 5);
INSERT INTO employee_department (department_id, employee_id) VALUES (1, 6);
INSERT INTO employee_department (department_id, employee_id) VALUES (1, 7);