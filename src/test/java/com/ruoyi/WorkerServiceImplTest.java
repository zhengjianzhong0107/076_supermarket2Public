package com.ruoyi;

import com.ruoyi.framework.shiro.service.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WorkerServiceImplTest {

    @Autowired
    private PasswordService passwordService;

    @Test
    void allOperatorByPage() {
        System.out.println(passwordService.encryptPassword("admin", "123456", "111111"));
    }
}