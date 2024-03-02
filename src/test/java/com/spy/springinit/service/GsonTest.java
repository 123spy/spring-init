package com.spy.springinit.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spy.springinit.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GsonTest {

    @Resource
    private UserService userService;

    @Test
    void gsonTest() {
        Long id = 1763921075902726145l;
        User user = userService.getById(id);
        System.out.println(user);

        List<String> tagList = new ArrayList<String>();
        tagList.add("A");
        tagList.add("B");
        tagList.add("C");

        Gson gson = new Gson();
        String tags = gson.toJson(tagList);
        System.out.println("tags :" + tags);

        List<String> userTagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());

        System.out.println("userTagList :" + userTagList);
    }
}
