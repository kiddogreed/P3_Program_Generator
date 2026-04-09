package com.church.programgenerator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Validation failures (e.g. @Valid on a form model). */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationError(BindException ex, Model model) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation error: {}", errors);
        model.addAttribute("errorTitle", "Invalid Input");
        model.addAttribute("errorMessage", errors);
        return "error";
    }

    /** Wrong type for a request parameter (e.g. bad date format). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        String msg = "Invalid value for '" + ex.getName() + "'. Please check the format and try again.";
        log.warn("Type mismatch: {}", msg);
        model.addAttribute("errorTitle", "Invalid Input");
        model.addAttribute("errorMessage", msg);
        return "error";
    }

    /** Business-logic or data-access errors. */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(IllegalArgumentException ex, Model model) {
        log.warn("Not found: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /** Catch-all for unexpected errors. */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage",
                "An unexpected error occurred. Please try again or contact the administrator.");
        return "error";
    }
}
