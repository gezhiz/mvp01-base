package com.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 接口测试
 * Created by wenjie on 16/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(name = "parent", locations = {"classpath*:applicationContext*.xml"}),
        @ContextConfiguration(name = "child", locations = {"classpath*:dispatcher-servlet.xml"})
})
@WebAppConfiguration
public class CustomerControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Test
    public void testGet() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//
        RequestBuilder requestBuilder = null;
        //获取登录验证码
        requestBuilder = MockMvcRequestBuilders.get("/v1.0/customer/get_login_vcode")
                .param("mobile","18500865387");
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

        Thread.sleep(5000);
    }

    @Test
    public void testPost() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        RequestBuilder requestBuilder = null;
        requestBuilder = MockMvcRequestBuilders.post("/v1.0/customer/do_login")
                .param("mobile","18500865387")
                .param("code","066808")
                .headers(headers());

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

        Thread.sleep(5000);
    }

    private HttpHeaders headers() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", "1.57b813acd4c60e8fa2943308.1476948447917.F2D60BA3447E1F9C996BD427FF7D207E");
        return httpHeaders;
    }
}
