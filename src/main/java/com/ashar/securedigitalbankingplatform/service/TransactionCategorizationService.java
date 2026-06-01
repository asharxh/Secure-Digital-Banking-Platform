package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.entity.TransactionCategory;
import org.springframework.stereotype.Service;

@Service
public class TransactionCategorizationService {

    public TransactionCategory categorize(String description) {
        String text = description.toLowerCase();

        if (text.contains("amazon"))
            return TransactionCategory.SHOPPING;

        if (text.contains("flipkart"))
            return TransactionCategory.SHOPPING;

        if (text.contains("uber"))
            return TransactionCategory.TRANSPORT;

        if (text.contains("ola"))
            return TransactionCategory.TRANSPORT;

        if (text.contains("netflix"))
            return TransactionCategory.ENTERTAINMENT;

        if (text.contains("spotify"))
            return TransactionCategory.ENTERTAINMENT;

        if (text.contains("swiggy"))
            return TransactionCategory.FOOD;

        if (text.contains("zomato"))
            return TransactionCategory.FOOD;

        if (text.contains("deposit"))
            return TransactionCategory.TRANSFER;

        if (text.contains("withdraw"))
            return TransactionCategory.TRANSFER;

        if (text.contains("money sent"))
            return TransactionCategory.TRANSFER;

        if (text.contains("money received"))
            return TransactionCategory.TRANSFER;

        return TransactionCategory.OTHER;
    }
}