package bootapp;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import noboot.Config;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@Import(Config.class)
public class BootApp {

    static Logger logger = LoggerFactory.getLogger(BootApp.class);

    public static void main(String[] args) {
        System.setProperty("useJndi", "false");
        Object[] config = {BootApp.class, Config.class};
        ApplicationContext ctx = SpringApplication.run(config, args);
        JdbcTemplate jdbc = ctx.getBean("myJdbcTemplate", JdbcTemplate.class);

        jdbc.query("SELECT name FROM userz", (rs) -> {
                logger.info("Returned {}", rs.getString("name"));
            }
        );
    }

    @Bean DataSource myDataSource() {
        JdbcDataSource h2 = new JdbcDataSource();
        h2.setUrl("jdbc:h2:mem:myBootDs;INIT=runscript from './create.sql'");
        return h2;
    }
}
