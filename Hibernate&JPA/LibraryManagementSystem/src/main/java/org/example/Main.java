package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.Author;
import model.Book;
import model.Publisher;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("libraryManagement");

        EntityManager em = emf.createEntityManager();

        // =========================
        // Insert sample data
        // =========================

        em.getTransaction().begin();

        Publisher publisher1 = new Publisher();
        publisher1.setName("Penguin");

        Publisher publisher2 = new Publisher();
        publisher2.setName("Bloomsbury");

        em.persist(publisher1);
        em.persist(publisher2);


        Author author1 = new Author();
        author1.setName("Naguib Mahfouz");

        Author author2 = new Author();
        author2.setName("J.K. Rowling");


        Book book1 = new Book();
        book1.setTitle("Palace Walk");
        book1.setAuthor(author1);
        book1.setPublisher(publisher1);

        Book book2 = new Book();
        book2.setTitle("Miramar");
        book2.setAuthor(author1);
        book2.setPublisher(publisher1);

        Book book3 = new Book();
        book3.setTitle("Harry Potter");
        book3.setAuthor(author2);
        book3.setPublisher(publisher2);


        author1.addBook(book1);
        author1.addBook(book2);

        author2.addBook(book3);


        em.persist(author1);
        em.persist(author2);

        em.getTransaction().commit();


        // =========================
        // 1. Books by author
        // =========================

        String jpql =
                "SELECT b FROM Book b " +
                        "WHERE b.author.name = :authorName";

        List<Book> books =
                em.createQuery(jpql, Book.class)
                        .setParameter("authorName", "Naguib Mahfouz")
                        .getResultList();

        System.out.println("\nBooks by Naguib Mahfouz:");

        for (Book b : books) {
            System.out.println(b.getTitle());
        }


        // =========================
        // 2. Books by publisher
        // =========================

        String publisherQuery =
                "SELECT b FROM Book b " +
                        "WHERE b.publisher.name = :publisherName";

        List<Book> publisherBooks =
                em.createQuery(
                                publisherQuery,
                                Book.class
                        )
                        .setParameter("publisherName", "Penguin")
                        .getResultList();

        System.out.println("\nBooks published by Penguin:");

        for (Book b : publisherBooks) {
            System.out.println(b.getTitle());
        }


        // =========================
        // 3. Book by ID
        // =========================

        String idQuery =
                "SELECT b FROM Book b " +
                        "WHERE b.id = ?1";

        Book selectedBook =
                em.createQuery(
                                idQuery,
                                Book.class
                        )
                        .setParameter(1, book1.getId())
                        .getSingleResult();

        System.out.println("\nBook with ID " +
                selectedBook.getId() + ":");

        System.out.println(selectedBook.getTitle());


        // =========================
        // 4. JOIN FETCH
        // =========================

        String fetchQuery =
                "SELECT DISTINCT a " +
                        "FROM Author a " +
                        "JOIN FETCH a.books " +
                        "WHERE a.name = :name";

        Author author =
                em.createQuery(
                                fetchQuery,
                                Author.class
                        )
                        .setParameter("name", "Naguib Mahfouz")
                        .getSingleResult();

        System.out.println("\nAuthor and books:");

        System.out.println(author.getName());

        for (Book b : author.getBooks()) {
            System.out.println("- " + b.getTitle());
        }


        // =========================
        // 5. COUNT + GROUP BY
        // =========================

        String countQuery =
                "SELECT a.name, COUNT(b) " +
                        "FROM Author a " +
                        "LEFT JOIN a.books b " +
                        "GROUP BY a.name";

        List<Object[]> counts =
                em.createQuery(
                                countQuery,
                                Object[].class
                        )
                        .getResultList();

        System.out.println("\nBooks per author:");

        for (Object[] row : counts) {

            System.out.println(
                    row[0] + " -> " + row[1]
            );
        }


        // =========================
        // 6. Criteria API
        // Find books by title
        // =========================

        CriteriaBuilder cb =
                em.getCriteriaBuilder();

        CriteriaQuery<Book> criteriaQuery =
                cb.createQuery(Book.class);

        Root<Book> bookRoot =
                criteriaQuery.from(Book.class);

        criteriaQuery.select(bookRoot)
                .where(
                        cb.like(
                                bookRoot.get("title"),
                                "%Harry%"
                        )
                );

        List<Book> criteriaBooks =
                em.createQuery(criteriaQuery)
                        .getResultList();

        System.out.println("\nCriteria API:");

        for (Book b : criteriaBooks) {
            System.out.println(b.getTitle());
        }


        // =========================
        // 7. Dynamic Criteria
        // =========================

        String title = "Palace";
        String authorName = "Naguib Mahfouz";

        CriteriaQuery<Book> dynamicQuery =
                cb.createQuery(Book.class);

        Root<Book> root =
                dynamicQuery.from(Book.class);

        List<Predicate> predicates =
                new ArrayList<>();


        if (title != null && !title.isBlank()) {

            predicates.add(
                    cb.like(
                            root.get("title"),
                            "%" + title + "%"
                    )
            );
        }


        if (authorName != null && !authorName.isBlank()) {

            predicates.add(
                    cb.equal(
                            root.get("author")
                                    .get("name"),
                            authorName
                    )
            );
        }


        dynamicQuery.select(root);

        if (!predicates.isEmpty()) {

            dynamicQuery.where(
                    cb.and(
                            predicates.toArray(
                                    new Predicate[0]
                            )
                    )
            );
        }


        List<Book> filteredBooks =
                em.createQuery(dynamicQuery)
                        .getResultList();

        System.out.println("\nDynamic Criteria:");

        for (Book b : filteredBooks) {
            System.out.println(b.getTitle());
        }


        // =========================
        // 8. Normal LAZY query
        // =========================

        String lazyQuery =
                "SELECT a FROM Author a " +
                        "WHERE a.name = :name";

        Author lazyAuthor =
                em.createQuery(
                                lazyQuery,
                                Author.class
                        )
                        .setParameter(
                                "name",
                                "J.K. Rowling"
                        )
                        .getSingleResult();

        System.out.println("\nLAZY books:");

        System.out.println(
                lazyAuthor.getBooks().size()
        );


        // =========================
        // 9. JOIN FETCH comparison
        // =========================

        String joinFetchQuery =
                "SELECT DISTINCT a " +
                        "FROM Author a " +
                        "JOIN FETCH a.books " +
                        "WHERE a.name = :name";

        Author fetchedAuthor =
                em.createQuery(
                                joinFetchQuery,
                                Author.class
                        )
                        .setParameter(
                                "name",
                                "J.K. Rowling"
                        )
                        .getSingleResult();

        System.out.println("\nJOIN FETCH books:");

        System.out.println(
                fetchedAuthor.getBooks().size()
        );


        // =========================
        // 10. Standard JPQL
        // =========================

        String standardQuery =
                "SELECT b FROM Book b " +
                        "WHERE b.title LIKE :title";

        List<Book> result =
                em.createQuery(
                                standardQuery,
                                Book.class
                        )
                        .setParameter("title", "%Potter%")
                        .getResultList();

        System.out.println("\nStandard JPQL:");

        for (Book b : result) {
            System.out.println(b.getTitle());
        }


        em.close();
        emf.close();
    }

}