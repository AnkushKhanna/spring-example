package com.transactions.controller.error;

import com.transactions.dto.error.ErrorResponseDto;
import com.transactions.error.DuplicateTransactionException;
import com.transactions.error.ParentNotFoundException;
import com.transactions.error.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController
{
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponseDto handleDuplicateTrnansaction(DuplicateTransactionException exception)
    {

        final ErrorResponseDto error = new ErrorResponseDto();
        error.MESSAGE = exception.getMessage();

        return error;
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponseDto handleParentNotFound(ParentNotFoundException exception)
    {
        final ErrorResponseDto error = new ErrorResponseDto();
        error.MESSAGE = exception.getMessage();

        return error;
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleTransactionNotFound(TransactionNotFoundException exception)
    {
        final ErrorResponseDto error = new ErrorResponseDto();
        error.MESSAGE = exception.getMessage();

        return error;
    }
}

