package com.sie.testing.validator;

import javax.xml.bind.ValidationException;

import org.springframework.util.StringUtils;

import com.sie.testing.domain.HelloWorldRequest;
import com.sie.testing.domain.ValidatorResult;

public class HelloWorldRequestValidator {

    public ValidatorResult validate(HelloWorldRequest request) throws ValidationException {
        ValidatorResult result = new ValidatorResult();
        if (StringUtils.isEmpty(request.getName())){
            result = new ValidatorResult(false, "NAME MISSING");
        } else if (request.getName().equalsIgnoreCase("EXCEPTION")){
            throw new ValidationException("VALIDATION");
        }
        return result;
    }

}
