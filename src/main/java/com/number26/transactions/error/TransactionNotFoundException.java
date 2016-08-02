package com.number26.transactions.error;

public class TransactionNotFoundException extends RuntimeException
{
    public TransactionNotFoundException(final Long id)
    {
        super(String.format("Transaction %d not found.", id));
    }
}
