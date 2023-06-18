package at.htlleonding.mill.repositories;

import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class SqlRunner {
    private static final String SCRIPT_PROPERTIES_PATH = "sql/script-files.properties";

    public static void runScript(SqlScript sqlScript) {

        try {
            Properties scriptProperties = new Properties();
            scriptProperties.load(new FileInputStream(SCRIPT_PROPERTIES_PATH));

            DataSource dataSource = Database.getDataSource();
            Connection conn = dataSource.getConnection();
            System.out.println("Connection established for " + sqlScript.name() + "......");
            ScriptRunner sr = new ScriptRunner(conn);

            String script = scriptProperties.getProperty(sqlScript.name().toLowerCase());
            sr.runScript(new BufferedReader(new FileReader(script)));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}