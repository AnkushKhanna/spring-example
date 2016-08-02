package com.number26.transactions.error;

public class ParentNotFoundException extends RuntimeException
{
    public ParentNotFoundException(final Long parentId, final Long id)
    {
        super(String.format("Parent %d not present till now for %d.", parentId, id));
    }
}
