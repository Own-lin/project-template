package com.ader1y.template.client.api.v1;

import com.ader1y.template.model.VO.ExampleInfoVO;
import com.ader1y.template.model.base.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        contextId = "ExampleClient",
        path = "/api/v1/example",
        url = "${template.client.url}"
        //  找不到value指定的dns host路径时才会使用URL
        , value = "${template.client.host}"
)
public interface ExampleClient {


    @GetMapping("/info")
    R<ExampleInfoVO> getInfo();


}
