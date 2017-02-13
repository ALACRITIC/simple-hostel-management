package org.remipassmoilesel.bookme.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;

/**
 * Helper for form messages. Not used now.
 * <p>
 * FormErrorMap errorMap = new FormErrorMap(reservationResults);
 * model.addAttribute("errorMap", errorMap);
 */
public class FormErrorMap {

    private final List<ObjectError> errors;
    private final HashMap<String, String> messages;

    public FormErrorMap(BindingResult result) {
        this.errors = result.getAllErrors();
        this.messages = new HashMap<>();

        for (ObjectError error : errors) {
            FieldError fe = (FieldError) error;
            setMessage(fe.getField(), error.getDefaultMessage());
        }
    }

    public void setMessage(String objectName, String message) {
        messages.put(objectName, message);
    }

    public String getMessage(String objectName) {
        return messages.get(objectName);
    }

    public boolean hasError(String objectName) {
        return messages.get(objectName) != null;
    }

    @Override
    public String toString() {
        return "FormErrorMap{" +
                "messages=" + messages +
                '}';
    }
}
