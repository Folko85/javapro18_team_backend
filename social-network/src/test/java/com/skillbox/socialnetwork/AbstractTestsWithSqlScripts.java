package com.skillbox.socialnetwork;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class AbstractTestsWithSqlScripts extends AbstractTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("insert-25-persons.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("insert-30-posts.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("insert-friendships.sql"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}