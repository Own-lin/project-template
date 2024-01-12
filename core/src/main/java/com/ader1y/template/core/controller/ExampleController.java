package com.ader1y.template.core.controller;

import com.ader1y.template.client.api.v1.ExampleClient;
import com.ader1y.template.model.VO.ExampleInfoVO;
import com.ader1y.template.model.base.BadCode;
import com.ader1y.template.model.base.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController {

    @Resource
    private ExampleClient exampleClient;

    @GetMapping("/get-ex")
    public void getException(){
        BadCode.UN_EXPECTED.throwEx();
    }

    @GetMapping("/info")
    public R<ExampleInfoVO> getInfo(){

        return R.success(new ExampleInfoVO("I'm example.", "empty remark"));
    }

    @GetMapping("/client-info")
    public R<ExampleInfoVO> getClientInfo(){

        return exampleClient.getInfo();
    }

}
