package ru.treshchilin.training.h2jdbcsample.model;

import java.util.Map;

public class TablePkDescription {
	private String tableName;
	private Map<String, String> tablePkAndType;
	
	public TablePkDescription(String tableName, Map<String, String> tablePk) {
		this.tableName = tableName;
		this.tablePkAndType = tablePk;
	}
	
	public String getTableName() {
		return tableName;
	}

	public Map<String, String> getTablePkAndType() {
		return tablePkAndType;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder= new StringBuilder();
		tablePkAndType.forEach((pk, type) -> stringBuilder.append(tableName + ", " + pk + ", " + type + "\n"));
		return stringBuilder.toString();
	}
}
