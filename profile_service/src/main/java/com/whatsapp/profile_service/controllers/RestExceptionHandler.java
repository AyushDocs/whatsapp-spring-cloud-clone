package com.whatsapp.profile_service.controllers;

import java.util.HashMap;
import java.util.Map;

import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.exceptions.RequestValidationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
      @ExceptionHandler(RequestValidationException.class)
      public ResponseEntity<Response<Object>> invalidRequest(RequestValidationException ex) {
            String message = ex.getMessage();
            Response<Object> res = new Response<>(null, message, true);
            return ResponseEntity.badRequest().body(res);
      }

      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ResponseEntity<Response<Map<String, String>>> invalidRequest(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult()
                        .getFieldErrors()
                        .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

            Response<Map<String, String>> res = new Response<>(errors, "errors are there", true);
            return ResponseEntity.badRequest().body(res);
      }
}