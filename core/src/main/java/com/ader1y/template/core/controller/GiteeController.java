package com.ader1y.template.core.controller;

import com.ader1y.template.model.base.BadCode;
import com.ader1y.template.model.base.BusinessCode;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/gitee")
public class GiteeController {

    private final RestClient restClient = RestClient.create();


    @GetMapping("/get-image")
    public void getImage(HttpServletResponse response, @RequestParam("path") String path) {
        int suffixIndex = StringUtils.lastIndexOf(path, ".");
        if (suffixIndex == -1) BusinessCode.UN_SUPPORT_PARAM.throwEx("只能获取图片");
        String imgSuffix = path.substring(StringUtils.lastIndexOf(path, "."));
        if (StringUtils.isBlank(imgSuffix)) BusinessCode.UN_SUPPORT_PARAM.throwEx("未知的图片格式");

        AtomicReference<MediaType> mediaType = new AtomicReference<>(MediaType.ALL);
        byte[] info = restClient.get()
                .uri(path)
                .header("Host", "gitee.com")
                .accept(MediaType.ALL)
                .accept(MediaType.IMAGE_PNG)
                .accept(MediaType.IMAGE_GIF)
                .accept(MediaType.IMAGE_JPEG)
                .accept(MediaType.TEXT_HTML)
                .exchange((req, res) -> {
                    if (!res.getStatusCode().isError()) {
                        mediaType.set(res.getHeaders().getContentType());
                        return res.getBody().readAllBytes();
                    } else {
                        BadCode.GET_IMAGE_ERROR.throwEx(res.getBody());
                    }
                    return new byte[]{};
                });

        try (OutputStream os = response.getOutputStream()) {
            response.setContentType(mediaType.get().toString());
            os.write(info);
        } catch (IOException e) {
            BadCode.GET_IMAGE_ERROR.throwEx(e.getMessage());
        }
    }

}
