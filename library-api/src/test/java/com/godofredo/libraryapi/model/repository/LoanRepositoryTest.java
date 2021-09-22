package com.godofredo.libraryapi.model.repository;

import com.godofredo.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Return true when find isbn")
    public void returnTrueWhenExistIsbn (){
        String isbn = "123";
        Book book = createANewBook();
        entityManager.persist(book);

        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Return true when not found isbn")
    public void returnTrueWhenNotExistIsbn (){
        String isbn = "123";

        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Find a Book By Id")
    public void findByIdTest(){
        // Given
        Book book = createANewBook();
        entityManager.persist(book);
        //When
        Optional<Book> foundBook = repository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Save a book")
    public void saveBookTest(){
        // Given
        Book book = createANewBook();
        entityManager.persist(book); //Create dynamically on the database
        Book foundBook = entityManager.find(Book.class, book.getId()); // make sure it was there

        repository.delete(foundBook); // Deleted

        Book deletedBook = entityManager.find(Book.class, book.getId());// is it still there?
        assertThat(deletedBook).isNull();// is it null?
    }

    @Test
    @DisplayName("Delete a book")
    public void deleteBookTest(){
        // Given
        Book book = createANewBook();

        //When
        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    public Book createANewBook() {
        return Book.builder().author("Bill").title("Gates 3").isbn("123").build();
    }
}
