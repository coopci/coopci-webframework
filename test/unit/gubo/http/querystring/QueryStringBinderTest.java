package gubo.http.querystring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import gubo.http.grizzly.demo.DemoRequest;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler.BytesReadHandler;
import gubo.http.querystring.QueryStringBinder.JDBCOrderBy;
import gubo.http.querystring.QueryStringBinder.JDBCWhere;

@RunWith(MockitoJUnitRunner.class)
public class QueryStringBinderTest {
    QueryStringBinder testee = new QueryStringBinder();
    
	@Test
	public void testJDBCWhere() throws Exception {
		
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("eq__isVIP", "1");
		data.put("isnull__isVIP", "1");
		data.put("eq1__isVIP", "1");
		data.put("order_by", "isVIP desc, age");
		
		JDBCWhere jdbcWhere = testee.genJDBCWhere(data, Person.class, null);
		System.out.println("whereClause: " + jdbcWhere.whereClause);
		for (Object o : jdbcWhere.params) {
			System.out.println("\tparam: " + o);
		}
	}
	
	@Test
    public void testJDBCOrderBy() throws Exception {
        
        Map<String, String> data = new HashMap<String, String>();
        data.put("order_by", "isVIP desc, age");
        data.put("eq__isVIP", "1");
        data.put("isnull__isVIP", "1");
        data.put("eq1__isVIP", "1");
        
        JDBCOrderBy jdbcOrderBy = testee.genJDBCOrderBy(data, Person.class, null);
        System.out.println("orderByClause: " + jdbcOrderBy.getOrderByClause());
        
        assertEquals("order by isVIP desc, age", jdbcOrderBy.getOrderByClause());
        
    }
	class Animal {
		@QueryStringField()
		public String bloodColor="red";
	}
	
	class Dog extends Animal {
		@QueryStringField(name="legs_num")
		public int legsNum = 4;
	}
	@Test
	public void test_toQueryString() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Dog dog = new Dog();
		dog.bloodColor="re d";
		QueryStringBinder binder = new QueryStringBinder();
		String qs = binder.toQueryString(dog, null);
		System.out.println(qs);
		assertTrue(binder.toQueryString(dog, null).contains("bloodColor=re%20d&"));
		assertTrue(binder.toQueryString(dog, null).contains("legs_num=4"));
	}
	
	@Test
    public void test_MapToQueryString() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
        Map<String, String> data = new HashMap<String, String>();
        data.put("key1", "value 1+2");
        QueryStringBinder binder = new QueryStringBinder();
        String qs = binder.toQueryString(data);
        System.out.println(qs);
        assertTrue(qs.contains("key1=value%201"));
    }
	
	@Test
	public void test_toHashMap() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Dog dog = new Dog();
		QueryStringBinder binder = new QueryStringBinder();
		assertTrue(binder.toHashMap(dog, null).containsKey("bloodColor"));
		assertTrue(binder.toHashMap(dog, null).containsKey("legs_num"));
	}
	@Test
	public void test_constructBinding() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, InstantiationException {
		Dog dog = new Dog();
		QueryStringBinder binder = new QueryStringBinder();
		QueryStringBinder.Binding binding = binder.constructBinding(dog);
		assertEquals(2, binding._cachedParses.size());
		assertTrue(binding._cachedParses.containsKey("bloodColor"));
		assertTrue(binding._cachedParses.containsKey("legs_num"));
		assertEquals(Dog.class, binding.clazz);
	}
	
	@Test
	public void test_bind() throws Exception {
		Dog dog = new Dog();
		assertEquals(4, dog.legsNum);
		assertEquals("red", dog.bloodColor);
		QueryStringBinder binder = new QueryStringBinder();
		binder.bind("legs_num=3&bloodColor=bl%20ue", dog);
		assertEquals(3, dog.legsNum);
		assertEquals("bl ue", dog.bloodColor);
	}
	// @Mock
	InMemoryMultipartEntryHandler mockInMemoryMultipartEntryHandler = new InMemoryMultipartEntryHandler();
	
	
	@Mock
	BytesReadHandler mockBytesReadHandlerForText;
	@Mock
	BytesReadHandler mockBytesReadHandlerForFile;
	@Mock
	ContentDisposition mockContentDisposition;
	
	@SuppressWarnings("serial")
	@Test
	public void testBindWithMultipartFile() throws Exception {
		DemoRequest req = new DemoRequest();
//		when(mockInMemoryMultipartEntryHandler.getMap()).thenReturn(
//				new HashMap<String, String>() {
//					{
//						this.put("owner", "user1");
//					}
//				});
		Field multipartEntriesField = FieldUtils.getDeclaredField(InMemoryMultipartEntryHandler.class, "multipartEntries", true);
		
		// multipartEntriesField.setAccessible(true);
		ConcurrentHashMap<String, BytesReadHandler> multipartEntries = (ConcurrentHashMap<String, BytesReadHandler>) multipartEntriesField.get(mockInMemoryMultipartEntryHandler);
		
		when(mockBytesReadHandlerForText.getData()).thenReturn("user1".getBytes());
		multipartEntries.put("owner", mockBytesReadHandlerForText);
		
		
		when(mockContentDisposition.getDispositionParam("filename")).thenReturn("file1");
		when(mockContentDisposition.getDispositionParams()).thenReturn(
				new HashMap<String, String>() {
					{this.put("filename", "file1");}
				}.keySet()
		);
		
		when(mockBytesReadHandlerForFile.getContentDisposition()).thenReturn(mockContentDisposition);
		when(mockBytesReadHandlerForFile.getData()).thenReturn("Content of file1.".getBytes());
		multipartEntries.put("qualification", mockBytesReadHandlerForFile);
		
		
		QueryStringBinder binder = new QueryStringBinder();
		binder.bind(mockInMemoryMultipartEntryHandler, req);
		assertEquals("user1", req.owner);
		assertEquals("file1", req.qualification.getFilename());
		assertEquals("Content of file1.", new String(req.qualification.getBytes()));
		return;
	}
	
	@Test
	public void testBind() throws Exception {
		QueryStringBinder binder = new QueryStringBinder();
		Person person = new Person();
		binder.bind("name=john&age=30&isVIP=true&height=1.80&tier=III&register_time=2019-07-01 00:01:01&gender=Male", person);
		assertEquals(Gender.Male, person.gender);
		assertEquals("男", person.gender.chinese);
		String qstring = binder.toQueryString(person, null);
		System.out.println("qstring: " + qstring);
		assertTrue(qstring.contains("gender=Male"));
	}
}
