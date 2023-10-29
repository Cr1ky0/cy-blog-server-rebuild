package com.criiky0;

import com.criiky0.mapper.MenuMapper;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.service.MenuService;
import com.criiky0.utils.ElasticSearchUtil;
import com.criiky0.utils.JavaMailUtil;
import com.criiky0.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class MyTest {

    @Autowired
    ElasticSearchUtil elasticSearchClientFactory;
    
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuService menuService;
    
    
    @org.junit.jupiter.api.Test
    public void test1() {
        Integer i = JavaMailUtil.sendCode("503094716@qq.com");
        System.out.println(i);
    }

    @org.junit.jupiter.api.Test
    public void test2() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test4(){
        System.out.println(ElasticSearchUtil.client);
        System.out.println(ElasticSearchUtil.client);
        System.out.println(ElasticSearchUtil.client);
    }
    
    @Test
    public void test5() throws IOException {
        ElasticSearchUtil.client.indices().create(c -> c
                .index("users")
        );
    }

    @Test
    public void test6(){
        Result<HashMap<String, List<MenuDTO>>> menuOfCriiky0 = menuService.getMenuOfCriiky0();
        HashMap<String, List<MenuDTO>> data = menuOfCriiky0.getData();
        List<MenuDTO> menuDTOS = data.get("menus");
        System.out.println(menuDTOS);
    }
}
