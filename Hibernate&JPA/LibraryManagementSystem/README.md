# Library Management System

A simple **Java JPA/Hibernate** project that demonstrates entity relationships, inheritance, JPQL queries, JOIN FETCH, aggregation, and Criteria API.

## Technologies

* Java 17
* Jakarta Persistence (JPA)
* Hibernate ORM
* H2 Database
* Maven

## Project Structure

```text
src/main/java
├── model
│   ├── Author.java
│   ├── Book.java
│   ├── Category.java
│   ├── Publisher.java
│   ├── Person.java
│   ├── Employee.java
│   └── Customer.java
│
└── org/example
    └── Main.java

src/main/resources
└── META-INF
    └── persistence.xml
```

## Entity Relationships

### Author - Book

A bidirectional **One-to-Many / Many-to-One** relationship is used.

* One Author can have many Books.
* Each Book belongs to one Author.
* `CascadeType.ALL` and `orphanRemoval` are used on the Author side.

```java
@OneToMany(mappedBy = "author",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
private List<Book> books;
```

### Book - Publisher

A **Many-to-One** relationship is used.

* Many Books can belong to one Publisher.
* The relationship uses `LAZY` fetching.

### Book - Category

A **Many-to-Many** relationship is used between Books and Categories.

A join table called `book_category` is used to connect both entities.

## Inheritance

The project contains a `Person` inheritance hierarchy:

```text
Person
├── Employee
└── Customer
```

The `JOINED` inheritance strategy is used:

```java
@Inheritance(strategy = InheritanceType.JOINED)
```

This creates a separate table for the parent entity and tables for the child entities.

## JPQL Queries

The project demonstrates several JPQL queries:

### 1. Find books by author

Finds all books written by a specific author.

```java
SELECT b FROM Book b
WHERE b.author.name = :authorName
```

### 2. Find books by publisher

```java
SELECT b FROM Book b
WHERE b.publisher.name = :publisherName
```

### 3. Find a book by ID

A positional parameter is used:

```java
SELECT b FROM Book b
WHERE b.id = ?1
```

### 4. JOIN FETCH

Fetches an Author together with their Books in one query:

```java
SELECT DISTINCT a
FROM Author a
JOIN FETCH a.books
WHERE a.name = :name
```

`JOIN FETCH` is useful when the related collection needs to be available immediately, especially before the persistence context is closed.

### 5. Aggregate Query

Counts the number of books for each author:

```java
SELECT a.name, COUNT(b)
FROM Author a
LEFT JOIN a.books b
GROUP BY a.name
``
```
