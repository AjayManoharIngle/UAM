package org.rebit.auth.config;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;


/**
 * 
 * @author Kapil Gautam
 * 
 */

public class ConnectionFactory {

	
	private static interface SingleTon{
	    final static ConnectionFactory INSTANCE = new ConnectionFactory();
	}
 
    private DataSource dataSource;
	
 
    private ConnectionFactory() {
    	Properties p=new Properties();
		try(InputStream resource = getClass().getResourceAsStream("/application.properties")){
			p.load(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}

        Properties properties = new Properties();
        properties.setProperty("user", p.getProperty("spring.datasource.username"));
        properties.setProperty("password", p.getProperty("spring.datasource.password")); // or get properties from some configuration file
        
        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
        		p.getProperty("spring.datasource.url"), properties
        );
        new PoolableConnectionFactory(
                connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED
        );
 
        this.dataSource = new PoolingDataSource(pool);
    }
 
    public static Connection getDatabaseConnection() throws SQLException {
        return SingleTon.INSTANCE.dataSource.getConnection();
    }
    
    public static Connection getDatabaseConnectionWithDataSource() throws NamingException, SQLException {
    	LoadProperty lg = new LoadProperty();
    	EnableDataSourceNew eds=lg.getPropertyObject();
    	if(eds.isJndiDataSourceUsed()) {

    			JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
    			DataSource ds=jndiDataSourceLookup.getDataSource(eds.getDatasourceName());
    			
                return ds.getConnection();
    	}else {
    			return getDatabaseConnection();
		}
    }
}
