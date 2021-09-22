package com.godofredo.libraryapi.service;

import com.godofredo.libraryapi.exception.BusinessException;
import com.godofredo.libraryapi.model.entity.Book;
import com.godofredo.libraryapi.model.repository.BookRepository;
import com.godofredo.libraryapi.service.impl.BookServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){

        this.service = new BookServiceImp(repository);

    }

    @Test
    @DisplayName("Must save a book")
    public void saveBookTest(){
        Book book = createValidBook();
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        when(repository.save(book)).thenReturn(Book.builder().id(1L).isbn("654321").author("Bill").title("Gates 3").build());
        Book bookSaved = service.save(book);
        assertThat(bookSaved.getId()).isNotNull();
        assertThat(bookSaved.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookSaved.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookSaved.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Must throw error duplicated Isbn")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        Book book = createValidBook();

        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Duplicated Isbn");

        verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Get book By Id")
    public void getBookById(){
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(book.getId());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Book Not Found By Id Test")
    public void bookNotFoundByIdTest(){
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Delete book By Id")
    public void deleteBookTest(){
        Long id = 1L;
        Book book = Book.builder().id(id).build();
        when(repository.findById(id)).thenReturn(Optional.of(book));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(()-> service.delete(book));

        verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Delete book By Id not found")
    public void deleteBookByIdNotFoundTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,()-> service.delete(book));

        verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Update book By Id")
    public void updateBookTest(){
        Long id = 1L;
        Book updatingBook = Book.builder().id(id).build();

        Book updatedBook = createValidBook();
        updatedBook.setId(id);

        when(repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    @DisplayName("Update book By Id not found")
    public void updateBookByIdNotFoundTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,()-> service.update(book));

        verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Must to filter books by attributes")
    public void  findBookTest(){
        Book book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0,10);
        List<Book> booksList = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(booksList, pageRequest, 1);
        when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(booksList);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);


    }

    public Book createValidBook() {
        return Book.builder().author("Bill").title("Gates 3").isbn("654321").build();
    }

    @Test
    @DisplayName("Get book by isbn Test")
    public void getBookByIsbnTest (){
        String isbn = "1230";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

        Optional<Book> book = service.getBookByIsbn(isbn);

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get().getId()).isEqualTo(1L);
        assertThat(book.get().getIsbn()).isEqualTo(isbn);

        verify(repository, times(1)).findByIsbn(isbn);

    }
}
