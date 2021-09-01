package com.epam.likarnya.service;

import com.epam.likarnya.model.Statement;
import com.epam.likarnya.model.User;

public interface StatementService {
    Statement createOrUpdate(Statement statement);
}
