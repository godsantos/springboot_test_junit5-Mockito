package com.godofredo.libraryapi.api.resource;

import com.godofredo.libraryapi.api.dto.LoanDTO;
import com.godofredo.libraryapi.model.entity.Book;
import com.godofredo.libraryapi.model.entity.Loan;
import com.godofredo.libraryapi.service.BookService;
import com.godofredo.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO){
        Book book = bookService
                .getBookByIsbn(loanDTO.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
        Loan entity = Loan.builder()
                        .book(book)
                        .customer(loanDTO.getCustomer())
                        .loanDate(LocalDate.now())
                        .build();

        entity = loanService.save(entity);
        return entity.getId();
    }
}
