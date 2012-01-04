package com.horaz.client.model;

import java.util.Date;

import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.horaz.client.model.SQLiteDataStore.SQLiteColumnDef;
import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelAddedListener;
import com.horaz.client.model.events.TableCreatedEvent;
import com.horaz.client.model.events.TableCreatedListener;

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
		public final native String getName() /*-{ return this.name; }-*/;
	}

	private boolean eventTableCreatedCatched;

	@Override
	public String getModuleName() {
		return "com.horaz.Horaz";
	}

	public void testAdd() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new SQLiteDataStore<TestModel>("testadd", "1", 1024*1024);
		ds.initTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});

		new Timer() {
			@Override
			public void run() {
				// create model
				final TestModel mdl = new TestModel();
				mdl.setField("name", "foo");

				// register hook
				ds.addModelAddedListener(new ModelAddedListener<SQLiteDataStoreTest.TestModel>() {
					@Override
					public void onModelAdded(ModelAddedEvent<TestModel> event) {
						// find model in db
						ds.getDatabase().readTransaction(new TransactionCallback() {
							@Override
							public void onTransactionFailure(SQLError error) {
								fail();
							}
							@Override
							public void onTransactionStart(SQLTransaction transaction) {
								transaction.executeSql("SELECT * FROM testTbl WHERE name='foo'", null, new StatementCallback<JavaScriptObject>() {
									@Override
									public boolean onFailure(
											SQLTransaction transaction, SQLError error) {
										fail(error.getMessage());
										return false;
									}
									@Override
									public void onSuccess(SQLTransaction transaction,
											SQLResultSet<JavaScriptObject> resultSet) {
										assertEquals(1, resultSet.getRows().getLength());
										TestModelJS mdlDB = (TestModelJS) resultSet.getRows().getItem(0);
										assertEquals(mdl.getField("name"), mdlDB.getName());
										finishTest();
									}
								});
							}
							@Override
							public void onTransactionSuccess() {
							}
						});
					}
				});

				// insert
				ds.add(mdl);
			}
		}.schedule(200);
		delayTestFinish(500);
	}

	public void testFindAll() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new SQLiteDataStore<TestModel>("testfindall", "1", 1024*1024);
		ds.initTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});

		// insert models
		new Timer() {
			@Override
			public void run() {
				// create model
				TestModel mdl = new TestModel();
				mdl.setField("name", "foo");
				// insert
				ds.add(mdl);

				mdl = new TestModel();
				mdl.setField("name", "bar");
				// insert
				ds.add(mdl);
			}
		}.schedule(200);

		// find only "bar"
		new Timer() {
			@Override
			public void run() {
				Filter filter = new Filter().whereEquals("name", "bar");
				ds.findAll(filter, new StatementCallback<JavaScriptObject>() {
					@Override
					public boolean onFailure(SQLTransaction transaction, SQLError error) {
						fail();
						return false;
					}

					@Override
					public void onSuccess(SQLTransaction transaction, SQLResultSet<JavaScriptObject> resultSet) {
						assertEquals(1, resultSet.getRows().getLength());
						TestModelJS mdlDB = (TestModelJS) resultSet.getRows().getItem(0);
						assertEquals("bar", mdlDB.getName());
						finishTest();
					}
				});
			}
		}.schedule(500);

		delayTestFinish(800);
	}

	public void testGet() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new SQLiteDataStore<TestModel>("testget", "1", 1024*1024);
		ds.initTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});

		// insert model
		new Timer() {
			@Override
			public void run() {
				// create model
				final TestModel mdl = new TestModel();
				mdl.setField("name", "foo");

				// register hook
				ds.addModelAddedListener(new ModelAddedListener<SQLiteDataStoreTest.TestModel>() {
					@Override
					public void onModelAdded(ModelAddedEvent<TestModel> event) {
						// get model
						ds.get(event.getModel().getModelId(), new StatementCallback<JavaScriptObject>() {
							@Override
							public boolean onFailure(SQLTransaction transaction, SQLError error) {
								fail();
								return false;
							}

							@Override
							public void onSuccess(SQLTransaction transaction, SQLResultSet<JavaScriptObject> resultSet) {
								assertEquals(1, resultSet.getRows().getLength());
								TestModelJS mdlDB = (TestModelJS) resultSet.getRows().getItem(0);
								assertEquals(mdl.getField("name"), mdlDB.getName());
								finishTest();
							}
						});
					}
				});

				// insert
				ds.add(mdl);
			}
		}.schedule(200);
		delayTestFinish(500);
	}

	public void testInitTable() {
		final SQLiteDataStore<TestModel> ds = new SQLiteDataStore<TestModel>("testInit"+new Date().getTime(), "1", 1024*1024);
		// first time, table-created-event must occur
		eventTableCreatedCatched = false;

		ds.addTableCreatedListener(new TableCreatedListener() {
			@Override
			public void onTableCreated(TableCreatedEvent event) {
				eventTableCreatedCatched = true;
			}
		});
		ds.initTable("testTbl", new SQLiteColumnDef[] {
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
								assertTrue(eventTableCreatedCatched);
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

		// second time, no table-created event
		eventTableCreatedCatched = false;
		ds.initTable("testTbl", new SQLiteColumnDef[] {
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
								assertFalse(eventTableCreatedCatched);
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
