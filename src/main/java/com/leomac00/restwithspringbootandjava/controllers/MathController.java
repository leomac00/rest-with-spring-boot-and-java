package com.leomac00.restwithspringbootandjava.controllers;

import com.leomac00.restwithspringbootandjava.enums.MathEnum;
import org.springframework.web.bind.annotation.*;

@RestController
public class MathController {

    @GetMapping(value="/{operation}/{numberOne}/{numberTwo}")
    public Object operation(@PathVariable("operation") String operation,
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo) throws Exception {
        MathEnum e = Enum.valueOf(MathEnum.class, operation.toUpperCase());
        return e.calc(numberOne,numberTwo);
    }
}
