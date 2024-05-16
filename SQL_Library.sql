create database library;

CREATE TABLE DeweyClassification (
    ClassificationNumber INT PRIMARY KEY,
    Description TEXT
);

CREATE TABLE Book (
    BookID INT PRIMARY KEY,
    Title VARCHAR(255),
    Author VARCHAR(255),
    PublicationYear INT,
    Copies INT,
	PricePerDay DECIMAL(10, 2),
    ClassificationNumber INT,
    Reservation_Count INT default 0,
    FOREIGN KEY (ClassificationNumber) REFERENCES DeweyClassification(ClassificationNumber)
);

CREATE TABLE Librarian (
    LibrarianID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    Email VARCHAR(255) UNIQUE,
    Phone VARCHAR(255),
    Address TEXT,
    EmploymentDate DATE,
    password VARCHAR(255)
);

CREATE TABLE Patron (
    PatronID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    Email VARCHAR(255),
    Phone VARCHAR(255),
    Address TEXT
);

CREATE TABLE BorrowingTransaction (
    BorrowingID INT PRIMARY KEY,
    BorrowDate DATE,
    DueDate DATE,
    ReturnDate DATE,
    PaymentStatus BOOLEAN,
    BookID INT,
    PatronID INT,
    LibrarianID INT,
    FOREIGN KEY (BookID) REFERENCES Book(BookID),
    FOREIGN KEY (PatronID) REFERENCES Patron(PatronID),
    FOREIGN KEY (LibrarianID) REFERENCES Librarian(LibrarianID)
);

INSERT INTO Book (BookID, Title, ClassificationNumber, Copies, Author, PublicationYear,PricePerDay) VALUES
(1, 'Lost Echoes', 100, 5, 'Ava Sinclair', 2021,1),
(2, 'Atlantis Rises', 200, 3, 'Ethan Blackwood', 2020,1),
(3, 'Starlight Tales', 210, 4, 'Maya Rivera', 2019,3),
(4, 'Time Garden', 300, 2, 'Leo Frost', 2021,1),
(5, 'Dreamscapes', 310, 6, 'Isla Grey', 2018,2),
(6, 'Esmeralda''s Shadow', 400, 3, 'Jackson Pierce', 2022,1),
(7, 'Baker''s Secret', 410, 4, 'Nora Quinn', 2020,4),
(8, 'Crimson Manor', 420, 5, 'Liam Sterling', 2019,4),
(9, 'Ambassador''s Intrigue', 430, 3, 'Elise Hawthorne', 2021,6),
(10, 'Obsidian Key', 440, 4, 'Marcus Vale', 2022,2);

INSERT INTO DeweyClassification (ClassificationNumber, Description) VALUES
(100, 'Fiction'),
(200, 'Science Fiction'),
(210, 'Fantasy'),
(300, 'Mystery'),
(310, 'Thriller'),
(400, 'Romance'),
(410, 'Historical Fiction'),
(420, 'Horror'),
(430, 'Political Thriller'),
(440, 'Adventure');

INSERT INTO Librarian ( Name, Email, Phone, Address, EmploymentDate,password) VALUES
('Alice Johnson', '1@example.com', '555-1234', '123 Main St, Cityville', '2020-05-15','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'),
( 'Bob Smith', '2@example.com', '555-5678', '456 Oak St, Townburg', '2019-08-20','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'),
('Charlie Brown', 'charlie@example.com', '555-9012', '789 Pine St, Villagetown', '2021-02-10','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'),
('Diana Miller', 'diana@example.com', '555-3456', '234 Elm St, Hamletville', '2020-11-30','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'),
('Evan Davis', 'evan@example.com', '555-7890', '567 Birch St, Countryside', '2018-07-05','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');

INSERT INTO Patron ( Name, Email, Phone, Address) VALUES
( 'John Doe', 'john@example.com', '555-1111', '789 Maple St, Suburbia'),
('Jane Smith', 'jane@example.com', '555-2222', '456 Pine St, Townsville'),
('Robert Johnson', 'robert@example.com', '555-3333', '123 Oak St, Villageton'),
('Emily Davis', 'emily@example.com', '555-4444', '789 Elm St, Countryside'),
('Michael Brown', 'michael@example.com', '555-5555', '567 Birch St, Hamletville');

INSERT INTO BorrowingTransaction (BorrowingID, BorrowDate, DueDate, ReturnDate, PaymentStatus,BookID, PatronID, LibrarianID) VALUES
(1, '2022-01-10', '2022-01-20', '2022-01-15', TRUE, 1, 1, 1),
(2, '2022-02-15', '2022-02-25', NULL, FALSE, 2, 2, 2),
(3, '2022-03-20', '2022-04-05', '2022-03-30', TRUE, 3,3, 3),
(4, '2022-04-25', '2022-05-05', NULL, FALSE,4,4, 4),
(5, '2022-05-30', '2022-06-15', '2022-06-10', TRUE, 5,5, 5);

select * from book;
select * from borrowingtransaction;
select * from deweyclassification;
select * from librarian;
select * from patron;




