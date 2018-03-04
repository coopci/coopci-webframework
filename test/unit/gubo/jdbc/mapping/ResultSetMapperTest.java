package gubo.jdbc.mapping;

import static org.junit.Assert.assertEquals;

import javax.persistence.Column;

import org.junit.Test;

public class ResultSetMapperTest {

	class ToClass {

		@Column()
		public String name;

		@Column()
		public int age;

		@Column()
		public boolean isVIP;

	}

	class FromClass {

		@Column()
		public String title;

		@Column()
		public String name;

		@Column()
		public int age;

	}

	@Test
	public void testCopy() throws Exception {
		ToClass to = new ToClass();
		to.name = "aaa";
		to.age = 10;
		FromClass from = new FromClass();
		from.age = 20;
		from.name = "bbb";
		ResultSetMapper.copy(to, from);

		assertEquals(20, to.age);
		assertEquals("bbb", to.name);
	}
}
