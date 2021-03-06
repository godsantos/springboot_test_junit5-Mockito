package com.godofredo.libraryapi.service.impl;

import com.godofredo.libraryapi.model.entity.Loan;
import com.godofredo.libraryapi.model.repository.LoanRepository;
import com.godofredo.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return repository.save(loan);
    }
}
