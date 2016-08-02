package com.number26.transactions.controller;

import com.number26.transactions.dto.TransactionDto;
import com.number26.transactions.mapper.TransactionMapper;
import com.number26.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping(value = "/transactionservice")
public class TransactionController
{
    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionMapper mapper;

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/transaction/{transactionId}",
        method = RequestMethod.PUT,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(
        @PathVariable("transactionId") Long transactionId, @Valid @RequestBody TransactionDto dto
    ) throws Exception
    {
        service.add(mapper.map(transactionId, dto));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/transaction/{transactionId}",
        method = RequestMethod.GET,
        consumes = MediaType.ALL_VALUE)
    public TransactionDto get(@PathVariable("transactionId") Long transactionId) throws Exception
    {
        return mapper.map(service.get(transactionId));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/types/{type}",
        method = RequestMethod.GET,
        consumes = MediaType.ALL_VALUE)
    public List<Long> get(@PathVariable("type") String type) throws Exception
    {
        return service.getTransactions(type).stream().map(t -> t.id).collect(toList());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/sum/{transactionId}",
        method = RequestMethod.GET,
        consumes = MediaType.ALL_VALUE)
    public Double sum(@PathVariable("transactionId") Long transactionId) throws Exception
    {
        return service.sum(transactionId);
    }
}
