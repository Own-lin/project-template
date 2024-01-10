package com.ader1y.template.core.controller;

import com.ader1y.template.core.support.base.BadCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creator")
public class CreatorController {

    @GetMapping("/get-ex")
    public void getException(){
        BadCode.UN_EXPECTED.throwEx();
    }

}
