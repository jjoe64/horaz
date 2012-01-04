package com.horaz.client.model;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.horaz.client.model.events.TableCreatedEvent;
import com.horaz.client.model.events.TableCreatedListener;

public class SQLiteDataStore<T extends BaseModel> extends DataStore<T> implements AsynchronousDataStore<T> {
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
				added(newModel);
			}
		});
	}

	public HandlerRegistration addTableCreatedListener(TableCreatedListener handler) {
		Type<TableCreatedListener> type = TableCreatedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	@Override
	public void find(
			Filter filter,
			StatementCallback<JavaScriptObject> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void find(
			String field,
			Object value,
			StatementCallback<JavaScriptObject> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findAll(final Filter filter, final StatementCallback<JavaScriptObject> callback) {
		if (!ready) throw new IllegalStateException("Table was not initialized, yet.");

		database.readTransaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				throw new IllegalStateException(error.getMessage());
			}

			@Override
			public void onTransactionStart(SQLTransaction transaction) {
				transaction.executeSql("SELECT * FROM "+table+" WHERE "+filter.getSQLStatement(), filter.getValues(), callback);
			}

			@Override
			public void onTransactionSuccess() {
			}
		});

	}

	@Override
	public void findAll(
			String field,
			Object value,
			StatementCallback<JavaScriptObject> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(final int id, final StatementCallback<JavaScriptObject> callback) {
		if (!ready) throw new IllegalStateException("Table was not initialized, yet.");

		database.readTransaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				throw new IllegalStateException(error.getMessage());
			}

			@Override
			public void onTransactionStart(SQLTransaction transaction) {
				transaction.executeSql("SELECT * FROM "+table+" WHERE modelId=? LIMIT 1", new Object[] {id}, callback);
			}

			@Override
			public void onTransactionSuccess() {
			}
		});
	}

	public Database getDatabase() {
		return database;
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

	@Override
	public void remove(T model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(T saveModel) {
		// TODO Auto-generated method stub

	}
}
