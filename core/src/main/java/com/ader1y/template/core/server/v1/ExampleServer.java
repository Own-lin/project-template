package com.ader1y.template.core.server.v1;

import com.ader1y.template.model.VO.ExampleInfoVO;
import com.ader1y.template.model.base.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/example")
public class ExampleServer {

    @GetMapping("/info")
    public R<ExampleInfoVO> getInfo(){

        return R.success(new ExampleInfoVO("I'm example server.", "empty remark"));
    }

}
