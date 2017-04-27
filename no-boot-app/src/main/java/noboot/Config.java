package noboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;



import javax.naming.NamingException;
import javax.sql.DataSource;


@Configuration
public class Config {

    static final class UseJndiCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return !"false".equals(System.getProperty("useJndi"));
        }
    };

    @Bean
    @Conditional(UseJndiCondition.class)
    public JndiTemplate jndiTemplate() {
        return new JndiTemplate();
    }

    @Bean
    @Conditional(UseJndiCondition.class)
    public DataSource myDataSource() {
        try {
            return (DataSource) jndiTemplate().lookup("jdbc/datasource");
        }
        catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    @Qualifier("myDataSource")
    DataSource _myDataSource;

    @Bean
    public JdbcTemplate myJdbcTemplate() {
        return new JdbcTemplate(_myDataSource);
    }


}
