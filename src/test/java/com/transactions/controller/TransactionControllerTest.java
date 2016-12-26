package com.transactions.controller;

import com.google.common.collect.Maps;
import com.transactions.Application;
import com.transactions.mapper.TransactionMapper;
import com.transactions.model.Transaction;
import com.transactions.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class TransactionControllerTest
{
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TransactionMapper mapper;

    @Autowired
    private TransactionService transactionService;

    private MockMvc mvc;

    @Before
    public void baseSetUp() throws Exception
    {
        ReflectionTestUtils.setField(transactionService, "transactions", Maps.newHashMap());
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void get_status_not_found_when_transaction_does_not_exists() throws Exception
    {
        mvc
            .perform(get("/transactionservice/transaction/11")).andExpect(status().isNotFound());
    }

    @Test
    public void get_status_ok_when_transaction_exists() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 40D;
        t.type = "string";
        t.parentId = Optional.empty();

        transactionService.add(t);

        mvc
            .perform(get("/transactionservice/transaction/11")).andExpect(status().isOk());
    }

    @Test
    public void put_status_created_when_transaction_is_added() throws Exception
    {
        mvc
            .perform(
                put("/transactionservice/transaction/18")
                    .content("{\"amount\":5000,\"type\":\"cars\"}").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated());
    }

    @Test
    public void put_status_conflict_when_transaction_already_exists() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 40D;
        t.type = "string";
        t.parentId = Optional.empty();

        transactionService.add(t);

        mvc
            .perform(
                put("/transactionservice/transaction/11")
                    .content("{\"amount\":5000,\"type\":\"cars\"}").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isConflict());
    }

    @Test
    public void get_type_status_ok_when_transactions_exists() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 40D;
        t.type = "string";
        t.parentId = Optional.empty();

        transactionService.add(t);

        MvcResult mvcResult = mvc
            .perform(get("/transactionservice/types/string"))
            .andExpect(status().isOk()).andReturn();

        assertEquals("[11]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void get_type_status_ok_when_multiple_transactions_exists() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 40D;
        t.type = "string";
        t.parentId = Optional.empty();

        Transaction t2 = new Transaction();
        t2.id = 12L;
        t2.amount = 40D;
        t2.type = "string";
        t2.parentId = Optional.empty();

        transactionService.add(t);
        transactionService.add(t2);

        MvcResult mvcResult = mvc
            .perform(get("/transactionservice/types/string"))
            .andExpect(status().isOk()).andReturn();

        assertEquals("[11,12]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void get_type_return_empty_when_no_transaction_match() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 40D;
        t.type = "string";
        t.parentId = Optional.empty();

        transactionService.add(t);

        MvcResult mvcResult = mvc
            .perform(get("/transactionservice/types/long"))
            .andExpect(status().isOk()).andReturn();

        assertEquals("[]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void get_sum_return_correct_sum_with_one_tranaction() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 41D;
        t.type = "string";
        t.parentId = Optional.empty();

        transactionService.add(t);

        MvcResult mvcResult = mvc
            .perform(get("/transactionservice/sum/11"))
            .andExpect(status().isOk()).andReturn();

        assertEquals("41.0", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void get_sum_return_NOT_FOUND_sum_with_one_transaction() throws Exception
    {
        mvc
            .perform(get("/transactionservice/sum/11"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void get_sum_return_sum_with_transitive_transaction() throws Exception
    {
        Transaction t = new Transaction();
        t.id = 11L;
        t.amount = 41D;
        t.type = "string";
        t.parentId = Optional.empty();

        transactionService.add(t);

        Transaction t2 = new Transaction();
        t2.id = 12L;
        t2.amount = 90D;
        t2.type = "string";
        t2.parentId = Optional.of(11L);

        transactionService.add(t2);

        MvcResult mvcResult = mvc
            .perform(get("/transactionservice/sum/11"))
            .andExpect(status().isOk()).andReturn();

        assertEquals("131.0", mvcResult.getResponse().getContentAsString());
    }
}
