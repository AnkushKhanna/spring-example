package com.transactions.error;

public class DuplicateTransactionException extends RuntimeException
{
    public DuplicateTransactionException(final Long id)
    {
        super(String.format("Transaction already exists for id %d.", id));
    }
}
