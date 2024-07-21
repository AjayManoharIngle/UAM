package org.rebit.auth.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class LoadProperty {

	
public EnableDataSourceNew getPropertyObject(){
    	Properties p=new Properties();
		try(InputStream resource = getClass().getResourceAsStream("/master.properties")){
			p.load(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EnableDataSourceNew eds = new EnableDataSourceNew();
		String isDatasource = p.getProperty("is.datasource.use");
		if("true".equalsIgnoreCase(isDatasource)) {
			eds.setJndiDataSourceUsed(true);
			eds.setDatasourceName(p.getProperty("datasource.name"));
		}else {
			eds.setJndiDataSourceUsed(false);
		}
		return eds;
    }
	
}
