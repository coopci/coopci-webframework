package gubo.jdbc.mapping.test;

import gubo.jdbc.mapping.UpdateStatementGenerator;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;


public class TestUpdate {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Connection dbconn = DBUtils.getHikariConnection();
		dbconn.setAutoCommit(false);
		UpdateStatementGenerator generator = new UpdateStatementGenerator();
		
		A a = new A();
		a.id = 3;
		a.c2 = 4003;
		a.c3 = "haha123";
		a.cboolean = false;
		PreparedStatement stmt = generator.prepareUpdateStatement(dbconn, a, Statement.RETURN_GENERATED_KEYS);
		stmt.executeUpdate();
		dbconn.commit();
		
		return;
	}
}
