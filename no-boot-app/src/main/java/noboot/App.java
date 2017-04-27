package noboot;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        JdbcDataSource h2 = new JdbcDataSource();
        h2.setUrl("jdbc:h2:mem:myDs;INIT=runscript from './create.sql'");


        SimpleNamingContextBuilder jndi = new SimpleNamingContextBuilder();
        jndi.bind("jdbc/datasource", h2);
        try {
            jndi.activate();
        }
        catch (NamingException e) {
            throw new RuntimeException(e);
        }

        ApplicationContext ctx = new AnnotationConfigApplicationContext("noboot");

        JdbcTemplate jdbc = ctx.getBean(JdbcTemplate.class);

        jdbc.query("SELECT name FROM userz", new RowCallbackHandler() {
            @Override public void processRow(ResultSet rs) throws SQLException {
                System.out.println("Returned " + rs.getString("name"));
            }
        });
    }
}
