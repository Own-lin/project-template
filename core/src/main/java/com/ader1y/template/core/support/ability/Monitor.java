package com.ader1y.template.core.support.ability;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class Monitor {

    private static final Logger LOG = getLogger(Monitor.class);

    public static final String ROBOT_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=61f2cd2e-8d86-45d3-809e-3f434db882ca";
    private final RestClient restClient = RestClient.create();


    public void sendNotify(String message) {
        try {
            LOG.warn("send monitor msg");

            JSONObject postBodyJson = new JSONObject()
                    .fluentPut("msgtype", "text")
                    .fluentPut("text", new JSONObject().fluentPut("content", "Gitee图床告警: \n" + message));

            ResponseEntity<String> response = restClient.post()
                    .uri(ROBOT_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(postBodyJson.toString())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (q, s) -> LOG.error("推送告警失败: {}", s.getBody()))
                    .toEntity(String.class);
            JSONObject responseJSON = JSON.parseObject(response.getBody());
            if (responseJSON != null && !"0".equals(responseJSON.get("errcode"))) {
                LOG.error("推送告警消息到机器人返回 {} 机器人返回异常信息 {}", postBodyJson, responseJSON.get("errmsg"));
            }
        } catch (Exception e) {
            LOG.error("推送告警失败: {}", e.getMessage());
        }
    }

}
