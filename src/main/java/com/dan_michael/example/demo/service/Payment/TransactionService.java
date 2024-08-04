package com.dan_michael.example.demo.service.Payment;

import com.dan_michael.example.demo.model.entities.Transaction;
import com.dan_michael.example.demo.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setGateway(transactionDetails.getGateway());
        transaction.setTransactionDate(transactionDetails.getTransactionDate());
        transaction.setAccountNumber(transactionDetails.getAccountNumber());
        transaction.setCode(transactionDetails.getCode());
        transaction.setContent(transactionDetails.getContent());
        transaction.setTransferType(transactionDetails.getTransferType());
        transaction.setTransferAmount(transactionDetails.getTransferAmount());
        transaction.setAccumulated(transactionDetails.getAccumulated());
        transaction.setSubAccount(transactionDetails.getSubAccount());
        transaction.setReferenceCode(transactionDetails.getReferenceCode());
        transaction.setDescription(transactionDetails.getDescription());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        transactionRepository.delete(transaction);
    }
}
