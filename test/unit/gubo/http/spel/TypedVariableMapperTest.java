package gubo.http.spel;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class TypedVariableMapperTest {

	TypedVariableMapper testee = new TypedVariableMapper();
	@Mock
	Request mockRequest;
	
	@Test
	public void testMap() {
		
		HashSet<String> pn = new HashSet<String>();
		pn.add("foo__value");
		pn.add("foo__type");
		pn.add("name__value");
		pn.add("a");
		pn.add("b");
		when(mockRequest.getParameterNames()).thenReturn(pn);
		when(mockRequest.getParameter("foo__value")).thenReturn("1000.00");
		when(mockRequest.getParameter("foo__type")).thenReturn("BigDecimal");
		when(mockRequest.getParameter("name__value")).thenReturn("abc");
		//when(mockRequest.getParameter("a")).thenReturn("1000");
		//when(mockRequest.getParameter("b")).thenReturn("abc");
		
		Map<String, Object> variables = testee.map(mockRequest);
		
		assertEquals(2, variables.size());
		assertEquals("abc", variables.get("name"));
		assertEquals(new BigDecimal("1000.00"), variables.get("foo"));
	}
}
