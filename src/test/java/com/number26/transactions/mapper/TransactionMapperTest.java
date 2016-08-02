package com.number26.transactions.mapper;

import com.number26.transactions.dto.TransactionDto;
import com.number26.transactions.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionMapperTest
{
    @InjectMocks
    private TransactionMapper mapper;

    @Test
    public void map_transactionDto_to_transaction() throws Exception
    {
        TransactionDto dto = new TransactionDto();
        dto.amount = 10D;
        dto.type = "string";
        dto.parentId = null;

        Transaction transaction = mapper.map(100L, dto);

        assertEquals(100, transaction.id.intValue());
        assertEquals(10, transaction.amount.intValue());
        assertEquals("string", transaction.type);
        assertFalse(transaction.parentId.isPresent());
    }

    @Test
    public void map_transaction_to_transactionDto() throws Exception
    {
        Transaction transaction = new Transaction();
        transaction.id = 100L;
        transaction.amount = 10D;
        transaction.type = "string";
        transaction.parentId = Optional.of(10L);

        TransactionDto dto = mapper.map(transaction);

        assertEquals(10, dto.amount.intValue());
        assertEquals("string", dto.type);
        assertEquals(10, dto.parentId.intValue());
    }
}
