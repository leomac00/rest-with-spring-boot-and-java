package com.leomac00.restwithspringbootandjava.controllers;

import com.leomac00.restwithspringbootandjava.enums.MathEnum;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MathController {

    @GetMapping(value="/math/{operation}")
    public Number operation(@PathVariable("operation") String operation,
                                                       @RequestParam(value = "n1") String numberOne,
                                                       @RequestParam(value = "n2", required = false) String numberTwo)throws Exception {
        MathEnum e = Enum.valueOf(MathEnum.class, operation.toUpperCase());
        return e.calc(numberOne,numberTwo);
    }
}
