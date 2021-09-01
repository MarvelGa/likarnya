package com.epam.likarnya.service.impl;

import com.epam.likarnya.model.Statement;
import com.epam.likarnya.repository.StatementRepository;
import com.epam.likarnya.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatementServiceImpl implements StatementService {
    private final StatementRepository statementRepository;

    @Override
    public Statement createOrUpdate(Statement statement) {
        if (statement.getId()!=null){
            Optional<Statement> optionalStatement = statementRepository.findById(statement.getId());

            if(optionalStatement.isPresent()){
                Statement newStatement = optionalStatement.get();
                newStatement.setPatientStatus(statement.getPatientStatus());
                return statementRepository.save(newStatement);
            }
        }
        return statementRepository.save(statement);
    }
}
