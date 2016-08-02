package com.number26.transactions.service;

import com.google.common.collect.Maps;
import com.number26.transactions.error.DuplicateTransactionException;
import com.number26.transactions.error.ParentNotFoundException;
import com.number26.transactions.error.TransactionNotFoundException;
import com.number26.transactions.model.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Transaction Service, handles all transaction related work.
 *
 * Currently Transactions are saved in Map.
 * In DB (maybe): Use NO SQL -> Graph DB -> Neo4j.
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

    /**
     * Runtime: O(n),
     * would like to do it in better run time,
     * but do not want to over complicate,
     * O(n) is not bad to start with.
     * Improve if start getting slow.
     * Maybe another data-storage.
     */
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
