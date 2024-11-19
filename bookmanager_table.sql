CREATE DATABASE book_manager;

USE book_manager;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    price DOUBLE NOT NULL
);

USE book_manager;

INSERT INTO books (name, author, publisher, year, price) VALUES 
('Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', 'Scholastic', 1997, 19.99),
('The Hobbit', 'J.R.R. Tolkien', 'Houghton Mifflin', 1937, 15.95),
('1984', 'George Orwell', 'Harcourt Brace Jovanovich', 1949, 12.99),
('To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', 1960, 10.99),
('Pride and Prejudice', 'Jane Austen', 'T. Egerton', 1813, 9.99);


SELECT * FROM books;