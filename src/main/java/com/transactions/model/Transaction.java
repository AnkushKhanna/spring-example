package com.transactions.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public class Transaction
{
    public Long id;

    public Double amount;

    public String type;

    public Optional<Long> parentId;

    public List<Long> childrenIds = Lists.newArrayList();
}
