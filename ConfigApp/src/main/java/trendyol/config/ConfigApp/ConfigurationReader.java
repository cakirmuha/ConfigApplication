package trendyol.config.ConfigApp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.spy.memcached.MemcachedClient;


public class ConfigurationReader{
	
	private String applicationName;
	private String connectionString;
	private Integer refreshTimerIntervalInMs;
	private Connection conn = null;
	private MemcachedClient mcc = null;
	
	private ConcurrentMap<String,Object> configuration = new ConcurrentHashMap<String,Object>();
	
	public ConfigurationReader(String applicationName, String connectionString, 
			Integer refreshTimerIntervalInMs){
		this.applicationName = applicationName;
		this.connectionString = connectionString;
		this.refreshTimerIntervalInMs = refreshTimerIntervalInMs;
		
		try {
			mcc = new MemcachedClient(new
				      InetSocketAddress("127.0.0.1", 11211));
			// create a connection to the database
	        conn = DriverManager.getConnection(connectionString);
	        System.out.println("Connection to SQLite has been established.");	
      	  	selectAll();
	        asyncServiceMethod();
		}catch (SQLException e) {
		     System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	private void asyncServiceMethod(){ 
        Runnable task = new Runnable() {

            public void run() { 
                try {
                  while(true){
                	  Thread.sleep(refreshTimerIntervalInMs);
                	  if(conn.isClosed() || conn ==null){
                		  conn = DriverManager.getConnection(connectionString);
                	  }
                	  selectAll();
                  }
                } catch (Exception ex) { 
                    //handle error which cannot be thrown back 
                } 
            } 
        }; 
        new Thread(task, "ServiceThread").start(); 
    }
	
	private void selectAll(){
        String sql = "SELECT Name, Type, Value FROM AppConfig where IsActive = 1 "
        		+ "and ApplicationName = '" + applicationName + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try{
        	stmt  = conn.createStatement();
        	rs    = stmt.executeQuery(sql);

        	configuration.clear();
        	mcc.flush().isDone();
            // loop through the result set
            while (rs.next()) {
            	String Name = rs.getString("Name");
            	String Type = rs.getString("Type");
            	Object value = null;
            	if (Type.equals("Double")){
            		value = rs.getDouble("Value");
            	}else if(Type.equals("Int")){
            		value = rs.getInt("Value");
            	}else if(Type.equals("String")){
            		value = rs.getString("Value");
            	}else if(Type.equals("Boolean")){
            		value = rs.getBoolean("Value");
            	}else{
            		value = rs.getObject("Value");
            	}
            	
            	//Type is not needed in this manner, value = rs.getObject("Value") is sufficient
            	mcc.set(Name, refreshTimerIntervalInMs/100, value);
            	configuration.put(Name, value);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	@SuppressWarnings("unchecked")
	public <T> T GetValue(String key){	
		return (T)configuration.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T GetValueFromCache(String key){	
		return (T)mcc.get(key);
	}
}
