package com.whatsapp.profile_service.exceptions;

public class RequestValidationException extends RuntimeException {
      public RequestValidationException(String message) {
            super(message);
      }
}
