package com.number26.transactions.service;

import com.google.common.collect.Lists;
import com.number26.transactions.error.ParentNotFoundException;
import com.number26.transactions.error.TransactionNotFoundException;
import com.number26.transactions.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest
{
    @InjectMocks
    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception
    {
        transactionService.init();
    }

    @Test
    public void add_adds_transaction_correctly() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");

        transactionService.add(transaction);
    }

    @Test
    public void get_returns_transaction_correctly() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");

        transactionService.add(transaction);

        Transaction returnedTransaction = transactionService.get(10L);

        assertEquals(transaction, returnedTransaction);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void get_returns_error_when_transaction_not_present() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");

        transactionService.add(transaction);

        Transaction returnedTransaction = transactionService.get(11L);

        assertEquals(transaction, returnedTransaction);
    }

    @Test
    public void add_with_parent_should_add_correctly() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");
        Transaction childTransaction = createTransaction(11L, 50D, "string", Optional.of(10L));
        Transaction childTransaction2 = createTransaction(12L, 50D, "string", Optional.of(10L));

        transactionService.add(transaction);
        transactionService.add(childTransaction);
        transactionService.add(childTransaction2);

        Transaction returnedTransaction = transactionService.get(10L);

        assertEquals(2, returnedTransaction.childrenIds.size());
        assertArrayEquals(Lists.newArrayList(11L, 12L).toArray(), returnedTransaction.childrenIds.toArray());
    }

    @Test(expected = ParentNotFoundException.class)
    public void add_without_parent_should_throw_exception() throws Exception
    {
        Transaction childTransaction = createTransaction(11L, 50D, "string", Optional.of(10L));

        transactionService.add(childTransaction);
    }

    @Test
    public void sum_return_correct_sum_for_depth_1() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");
        Transaction childTransaction = createTransaction(11L, 150D, "string", Optional.of(10L));
        Transaction childTransaction2 = createTransaction(12L, 15D, "string", Optional.of(10L));

        transactionService.add(transaction);
        transactionService.add(childTransaction);
        transactionService.add(childTransaction2);

        Double sum = transactionService.sum(10L);

        assertEquals(215, sum.intValue());
    }

    @Test
    public void sum_return_correct_sum_for_depth_2() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");
        Transaction childTransaction1 = createTransaction(11L, 150D, "string", Optional.of(10L));
        Transaction childTransaction2 = createTransaction(12L, 15D, "string", Optional.of(10L));

        Transaction childTransaction11 = createTransaction(13L, 125D, "long", Optional.of(11L));
        Transaction childTransaction21 = createTransaction(14L, 19D, "long", Optional.of(12L));

        transactionService.add(transaction);
        transactionService.add(childTransaction1);
        transactionService.add(childTransaction2);
        transactionService.add(childTransaction11);
        transactionService.add(childTransaction21);

        Double sum = transactionService.sum(10L);

        assertEquals(359, sum.intValue());
    }

    @Test
    public void type_should_return_correct_transactions() throws Exception
    {
        Transaction transaction = createTransaction(10L, 50D, "string");
        Transaction childTransaction1 = createTransaction(11L, 150D, "long", Optional.of(10L));
        Transaction childTransaction2 = createTransaction(12L, 15D, "int", Optional.of(10L));

        Transaction childTransaction11 = createTransaction(13L, 125D, "long", Optional.of(11L));
        Transaction childTransaction21 = createTransaction(14L, 19D, "long", Optional.of(12L));

        transactionService.add(transaction);
        transactionService.add(childTransaction1);
        transactionService.add(childTransaction2);
        transactionService.add(childTransaction11);
        transactionService.add(childTransaction21);

        List<Transaction> stringType = transactionService.getTransactions("string");

        assertEquals(1, stringType.size());
        assertEquals(10, stringType.get(0).id.intValue());

        List<Transaction> longType = transactionService.getTransactions("long");

        assertEquals(3, longType.size());
        assertArrayEquals(Lists.newArrayList(11L, 13L, 14L).toArray(), longType.stream().map(t -> t.id).collect(toList()).toArray());

    }

    private Transaction createTransaction(Long id, Double amount, String type)
    {
        return createTransaction(id, amount, type, Optional.empty());
    }

    private Transaction createTransaction(Long id, Double amount, String type, Optional<Long> parentId)
    {
        Transaction transaction = new Transaction();
        transaction.id = id;
        transaction.amount = amount;
        transaction.type = type;
        transaction.parentId = parentId;

        return transaction;
    }
}
