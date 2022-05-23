package ru.treshchilin.training.h2jdbcsample.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.treshchilin.training.h2jdbcsample.dao.TablePKRepository;
import ru.treshchilin.training.h2jdbcsample.model.TablePkDescription;

public class TablePKRepoH2Impl implements TablePKRepository {
	private static final String DEFAULT_JDBC_DRIVER = "org.h2.Driver";

	private static final String SELECT_ALL_FROM_TABLE_LIST = "SELECT * FROM TABLE_LIST";
	private static final String SELECT_ALL_FROM_TABLE_COLS = "SELECT * FROM TABLE_COLS";
	
	private static final String TABLE_NAME = "TABLE_NAME";
	private static final String COLUMN_TYPE = "COLUMN_TYPE";
	private static final String COLUMN_NAME = "COLUMN_NAME";
	private static final String PK = "PK";
	private static final String PK_SEP = ",";

	private Connection connection;

	public TablePKRepoH2Impl() throws ClassNotFoundException {
		Class.forName(DEFAULT_JDBC_DRIVER);
	}

	@Override
	public void connect(String url, String username, String password) throws SQLException {
		connection = DriverManager.getConnection(url, username, password);
	}

	@Override
	public void close() throws SQLException {
		connection.close();
	}

	@Override
	public List<TablePkDescription> findAll() throws SQLException{
		Statement statement = connection.createStatement();
		List<TablePkDescription> result = new ArrayList<>();
		
		ResultSet tablePkType = statement.executeQuery(SELECT_ALL_FROM_TABLE_COLS);
		Map<String, String> tablePkToType = new HashMap<>();
		
		while (tablePkType.next()) {
			tablePkToType.put(
					tablePkType.getString(TABLE_NAME) + ":" + tablePkType.getString(COLUMN_NAME).toLowerCase(),
					tablePkType.getString(COLUMN_TYPE));
		}
		
		ResultSet tableListRs = statement.executeQuery(SELECT_ALL_FROM_TABLE_LIST);
		
		while (tableListRs.next()) {
			String tableName = tableListRs.getString(TABLE_NAME);
			Map<String, String> pkToType = new HashMap<>();
			Arrays.stream(tableListRs.getString(PK).split(PK_SEP))
					.forEach(key -> pkToType.put(key, tablePkToType.get(tableName+":"+key.toLowerCase())));
			
			result.add(new TablePkDescription(tableName, pkToType));
		}
			
		return result;
	}
}
