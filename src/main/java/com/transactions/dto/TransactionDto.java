package com.transactions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotNull;

@JsonInclude(Include.NON_NULL)
public class TransactionDto
{
    @NotNull
    public Double amount;

    @NotNull
    public String type;

    public Long parentId;
}
