package com.transactions.service;

import com.google.common.collect.Maps;
import com.transactions.error.DuplicateTransactionException;
import com.transactions.error.ParentNotFoundException;
import com.transactions.error.TransactionNotFoundException;
import com.transactions.model.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Transaction Service, handles all transaction.
 * */
@Service
public class TransactionService
{
    private Map<Long, Transaction> transactions;

    @PostConstruct
    public void init()
    {
        transactions = Maps.newHashMap();
    }

    public void add(Transaction transaction)
    {
        if (transactions.containsKey(transaction.id)) {
            throw new DuplicateTransactionException(transaction.id);
        }

        addChildren(transaction);

        transactions.put(transaction.id, transaction);
    }

    public Transaction get(Long id)
    {
        Transaction transaction = transactions.get(id);
        if (transaction == null) {
            throw new TransactionNotFoundException(id);
        }

        return transaction;
    }

    public List<Transaction> getTransactions(String type)
    {
        return transactions.values()
            .stream()
            .filter(trans -> trans.type.equals(type))
            .collect(toList());
    }

    /**
     * Runtime: O(d), where d is the number of children transitively.
     */
    public Double sum(Long id)
    {
        Transaction transaction = transactions.get(id);

        if (transaction == null) {
            throw new TransactionNotFoundException(id);
        }

        return transaction.amount + transaction.childrenIds.stream().mapToDouble(this::sum).sum();
    }

    /**
     * Adding children in Transaction,
     * make sum calculation easier.
     */
    private void addChildren(final Transaction transaction)
    {
        if (transaction.parentId.isPresent()) {
            Transaction parentTransaction = transactions.computeIfPresent(
                transaction.parentId.get(), (id, parent) -> {
                    parent.childrenIds.add(transaction.id);
                    return parent;
                }
            );

            if (parentTransaction == null) {
                throw new ParentNotFoundException(transaction.parentId.get(), transaction.id);
            }
        }
    }
}
