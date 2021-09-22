package com.godofredo.libraryapi.service;

import com.godofredo.libraryapi.exception.BusinessException;
import com.godofredo.libraryapi.model.entity.Book;
import com.godofredo.libraryapi.model.entity.Loan;
import com.godofredo.libraryapi.model.repository.BookRepository;
import com.godofredo.libraryapi.model.repository.LoanRepository;
import com.godofredo.libraryapi.service.impl.BookServiceImp;
import com.godofredo.libraryapi.service.impl.LoanServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    LoanRepository repository;
    LoanService loanService;

    @BeforeEach
    public void setUp(){
        this.loanService = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Must save a loan")
    public void saveLoanTest(){
        Book book = Book.builder().id(1L).build();
        String customer = "Neo";

        Loan savingLoan = Loan.builder().id(1L).customer(customer).loanDate(LocalDate.now()).build();

        Loan savedLoan = Loan.builder().id(1L).customer(customer).loanDate(LocalDate.now()).book(book).build();
        when(repository.save(savingLoan)).thenReturn(savedLoan);

        Loan loan = loanService.save(savingLoan);

        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());

    }
}
