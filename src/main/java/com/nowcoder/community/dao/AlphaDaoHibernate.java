package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphahibernate")
public class AlphaDaoHibernate implements AlphaDao{

    @Override
    public String select() {
        return "Hibernate";
    }
}
