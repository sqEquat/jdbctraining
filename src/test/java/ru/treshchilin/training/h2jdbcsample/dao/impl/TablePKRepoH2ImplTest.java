package ru.treshchilin.training.h2jdbcsample.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.treshchilin.training.h2jdbcsample.model.TablePkDescription;

@ExtendWith(MockitoExtension.class)
class TablePKRepoH2ImplTest {
	
	private final String SELECT_ALL_FROM_TABLE_LIST = "SELECT * FROM TABLE_LIST";
	private final String SELECT_ALL_FROM_TABLE_COLS = "SELECT COLUMN_TYPE FROM TABLE_COLS WHERE TABLE_NAME=? AND lower(COLUMN_NAME)=lower(?)";
	
	private final String TABLE_NAME = "TABLE_NAME";
	private final String COLUMN_TYPE = "COLUMN_TYPE";
	private final String PK = "PK";

	@Mock
	Connection connection;
	
	@InjectMocks
	TablePKRepoH2Impl underTest;
	
	@Test
	void test() {
		assertThat(underTest).isNotNull();
	}
	
	@Test
	void close() throws Exception {
		underTest.close();
		
		verify(connection, atMostOnce()).close();
	}
	
	@Test
	void findAll() throws Exception {
//		Given
		ResultSet tableListRs = mock(ResultSet.class);
		when(tableListRs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		when(tableListRs.getString(TABLE_NAME)).thenReturn("users").thenReturn("accounts").thenReturn("not_a_table");
		when(tableListRs.getString(PK)).thenReturn("ID").thenReturn("account, account_id").thenReturn("");
		
		Statement tableListStatement = mock(Statement.class);
		when(tableListStatement.executeQuery(SELECT_ALL_FROM_TABLE_LIST)).thenReturn(tableListRs);
		
		when(connection.createStatement()).thenReturn(tableListStatement);
		
		ResultSet pkTypeRs = mock(ResultSet.class);
		when(pkTypeRs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		when(pkTypeRs.getString(COLUMN_TYPE)).thenReturn("INT").thenReturn("VARCHAR(32)").thenReturn("INT");
		
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(preparedStatement.executeQuery()).thenReturn(pkTypeRs);
		
		when(connection.prepareStatement(SELECT_ALL_FROM_TABLE_COLS)).thenReturn(preparedStatement);
		
//		When
		List<TablePkDescription> underTestReturns = underTest.findAll();
		
		
//		Then
		assertThat(underTestReturns.size()).isEqualTo(3);
		
		verify(tableListRs, atMostOnce()).close();
		verify(pkTypeRs, atMost(4)).close();
				
		verify(tableListStatement, atMostOnce()).close();
		verify(preparedStatement, atMostOnce()).close();
	}

}
