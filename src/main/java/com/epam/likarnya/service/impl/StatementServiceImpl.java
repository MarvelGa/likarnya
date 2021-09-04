package com.epam.likarnya.service.impl;

import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Statement;
import com.epam.likarnya.repository.StatementRepository;
import com.epam.likarnya.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatementServiceImpl implements StatementService {
    private final StatementRepository statementRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Statement createOrUpdate(Statement statement) {
        if (statement.getId()!=null){
            Optional<Statement> optionalStatement = statementRepository.findById(statement.getId());

            if(optionalStatement.isPresent()){
                Statement newStatement = optionalStatement.get();
                newStatement.setPatientStatus(statement.getPatientStatus());
                newStatement.setChanged(LocalDateTime.now());
                return statementRepository.save(newStatement);
            }
        }
        return statementRepository.save(statement);
    }

    @Override
    public Statement getById(Long id) {
        return statementRepository.findById(id).orElseThrow(()->new EntityNotFoundException(String.format("Statement with id = %s was not found", id)));
    }
}
