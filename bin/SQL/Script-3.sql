CREATE TABLE IF NOT EXISTS employees(
	employee_id serial PRIMARY KEY,
	employee_name VARCHAR(30) NOT null
);

CREATE TABLE IF NOT EXISTS customers(
	customer_id serial PRIMARY KEY,
	customer_name VARCHAR(30) NOT NULL CHECK(NOT(customer_name LIKE '%:%'))
);
CREATE TABLE IF NOT EXISTS users(
--int, varchar, nvarchar, decimal, boolean, https://www.postgresql.org/docs/9.5/datatype.html
-- column name, column type, constraints 
	user_id serial PRIMARY KEY,
	username VARCHAR(30) unique not NULL CHECK(NOT(username LIKE '%:%')), 
	user_password VARCHAR(60) not NULL,
	employee_id INT, -- reference
	customer_id INT, --reference
	main_account_id INT --reference
);
CREATE TABLE IF NOT EXISTS account_types(
	bank_account_type_id serial PRIMARY KEY,
	account_type_name VARCHAR(30) UNIQUE NOT NULL
	--other qualities that account types should have
);
CREATE TABLE IF NOT EXISTS bank_accounts(
	bank_account_id serial PRIMARY KEY,
	owner_user_id INT NOT NULL, --reference
	account_name VARCHAR(30) CHECK(NOT(account_name LIKE '%:%')),
	account_type_id INT , --reference
	account_balance INT NOT NULL,
	is_activated boolean DEFAULT false
);
CREATE TABLE IF NOT EXISTS transactions(
	transaction_id serial PRIMARY KEY,
	account_credit_from_id INT, --reference
	account_debit_to_id INT, --reference
	transfer_ammount INT NOT NULL,
	date_time TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE users ADD CONSTRAINT lnk_users_employee_id
    FOREIGN KEY (employee_id) REFERENCES employees (employee_id);
 
ALTER TABLE users ADD CONSTRAINT lnk_users_customer_id
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id);
 
ALTER TABLE users ADD CONSTRAINT lnk_users_account_id
    FOREIGN KEY (main_account_id) REFERENCES bank_accounts (bank_account_id);
ALTER TABLE bank_accounts add COLUMN account_balance
	int NOT NULL CHECK (account_balance >= 0);
   
ALTER TABLE bank_accounts ADD CONSTRAINT lnk_bank_account_owner_id
    FOREIGN KEY (owner_user_id) REFERENCES users (user_id);

ALTER TABLE bank_accounts ADD CONSTRAINT lnk_bank_account_type_id
    FOREIGN KEY (account_type_id) REFERENCES account_types (bank_account_type_id);
   
ALTER TABLE transactions ADD CONSTRAINT lnk_transaction_credit_id
    FOREIGN KEY (account_credit_from_id) REFERENCES bank_accounts (bank_account_id);

ALTER TABLE transactions ADD CONSTRAINT lnk_transaction_debit_id
    FOREIGN KEY (account_debit_to_id) REFERENCES bank_accounts (bank_account_id);

   ALTER TABLE employees add COLUMN employee_name VARCHAR(30) NOT null;

INSERT INTO account_types(account_type_name) VALUES ('Savings'), ('Checkings');

INSERT INTO employees (employee_name) VALUES ('Krimz Yogurt');
INSERT INTO customers (customer_name) VALUES ('Krimz Yogurt');

INSERT INTO users (username, user_password, employee_id, customer_id) VALUES
	('KrimzYogurt', 'password', 1, 1);

INSERT INTO bank_accounts (owner_user_id, account_name, account_type_id, account_balance)
 VALUES (1, 'Main', 2, 1736);
INSERT INTO transactions  (account_credit_from_id, account_debit_to_id, transfer_ammount)
 VALUES (1, NULL, 0);

SELECT * FROM account_types WHERE (bank_account_type_id , account_type_name) = (1, 'Savings');

WITH tem AS (SELECT bank_account_id FROM bank_accounts WHERE owner_user_id = 1)
SELECT * FROM transactions WHERE (account_credit_from_id IN (SELECT * FROM tem)) OR (account_debit_to_id IN (SELECT * FROM tem)); 
;

ALTER TABLE customers ADD CONSTRAINT CHECK_customer_name
	CHECK(NOT(customer_name LIKE '%:%'));
ALTER TABLE transactions ADD CONSTRAINT CHECK_transfer_to_from
	CHECK(NOT(account_credit_from_id = account_debit_to_id));
CREATE OR REPLACE FUNCTION perform_transaction( account_credit_from int, account_debit_to int, ammount int) 
 RETURNS int
 AS $$
 --BEGIN; --no begin because TCL isn't allowed in functions/processes
	UPDATE bank_accounts SET account_balance = (account_balance - ammount) WHERE bank_account_id = account_credit_from;
	UPDATE bank_accounts SET account_balance = (account_balance + ammount) WHERE bank_account_id = account_debit_to;
	INSERT INTO transactions  (account_credit_from_id, account_debit_to_id, transfer_ammount)
			VALUES (account_credit_from, account_debit_to , ammount);

	SELECT transaction_id FROM transactions ORDER BY transaction_id DESC LIMIT 1;
		
--COMMIT;
$$
LANGUAGE SQL;


