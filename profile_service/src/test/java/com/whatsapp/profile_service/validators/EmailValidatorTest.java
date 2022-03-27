package com.whatsapp.profile_service.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.whatsapp.profile_service.validators.EmailValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailValidatorTest {
    private EmailValidator underTest;
    @BeforeEach
    public void setUp() {
       underTest=new EmailValidator();
    }
    @Test
    void validate(){
       assertTrue(underTest.test("ayush@gmail.com"));
       assertFalse(underTest.test("ayush"));
       assertFalse(underTest.test("ayush@"));
       assertFalse(underTest.test("ayush@."));
    }
}