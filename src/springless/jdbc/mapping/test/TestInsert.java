package springless.jdbc.mapping.test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import springless.jdbc.mapping.InsertStatementGenerator;


public class TestInsert {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Connection dbconn = DBUtils.getHikariConnection();
		dbconn.setAutoCommit(false);
		InsertStatementGenerator generator = new InsertStatementGenerator();
		
//		A a = new A();
//		a.c2 = 4000;
//		a.c3 = "haha123";
//		PreparedStatement insertStmt = generator.prepareInsertStatement(dbconn, a, Statement.RETURN_GENERATED_KEYS);
//		insertStmt.execute();
//		dbconn.commit();
		

		A a2 = new A();
		a2.c2 = 4300;
		a2.c3 = "haha123fgh";
		PreparedStatement insertStmt2 = generator.prepareInsertStatement(dbconn, a2, Statement.RETURN_GENERATED_KEYS);
		insertStmt2.execute();
		dbconn.commit();
		
		
		return;
	}
}
