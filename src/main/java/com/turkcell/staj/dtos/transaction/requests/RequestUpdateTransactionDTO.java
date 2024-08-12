package com.turkcell.staj.dtos.transaction.requests;

import com.turkcell.staj.core.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateTransactionDTO {
    @NotNull(message = "Status can't be null")
    private Status status;
}
