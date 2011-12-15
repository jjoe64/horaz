package com.horaz.client.model;

import java.util.List;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.horaz.client.model.events.TableCreatedEvent;
import com.horaz.client.model.events.TableCreatedListener;

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
	private String table;
	private SQLiteColumnDef[] tableColumns;
	private boolean ready;

	public SQLiteDataStore(String databaseName, String version, int maxSizeBytes) {
		if (!Database.isSupported()) {
			throw new IllegalStateException("HTML5 Database is not supported by this browser.");
		}
		database = Database.openDatabase(databaseName, version, databaseName, maxSizeBytes);
	}

	@Override
	public void add(final T newModel) {
		if (!ready) throw new IllegalStateException("Table was not initialized, yet.");

		database.transaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				throw new IllegalStateException(error.getMessage());
			}

			@Override
			public void onTransactionStart(SQLTransaction transaction) {
				String cols = "";
				String values = "";
				Object[] args = new Object[tableColumns.length];
				int i=0;

				for (SQLiteColumnDef colDef : tableColumns) {
					// TODO types
					cols += "," + colDef.columnName;
					values += ",?";
					args[i] = newModel.getField(colDef.columnName);
					i++;
				}
				transaction.executeSql("INSERT INTO "+table+" ("+cols.substring(1)+") VALUES ("+values.substring(1)+")", args);
			}

			@Override
			public void onTransactionSuccess() {
				SQLiteDataStore.super.add(newModel);
			}
		});
	}

	public HandlerRegistration addTableCreatedListener(TableCreatedListener handler) {
		Type<TableCreatedListener> type = TableCreatedEvent.getType();
		return handlerManager.addHandler(type, handler);
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

	public void initTable(String table, SQLiteColumnDef[] columns) {
		this.table = table;
		this.tableColumns = columns;
		database.transaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				if (error.getMessage().contains("already exists")) {
					// table already exists
					//TODO fire event "ready"
					ready = true;
				} else {
					throw new IllegalStateException(error.getMessage());
				}
			}
			@Override
			public void onTransactionStart(SQLTransaction tx) {
				String columnsStr = "modelId INTEGER PRIMARY KEY";
				for (SQLiteColumnDef col : tableColumns) {
					columnsStr += "," + col.toSQL();
				}
				tx.executeSql("CREATE TABLE "+SQLiteDataStore.this.table+" ("
					+ columnsStr +")", null);
			}
			@Override
			public void onTransactionSuccess() {
				// fire table created event
				ready = true;
				fireEvent(new TableCreatedEvent());
			}
		});
	}
}
