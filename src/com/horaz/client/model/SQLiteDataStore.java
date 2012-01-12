package com.horaz.client.model;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.horaz.client.model.events.TableCreatedEvent;
import com.horaz.client.model.events.TableCreatedListener;

public abstract class SQLiteDataStore<T extends BaseModel> extends DataStore<T> implements AsynchronousDataStore<T> {
	public static class SQLiteColumnDef {
		public enum Type {
			TEXT, NUMERIC, INTEGER, REAL, _MODEL_ID
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
			return columnName+" "+(Type._MODEL_ID == typeName ? "INTEGER PRIMARY KEY" : typeName.name());
		}
	}

	private final Database database;
	private String table;
	private SQLiteColumnDef[] tableColumns;
	private int lastModelId; // TODO long instead of int
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

		// set model id
		newModel.setField("modelId", ++lastModelId);

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
	public void findAll(final Filter filter, final FindCallback<T> callback) {
		if (!ready) throw new IllegalStateException("Table was not initialized, yet.");

		database.readTransaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				throw new IllegalStateException(error.getMessage());
			}

			@Override
			public void onTransactionStart(SQLTransaction transaction) {
				transaction.executeSql("SELECT * FROM "+table+" WHERE "+filter.getSQLStatement(), filter.getValues(), new StatementCallback<JavaScriptObject>() {
					@Override
					public boolean onFailure(SQLTransaction transaction, SQLError error) {
						throw new IllegalStateException(error.getMessage());
					}

					@Override
					public void onSuccess(SQLTransaction transaction, SQLResultSet<JavaScriptObject> resultSet) {
						callback.onSuccess(new ModelsCollection<T>(SQLiteDataStore.this, resultSet));
					}
				});
			}

			@Override
			public void onTransactionSuccess() {
			}
		});

	}

	@Override
	public void get(final int id, final FindCallback<T> callback) {
		if (!ready) throw new IllegalStateException("Table was not initialized, yet.");

		database.readTransaction(new TransactionCallback() {
			@Override
			public void onTransactionFailure(SQLError error) {
				throw new IllegalStateException(error.getMessage());
			}

			@Override
			public void onTransactionStart(SQLTransaction transaction) {
				transaction.executeSql("SELECT * FROM "+table+" WHERE modelId=? LIMIT 1", new Object[] {id}, new StatementCallback<JavaScriptObject>() {
					@Override
					public boolean onFailure(SQLTransaction transaction, SQLError error) {
						throw new IllegalStateException(error.getMessage());
					}

					@Override
					public void onSuccess(SQLTransaction transaction, SQLResultSet<JavaScriptObject> resultSet) {
						callback.onSuccess(new ModelsCollection<T>(SQLiteDataStore.this, resultSet));
					}
				});
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
		// add modelId col
		this.tableColumns = new SQLiteColumnDef[columns.length +1];
		this.tableColumns[0] = new SQLiteColumnDef("modelId", SQLiteColumnDef.Type._MODEL_ID);
		for (int i=0; i<columns.length; i++) {
			this.tableColumns[i+1] = columns[i];
		}
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
				String columnsStr = "";
				for (SQLiteColumnDef col : tableColumns) {
					columnsStr += "," + col.toSQL();
				}
				tx.executeSql("CREATE TABLE "+SQLiteDataStore.this.table+" ("
					+ columnsStr.substring(1) +")", null);
			}
			@Override
			public void onTransactionSuccess() {
				// fire table created event
				lastModelId = 0;
				ready = true;
				fireEvent(new TableCreatedEvent());
			}
		});

		// TODO get last modelId
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
