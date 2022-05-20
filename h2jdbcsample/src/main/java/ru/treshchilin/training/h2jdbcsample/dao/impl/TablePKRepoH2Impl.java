package ru.treshchilin.training.h2jdbcsample.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ru.treshchilin.training.h2jdbcsample.dao.TablePKRepository;
import ru.treshchilin.training.h2jdbcsample.model.TablePkDescription;

public class TablePKRepoH2Impl implements TablePKRepository {
	private final String DEFAULT_JDBC_DRIVER = "org.h2.Driver";

	private final String SELECT_ALL_FROM_TABLE_LIST = "SELECT * FROM TABLE_LIST";
	private final String SELECT_ALL_FROM_TABLE_COLS = "SELECT COLUMN_TYPE FROM TABLE_COLS WHERE TABLE_NAME=? AND lower(COLUMN_NAME)=lower(?)";
	
	private final String TABLE_NAME = "TABLE_NAME";
	private final String COLUMN_TYPE = "COLUMN_TYPE";
	private final String PK = "PK";
	private final String PK_SEP = ",";

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
	public List<TablePkDescription> findAll() throws SQLException {
		Statement statement = connection.createStatement();
		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_TABLE_COLS);
		List<TablePkDescription> result = new ArrayList<>();
		
		ResultSet tableListRs = statement.executeQuery(SELECT_ALL_FROM_TABLE_LIST);

		while (tableListRs.next()) {
			String tableName = tableListRs.getString(TABLE_NAME);
			Set<String> pKeys = Arrays.stream(tableListRs.getString(PK).split(PK_SEP)).map(String::strip)
					.collect(Collectors.toSet());

			preparedStatement.setString(1, tableName);
			Map<String, String> pkToType = new HashMap<>();

			for (String key : pKeys) {
				preparedStatement.setString(2, key);
				ResultSet pkTypeRs = preparedStatement.executeQuery();

				if (pkTypeRs.next()) {
					String pkType = pkTypeRs.getString(COLUMN_TYPE);
					pkToType.put(key, pkType);
				} else {
					pkToType.put(key, "");
				}
				
				pkTypeRs.close();
			}
			
			result.add(new TablePkDescription(tableName, pkToType));
		}
		
		tableListRs.close();
		
		statement.close();
		preparedStatement.close();
		
		return result;
	}
}
