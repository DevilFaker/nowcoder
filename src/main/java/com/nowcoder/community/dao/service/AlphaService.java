package com.nowcoder.community.dao.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")

public class AlphaService {

    @Autowired
    AlphaDao alphaDao;

    public AlphaService(){
        System.out.println("创建");
    }

    @PostConstruct
    public void init(){
        System.out.println("创建之后");
    }

    @PreDestroy
    public void destory(){
        System.out.println("销毁");
    }

    public String find(){
        return alphaDao.select();
    }
}
