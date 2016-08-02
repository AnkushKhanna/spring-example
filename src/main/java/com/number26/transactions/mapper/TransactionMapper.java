package com.number26.transactions.mapper;

import com.number26.transactions.dto.TransactionDto;
import com.number26.transactions.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionMapper
{
    public Transaction map(Long id, TransactionDto dto)
    {
        Transaction transaction = new Transaction();
        transaction.id = id;
        transaction.amount = dto.amount;
        transaction.type = dto.type;
        transaction.parentId = Optional.ofNullable(dto.parentId);

        return transaction;
    }

    public TransactionDto map(Transaction transaction)
    {
        TransactionDto dto = new TransactionDto();
        dto.amount = transaction.amount;
        dto.type = transaction.type;
        dto.parentId = transaction.parentId.orElse(null);

        return dto;
    }
}
