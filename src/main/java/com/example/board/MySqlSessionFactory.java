package com.example.board;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


@Slf4j
public class MySqlSessionFactory {
    private MySqlSessionFactory(){}
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            URL resource = MySqlSessionFactory.class.getClassLoader().getResource("mybatis-config.xml");
            InputStream inputStream = resource.openStream();
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            log.error("MyBatis 설정 파일 가져오는 중 문제 발생", e);
        }
    }


    public static SqlSession getSession(){
        return sqlSessionFactory.openSession(true);
    }
}
