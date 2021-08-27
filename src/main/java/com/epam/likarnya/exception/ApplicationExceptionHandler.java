package com.epam.likarnya.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {
    private static final String GLOBAL_EXCEPTION_MESSAGE = "Something is wrong";


    @ExceptionHandler (EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException (EntityNotFoundException ex){
        log.error("EntityNotFoundException : ", ex.getMessage());
        ModelAndView model = new ModelAndView("errorPage");
        model.addObject("info",ex.getMessage());
        return model;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    public ModelAndView globalException(Exception ex) {
        log.error(GLOBAL_EXCEPTION_MESSAGE, ex);
        ModelAndView model = new ModelAndView("errorPage");
        model.addObject("info",GLOBAL_EXCEPTION_MESSAGE);
        return model;
    }
}
