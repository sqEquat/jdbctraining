package ru.treshchilin.training.h2jdbcsample.dao;

import java.sql.SQLException;
import java.util.List;

import ru.treshchilin.training.h2jdbcsample.model.TablePkDescription;

public interface TablePKRepository {	
	void connect(String url, String username, String password) throws SQLException;
	void close() throws SQLException;
	
	List<TablePkDescription> findAll() throws SQLException;
}
