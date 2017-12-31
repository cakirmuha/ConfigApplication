package trendyol.config.ConfigApp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import trendyol.config.ConfigApp.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurationReaderTest {

	private ConfigurationReader conf;
	private static String connectionString;
	private static String applicationName;
	private static int refreshTimerIntervalInMs;
	
	public ConfigurationReaderTest() throws IOException {
		conf = new ConfigurationReader(applicationName, connectionString, refreshTimerIntervalInMs);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connectionString = "jdbc:sqlite:src/main/java/resources/Trendyol.db";
		applicationName = "SERVICE-A";
		refreshTimerIntervalInMs = 23432;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetValue(){
		assertEquals("trendyol.com",conf.GetValue("SiteName"));
	}

	@Test
	public void testGetValueFromCache(){
		assertEquals("trendyol.com",conf.GetValueFromCache("SiteName"));
	}

}


