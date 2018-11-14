package gubo.jdbc.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 用来从数据库里的表定义生成对应的java类，带上了各种annotation。
public class TableClassGenerator {
	public static Logger logger = LoggerFactory
			.getLogger(TableClassGenerator.class);
	private static String warningComment = "//// This file is generated with "
			+ TableClassGenerator.class + ".";

	private String boundaryBegin = "====User code begins here====";
	private String boundaryEnd = "====User code ends here====";

	String extractUserCode(InputStream ins) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

		String line = null;
		boolean inUserCode = false;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			if (line.indexOf(boundaryBegin) > 0) {
				inUserCode = true;
			} else if (line.indexOf(boundaryEnd) > 0) {
				inUserCode = false;
			} else if (inUserCode) {
				sb.append(line);
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	String embedUserCode(InputStream ins, String newSource) throws IOException {
		String userCode = this.extractUserCode(ins);
		StringBuilder sb = new StringBuilder();
		for (String line : newSource.split("\n")) {
			line = line.replace("\r", "");
			line = line.replace("\n", "");
			sb.append(line);
			sb.append("\r\n");
			if (line.indexOf(boundaryBegin) > 0) {
				sb.append(userCode);
			}
		}
		return sb.toString();
	}

	public void generateAndWrite(DataSource ds, String schemaName,
			String tableName, String srcRoot, String packageName,
			String className) throws SQLException, IOException {

		String source = this.generateSource(ds, schemaName, tableName,
				packageName, className);

		// String packagePath = packageName.replace(".", "/");

		Path packagePath = Paths.get(srcRoot, packageName.replace(".", "/"));
		packagePath.toFile().mkdirs();
		Path filePath = Paths.get(packagePath.toString(), className + ".java");
		File file = filePath.toFile();
		if (!file.exists()) {
			file.createNewFile();
		}

		FileInputStream ins = new FileInputStream(file);
		source = embedUserCode(ins, source);
		logger.info("Writing to " + file.toString());
		OutputStream outs = new FileOutputStream(file);
		outs.write(source.getBytes());
		outs.close();
	}

	public String getBoundaryBegin() {
		return boundaryBegin;
	}

	public void setBoundaryBegin(String boundaryBegin) {
		this.boundaryBegin = boundaryBegin;
	}

	public String getBoundaryEnd() {
		return boundaryEnd;
	}

	public void setBoundaryEnd(String boundaryEnd) {
		this.boundaryEnd = boundaryEnd;
	}

	public String generateSource(DataSource ds, String schemaName,
			String tableName, String packageName, String className)
			throws SQLException {

		try (Connection dbconn = ds.getConnection()) {
			DatabaseMetaData meta = dbconn.getMetaData();
			String ret = generateSource(meta, schemaName, tableName,
					packageName, className);
			return ret;
		}
	}

	private void appendWarningComments(StringBuilder sb) {
		sb.append(warningComment);
		sb.append("\r\n");
	}

	private void appendImports(StringBuilder sb) {
		sb.append("import gubo.db.ISimplePoJo;\r\n"
				+ "import gubo.db.SimplePoJoDAO;\r\n"
				+ "import gubo.http.querystring.QueryStringField;\r\n"
				+ "import gubo.jdbc.mapping.InsertStatementGenerator;\r\n"
				+ "import gubo.jdbc.mapping.ResultSetMapper;\r\n"
				+ "import gubo.jdbc.mapping.UpdateStatementGenerator;\r\n"
				+ "import java.sql.Connection;\r\n"
				+ "import java.sql.PreparedStatement;\r\n"
				+ "import java.sql.ResultSet;\r\n"
				+ "import java.sql.SQLException;\r\n"
				+ "import java.sql.Statement;\r\n"
				+ "import java.sql.Timestamp;\r\n"
				+ "import java.util.LinkedList;\r\n"
				+ "import java.util.List;\r\n" + "\r\n"
				+ "import javax.persistence.Column;\r\n"
				+ "import javax.persistence.Entity;\r\n"
				+ "import javax.persistence.Table;\r\n"
				+ "import javax.persistence.GeneratedValue;\r\n"
				+ "import javax.persistence.Id;\r\n"
				+ "import java.math.BigDecimal;\r\n"
				+ "import javax.sql.DataSource;");
	}

	private void appenBoundaries(StringBuilder sb) {
		sb.append("\r\n");
		sb.append("// " + boundaryBegin);
		sb.append("\r\n");
		sb.append("// " + boundaryEnd);
		sb.append("\r\n");
	}

	private String indent = "\t";

	private String dataTypeToJajaType(int dataType) {

		switch (dataType) {
		case java.sql.Types.INTEGER:
			return "Integer";
		case java.sql.Types.BIGINT:
			return "Long";
		case java.sql.Types.FLOAT:
			return "Double";
		case java.sql.Types.DECIMAL:
			return "BigDecimal";
		case java.sql.Types.VARCHAR:
			return "String";
		case java.sql.Types.CHAR:
			return "String";
		case java.sql.Types.TIMESTAMP:
			return "Timestamp";
		case java.sql.Types.TINYINT:
			return "Boolean";
		case java.sql.Types.BIT:
			return "Boolean";
		case java.sql.Types.LONGVARCHAR:
			return "String";
		default:
			System.out.println("Unknown column type: " + dataType);
			return "Unknown";
		}
	}

	private void appendDefaultValue(StringBuilder sb, int dataType,
			String defaultValue) {
		switch (dataType) {
		case java.sql.Types.INTEGER:
			sb.append(defaultValue);
			return;
		case java.sql.Types.BIGINT:
			sb.append(defaultValue);
			return;
		case java.sql.Types.FLOAT:
			sb.append(defaultValue);
			return;
		case java.sql.Types.DECIMAL:
			sb.append("new BigDecimal(\"");
			sb.append(defaultValue);
			sb.append("\")");
			return;
		case java.sql.Types.VARCHAR:
		case java.sql.Types.LONGVARCHAR:
			if (defaultValue == null) {
				sb.append("null");
			} else {
				sb.append('"');
				sb.append(defaultValue);
				sb.append('"');
			}
			return;
		case java.sql.Types.CHAR:
			if (defaultValue == null) {
				sb.append("null");
			} else {
				sb.append('"');
				sb.append(defaultValue);
				sb.append('"');
			}
			return;
		case java.sql.Types.TIMESTAMP:
			if (defaultValue == null) {
				sb.append("null");
			} else if (defaultValue.equals("current_timestamp()")) {
				sb.append("new Timestamp(System.currentTimeMillis())");
			} else {

				try {
					Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
							.parse(defaultValue);
					long v = d.getTime();
					sb.append("new Timestamp(" + v + "L)");

					// if (v < 0) {
					// sb.append("new Timestamp(0)");
					// } else {
					// sb.append("new Timestamp(" + v + "L)");
					// }

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return;
		case java.sql.Types.TINYINT:
			return;
		case java.sql.Types.BIT:
			if ("1".equals(defaultValue)) {
				sb.append("true");
			} else if ("0".equals(defaultValue)) {
				sb.append("false");
			}
			return;
		default:
			if (defaultValue == null) {
				sb.append("null");
			}
			return;
		}
	}

	private String generateSource(DatabaseMetaData meta, String schemaName,
			String tableName, String packageName, String className)
			throws SQLException {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + packageName + ";\r\n\r\n");
		appendWarningComments(sb);
		appendImports(sb);

		sb.append("\r\n\r\n");

		sb.append("@Entity(name = \"" + tableName + "\")\r\n");
		sb.append("@Table(name = \"" + tableName + "\")\r\n");

		sb.append("public class " + className + " {\r\n");

		sb.append(this.indent
				+ "public static SimplePoJoDAO dao = new SimplePoJoDAO("
				+ className + ".class);");

		sb.append(";\r\n");

		ResultSet rs = meta.getColumns(meta.getConnection().getCatalog(),
				schemaName, tableName, "%");

		while (rs.next()) {
			String colName = rs.getString("COLUMN_NAME");
			String isAutoInc = rs.getString("IS_AUTOINCREMENT");

			String comment = rs.getString("REMARKS");
			int dataType = rs.getInt("DATA_TYPE");
			String defaultValue = rs.getString("COLUMN_DEF");

			sb.append("\r\n");
			if (comment != null && !comment.trim().isEmpty()) {
				sb.append(this.indent + "/*\r\n");
				sb.append(this.indent + comment);
				sb.append(this.indent + "\r\n");
				sb.append(this.indent + "*/\r\n");
			}
			if ("YES".equals(isAutoInc)) {
				sb.append(this.indent + "@Id()\r\n");
				sb.append(this.indent + "@GeneratedValue()\r\n");
			}

			sb.append(this.indent + "@Column(name = \"" + colName + "\")\r\n");
			sb.append(this.indent + "public ");
			sb.append(this.dataTypeToJajaType(dataType) + " ");

			sb.append(colName);
			sb.append(" = ");
			appendDefaultValue(sb, dataType, defaultValue);

			sb.append(";\r\n");

		}
		appenBoundaries(sb);
		sb.append("}\r\n");
		return sb.toString();

	}
}
