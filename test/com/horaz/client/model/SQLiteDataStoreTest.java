package com.horaz.client.model;

import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.horaz.client.model.SQLiteDataStore.SQLiteColumnDef;

public class SQLiteDataStoreTest extends GWTTestCase {
	class TestModel extends BaseModel {
		@Override
		protected ModelField[] getStructure() {
			return new ModelField[] {
					new ModelField("name")
			};
		}
	}
	static class TestModelJS extends JavaScriptObject {
		protected TestModelJS() {
		}
		public final native String getName() /*-{ return this.tbl_name; }-*/;
	}

	@Override
	public String getModuleName() {
		return "com.horaz.Horaz";
	}

	public void testCreateTable() {
		final SQLiteDataStore<TestModel> ds = new SQLiteDataStore<TestModel>("test", "1", 1024*1024);
		ds.createTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});
		new Timer() {
			@Override
			public void run() {
				// check table
				ds.getDatabase().readTransaction(new TransactionCallback() {
					@Override
					public void onTransactionFailure(SQLError error) {
						fail();
					}
					@Override
					public void onTransactionStart(SQLTransaction transaction) {
						transaction.executeSql("SELECT * FROM testTbl", null, new StatementCallback<JavaScriptObject>() {
							@Override
							public boolean onFailure(
									SQLTransaction transaction, SQLError error) {
								fail(error.getMessage());
								return false;
							}
							@Override
							public void onSuccess(SQLTransaction transaction,
									SQLResultSet<JavaScriptObject> resultSet) {
								finishTest();
							}
						});
					}
					@Override
					public void onTransactionSuccess() {
					}
				});
			}
		}.schedule(200);
		delayTestFinish(300);
	}
}
