package gubo.db.ddl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来向数据库执行ddl。
 * 
 **/
public class DdlImporter {
	private final static Logger logger = LoggerFactory.getLogger(DdlImporter.class);

	private void dropSchema(Connection dbconn, String schemaName) throws SQLException {
		PreparedStatement ps = dbconn.prepareStatement("drop database " + schemaName);
		ps.execute();
	}

	private void createSchema(Connection dbconn, String schemaName) throws SQLException {
		PreparedStatement ps = dbconn.prepareStatement("create database " + schemaName);
		ps.execute();
	}

	List<String> loadDdlList(String sqlFile) throws IOException {
		List<String> ret = new LinkedList<String>();
		BufferedReader br = new BufferedReader(new FileReader(sqlFile)); 
		String st;
		// 当前的ddl。
		StringBuilder sb = new StringBuilder();
		while ((st = br.readLine()) != null) {
			st = st.trim();
			if (st.length() == 0) {
				continue;
			}
			sb.append(st);
			if (st.endsWith(";")) {
				// 一句ddl的结尾。
				ret.add(sb.toString());
				sb = new StringBuilder();
			}
			
		}
		return ret;
	}

	void run(Connection dbconn, List<String> ddlList) throws SQLException {
		for (String ddl : ddlList) {
			PreparedStatement ps = dbconn.prepareStatement(ddl);
			ps.execute();
		}
	}

	/**
	 * 
	 * @param recreateSchema
	 *            在运行sql file 之前，先drop schema 再 create schema。
	 * @throws SQLException
	 * @throws IOException 
	 **/
	public void run(DataSource ds, String schemaName, boolean recreateSchema, String... sqlFiles) throws SQLException, IOException {

		try (Connection dbconn = ds.getConnection()) {
			dbconn.setAutoCommit(false);
			if (recreateSchema) {
				try {
					dropSchema(dbconn, schemaName);
				} catch (SQLException ex) {
					logger.error("Erro when dropping schema error:", ex);
				}
				createSchema(dbconn, schemaName);

				dbconn.setCatalog(schemaName);
			}
			for (String sqlFile : sqlFiles) {
				List<String> ddlList = loadDdlList(sqlFile);
				this.run(dbconn, ddlList);
			}
			dbconn.commit();
		}

	}
}
