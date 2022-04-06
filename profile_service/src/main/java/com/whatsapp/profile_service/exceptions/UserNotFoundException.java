package com.whatsapp.profile_service.exceptions;

public class UserNotFoundException extends RuntimeException {

      public UserNotFoundException(String string) {
            super(string);
      }
      public UserNotFoundException() {
            super();
      }
    
}
