package com.godofredo.libraryapi.service;

import com.godofredo.libraryapi.model.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public interface LoanService {
    Loan save(Loan loan);


}
