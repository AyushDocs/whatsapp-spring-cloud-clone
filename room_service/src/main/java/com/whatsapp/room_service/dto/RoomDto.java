package com.whatsapp.room_service.dto;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.whatsapp.room_service.models.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
      @UserEmail
      private List<String> userEmails;
      private String name;

      @Retention(RUNTIME)
      @Constraint(validatedBy = UserEmailValidator.class)
      @Target({ FIELD })
      @Documented
      public @interface UserEmail {
            String message() default "user emails not valid";

            Class<?>[] groups() default {};

            Class<? extends Payload>[] payload() default {};

      }

      public class UserEmailValidator implements ConstraintValidator<UserEmail, List<String>> {
            @Override
            public boolean isValid(List<String> value, ConstraintValidatorContext context) {
                  if (value.size() < 2)
                        return false;
                  if (value.stream().distinct().count() != value.size())
                        return false;
                  return true;
            }

      }

      public Room toRoom() {
            return new Room(null, null, name, getUserEmailsAsString());
      }

      public String getUserEmailsAsString() {
            return userEmails
                        .stream()
                        .filter(i->!i.equals(""))
                        .reduce("", (prev, curr) -> prev + "," + curr);
      }
}
