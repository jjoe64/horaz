package com.horaz.client.model;

import java.util.List;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.TransactionCallback;

public class SQLiteDataStore<T extends BaseModel> extends DataStore<T> {
	public static class SQLiteColumnDef {
		public enum Type {
			TEXT, NUMERIC, INTEGER, REAL
		}

		private final String columnName;
		private final Type typeName;

		public SQLiteColumnDef(String columnName, Type typeName) {
			this.columnName = columnName;
			this.typeName = typeName;
		}

		public String getColumnName() {
			return columnName;
		}

		public Type getTypeName() {
			return typeName;
		}

		public String toSQL() {
			return columnName+" "+typeName.name();
		}
	}

	private final Database database;

	public SQLiteDataStore(String databaseName, String version, int maxSizeBytes) {
		if (!Database.isSupported()) {
			throw new IllegalStateException("HTML5 Database is not supported by this browser.");
		}
		database = Database.openDatabase(databaseName, version, databaseName, maxSizeBytes);
	}

	public void createTable(final String name, final SQLiteColumnDef[] columns) {
		database.transaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				// TODO handle error...
				throw new IllegalStateException(error.getMessage());
			}
			@Override
			public void onTransactionStart(SQLTransaction tx) {
				String columnsStr = "modelId INTEGER PRIMARY KEY";
				for (SQLiteColumnDef col : columns) {
					columnsStr += "," + col.toSQL();
				}
				tx.executeSql("CREATE TABLE IF NOT EXISTS "+name+" ("
					+ columnsStr +")", null);
			}
			@Override
			public void onTransactionSuccess() {
				// Proceed when successfully committed...
			}
		});

	}

	@Override
	public T get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Database getDatabase() {
		return database;
	}

	@Override
	public List<T> getModels() {
		// TODO Auto-generated method stub
		return null;
	}
}
