package com.athaydes.web.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.athaydes.web.dao.Cache;
import com.athaydes.web.dao.CacheInfo;
import com.athaydes.web.dao.CacheThroughput;
import com.athaydes.web.dao.Cluster;
import com.athaydes.web.dao.Dao;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;

/**
 * User: Renato
 */
public class ServerTest {

	static final URI BASE_URI = getBaseURI();
	private static final String URL_THROUGHPUT = "http://ocinterview--frontend-1096888245.us-west-2.elb.amazonaws.com/caches/throughput";
	private static final String URL_CACHE = "http://ocinterview--frontend-1096888245.us-west-2.elb.amazonaws.com/caches";
	HttpServer server;

	private static URI getBaseURI() {
		return UriBuilder.fromUri( "http://localhost/" ).port( 9998 ).build();
	}

	static class TestDao implements Dao<String> {

		@Override
		public List<? extends String> getAll() {
			return Arrays.asList( "stuff1", "stuff2", "stuff3" );
		}

		@Override
		public String getById( String id ) {
			if ( id.equals( "id1" ) ) return "stuff1";
			return null;
		}
	}

	@Before
	public void startServer() throws IOException {
		System.out.println( "Starting grizzly..." );

		Injector injector = Guice.createInjector( new ServletModule() {
			@Override
			protected void configureServlets() {
				bind( new TypeLiteral<Dao<String>>() {
				} ).to( TestDao.class );
			}
		} );

		ResourceConfig rc = new PackagesResourceConfig( "com.athaydes.web.server" );
		IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory( rc, injector );
		server = GrizzlyServerFactory.createHttpServer( BASE_URI + "services/", rc, ioc );

		System.out.println( String.format( "Jersey app started with WADL available at "
				+ "%sservices/application.wadl\nTry out %s{app_name}\nHit enter to stop it...",
				BASE_URI, BASE_URI ) );
	}

	@After
	public void stopServer() {
		server.stop();
	}
	
	@Test
	public void testJsonToJavaObjects() throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		List<Cache> cacheList = mapper.readValue(getDataByJsonIo(URL_CACHE), new TypeReference<List<Cache>>(){});
		List<CacheThroughput> throughputList = mapper.readValue(getDataByJsonIo(URL_THROUGHPUT), new TypeReference<List<CacheThroughput>>(){});
		/*System.out.println(cacheList);
		System.out.println(throughputList);
		*/
		
		HashMap<String, List<CacheInfo>> clusterMap = new HashMap<String, List<CacheInfo>>();
		HashMap<String, CacheThroughput> cacheMap = new HashMap<String, CacheThroughput>();
//		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
		float threshold = 20;
		
		for (CacheThroughput throughput : throughputList)
		{
			if (!cacheMap.containsKey(throughput.getUuid()) 
					|| Long.valueOf(cacheMap.get(throughput).getUuid()) < Long.valueOf(throughput.getTs()))
				cacheMap.put(throughput.getUuid(), throughput);
		}
		
		for (Cache cache : cacheList)
		{
			if (!clusterMap.containsKey(cache.getCluster()))
				clusterMap.put(cache.getCluster(), new ArrayList<CacheInfo>());
			clusterMap.get(cache.getCluster()).add(
					new CacheInfo(cache.getUuid(), cache.getId(), cache.getCache(), cache.getCluster(), 
							Long.valueOf(cache.getTs()), Double.valueOf(cacheMap.get(cache.getUuid()).getValue())
					));
		}		
	
		for (String key : clusterMap.keySet()) {
			Cluster cluster = new Cluster(key, clusterMap.get(key));
			cluster.calcOutlier(threshold);
			cluster.printOutlier();
		}	
	}
	
	private String getDataByJsonIo(String url) throws IOException {
		// TODO Auto-generated method stub
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try{
			inputStream = new java.net.URL(url).openStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			return readData(bufferedReader);
		} catch (IOException e){
			throw e;
		} finally {
			//closeResource(inputStream);
		}
		
	}

	private String readData(Reader reader) throws IOException {
		// TODO Auto-generated method stub
		StringBuilder strb = new StringBuilder();
		int c;
		while ((c = reader.read()) != -1){
			strb.append((char)c);
		}
		return strb.toString();
	}

/*	@Test
	public void testGetAll() throws IOException {
		Client client = Client.create( new DefaultClientConfig() );
		WebResource service = client.resource( getBaseURI() );

		ClientResponse resp = service.path( "services" ).path( "stuff" )
				.accept( MediaType.TEXT_HTML )
				.get( ClientResponse.class );
		System.out.println( "Got stuff: " + resp );
		String text = resp.getEntity( String.class );

		assertEquals( 200, resp.getStatus() );
		assertEquals( "<h2>All stuff</h2><ul>" +
				"<li>stuff1</li>" +
				"<li>stuff2</li>" +
				"<li>stuff3</li></ul>", text );

	}

	@Test
	public void testGetById() throws IOException {
		Client client = Client.create( new DefaultClientConfig() );
		WebResource service = client.resource( getBaseURI() );

		ClientResponse resp = service.path( "services" )
				.path( "stuff" ).path( "id1" )
				.accept( MediaType.TEXT_HTML )
				.get( ClientResponse.class );
		System.out.println( "Got stuff: " + resp );
		String text = resp.getEntity( String.class );

		assertEquals( 200, resp.getStatus() );
		assertEquals( "<html><body><div>stuff1</div></body></html>", text );

		String text2 = service.path( "services" )
				.path( "stuff" ).path( "non_existent_id" )
				.accept( MediaType.TEXT_HTML )
				.get( String.class );

		assertEquals( 200, resp.getStatus() );
		assertEquals( "<html><body><div>Not Found</div></body></html>", text2 );

	}
*/
	public static void main( String[] args ) throws Exception {
		ServerTest test = new ServerTest();
		test.startServer();
		System.in.read(); // hit enter to stop the server
		test.server.stop();
	}

}
