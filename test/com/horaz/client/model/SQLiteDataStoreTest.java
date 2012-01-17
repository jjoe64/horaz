package com.horaz.client.model;

import java.util.Date;
import java.util.Iterator;

import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.horaz.client.model.AsynchronousDataStore.FindCallback;
import com.horaz.client.model.AsynchronousDataStore.ModelsCollection;
import com.horaz.client.model.SQLiteDataStore.SQLiteColumnDef;
import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelAddedListener;
import com.horaz.client.model.events.ModelRemovedEvent;
import com.horaz.client.model.events.ModelRemovedListener;
import com.horaz.client.model.events.ModelUpdatedEvent;
import com.horaz.client.model.events.ModelUpdatedListener;
import com.horaz.client.model.events.TableCreatedEvent;
import com.horaz.client.model.events.TableCreatedListener;

public class SQLiteDataStoreTest extends GWTTestCase {
	class TestingSQLiteDataStore extends SQLiteDataStore<TestModel> {
		public TestingSQLiteDataStore(String databaseName, String version,
				int maxSizeBytes) {
			super(databaseName, version, maxSizeBytes);
		}

		@Override
		public TestModel reflectJavaScriptObject(JavaScriptObject jsObj) {
			TestModelJS mdlDB = (TestModelJS) jsObj;
			TestModel mdl = new TestModel();
			mdl.setField("name", mdlDB.getName());
			mdl.setField("modelId", (long) mdlDB.getModelId());
			return mdl;
		}
	}

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
		public final native int getModelId() /*-{ return this.modelId; }-*/;
		public final native String getName() /*-{ return this.name; }-*/;
	}

	private boolean eventTableCreatedCatched;

	@Override
	public String getModuleName() {
		return "com.horaz.Horaz";
	}

	public void testAdd() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testadd", "1", 1024*1024);
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
										assertEquals(1, (long) mdlDB.getModelId());
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
		}.schedule(300);
		delayTestFinish(800);
	}

	public void testFind_whereBar_filter() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testFind_whereBar_filter", "1", 1024*1024);
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
				ds.find(filter, new FindCallback<TestModel>() {
					@Override
					public void onSuccess(ModelsCollection<TestModel> results) {
						Iterator<TestModel> it = results.iterator();
						assertTrue(it.hasNext());
						assertEquals("bar", it.next().getField("name"));
						assertFalse(it.hasNext());
						finishTest();
					}
				});
			}
		}.schedule(500);
		delayTestFinish(800);
	}

	public void testFind_whereBar_string() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testFind_whereBar_string", "1", 1024*1024);
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
				ds.find("name", "bar", new FindCallback<TestModel>() {
					@Override
					public void onSuccess(ModelsCollection<TestModel> results) {
						Iterator<TestModel> it = results.iterator();
						assertTrue(it.hasNext());
						assertEquals("bar", it.next().getField("name"));
						assertFalse(it.hasNext());
						finishTest();
					}
				});
			}
		}.schedule(500);

		delayTestFinish(800);
	}

	public void testFindAll_no_results() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testFindAll_no_results", "1", 1024*1024);
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
			}
		}.schedule(200);

		// find only "bar"
		new Timer() {
			@Override
			public void run() {
				ds.findAll("name", "xbar", new FindCallback<TestModel>() {
					@Override
					public void onSuccess(ModelsCollection<TestModel> results) {
						Iterator<TestModel> it = results.iterator();
						assertFalse(it.hasNext());
						finishTest();
					}
				});
			}
		}.schedule(500);

		delayTestFinish(800);
	}

	public void testFindAll_whereBar() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testfindall_whereBar", "1", 1024*1024);
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
				ds.findAll(filter, new FindCallback<TestModel>() {
					@Override
					public void onSuccess(ModelsCollection<TestModel> results) {
						Iterator<TestModel> it = results.iterator();
						assertTrue(it.hasNext());
						assertEquals("bar", it.next().getField("name"));
						assertFalse(it.hasNext());
						finishTest();
					}
				});
			}
		}.schedule(500);

		delayTestFinish(800);
	}

	public void testFindAll_whereBar_string() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testFindAll_whereBar_string", "1", 1024*1024);
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
				ds.findAll("name", "bar", new FindCallback<TestModel>() {
					@Override
					public void onSuccess(ModelsCollection<TestModel> results) {
						Iterator<TestModel> it = results.iterator();
						assertTrue(it.hasNext());
						assertEquals("bar", it.next().getField("name"));
						assertTrue(it.hasNext());
						assertEquals("bar", it.next().getField("name"));
						assertFalse(it.hasNext());
						finishTest();
					}
				});
			}
		}.schedule(500);

		delayTestFinish(800);
	}

	public void testFindAll_whereNotBar() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testFindAll_whereNotBar", "1", 1024*1024);
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
				Filter filter = new Filter().whereNotEquals("name", "bar");
				ds.findAll(filter, new FindCallback<TestModel>() {
					@Override
					public void onSuccess(ModelsCollection<TestModel> results) {
						Iterator<TestModel> it = results.iterator();
						assertTrue(it.hasNext());
						assertEquals("foo", it.next().getField("name"));
						assertFalse(it.hasNext());
						finishTest();
					}
				});
			}
		}.schedule(500);

		delayTestFinish(800);
	}

	public void testGet() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testget", "1", 1024*1024);
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
						ds.get(event.getModel().getModelId(), new FindCallback<TestModel>() {
							@Override
							public void onSuccess(ModelsCollection<TestModel> results) {
								Iterator<TestModel> it = results.iterator();
								assertTrue(it.hasNext());
								assertEquals("foo", it.next().getField("name"));
								assertFalse(it.hasNext());
								finishTest();
							}
						});
					}
				});

				// insert
				ds.add(mdl);
			}
		}.schedule(300);
		delayTestFinish(5000);
	}

	public void testInitTable() {
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testInit"+new Date().getTime(), "1", 1024*1024);
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

								// second time, no table-created event
								eventTableCreatedCatched = false;
								ds.initTable("testTbl", new SQLiteColumnDef[] {
										new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
								});
							}
						});
					}
					@Override
					public void onTransactionSuccess() {
					}
				});
			}
		}.schedule(200);

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
		}.schedule(500);
		delayTestFinish(1000);
	}

	public void testInitTable_lastModelId() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testInitTable_lastModelId", "1", 1024*1024);
		ds.initTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});

		// create and add model
		new Timer() {
			@Override
			public void run() {
				ds.addModelAddedListener(new ModelAddedListener<SQLiteDataStoreTest.TestModel>() {
					@Override
					public void onModelAdded(ModelAddedEvent<TestModel> event) {
						// modelId == 1
						assertEquals(1, event.getModel().getModelId());
					}
				});
				ds.add(new TestModel());
			}
		}.schedule(300);

		new Timer() {
			@Override
			public void run() {
				// setup new datastore + table
				final SQLiteDataStore<TestModel> ds2 = new TestingSQLiteDataStore("testInitTable_lastModelId", "1", 1024*1024);
				ds2.initTable("testTbl", new SQLiteColumnDef[] {
						new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
				});

				new Timer() {
					@Override
					public void run() {
						// create and add model
						ds2.addModelAddedListener(new ModelAddedListener<SQLiteDataStoreTest.TestModel>() {
							@Override
							public void onModelAdded(ModelAddedEvent<TestModel> event) {
								// modelId == 2
								assertEquals(2, event.getModel().getModelId());
								finishTest();
							}
						});
						ds2.add(new TestModel());
					}
				}.schedule(300);
			}
		}.schedule(800);
		delayTestFinish(3000);
	}

	public void testRemove() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testRemove", "1", 1024*1024);
		ds.initTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});

		// insert model
		final TestModel mdl = new TestModel();
		new Timer() {
			@Override
			public void run() {
				// create model
				mdl.setField("name", "foo");
				ds.add(mdl);
			}
		}.schedule(300);

		// remove model
		new Timer() {
			@Override
			public void run() {
				// register hook
				ds.addModelRemovedListener(new ModelRemovedListener<SQLiteDataStoreTest.TestModel>() {
					@Override
					public void onModelRemoved(ModelRemovedEvent<TestModel> event) {
						assertEquals(mdl.getModelId(), event.getModel().getModelId());
						// assert empty datastore
						ds.find("name", "foo", new FindCallback<SQLiteDataStoreTest.TestModel>() {
							@Override
							public void onSuccess(ModelsCollection<TestModel> results) {
								assertFalse(results.iterator().hasNext());
								finishTest();
							}
						});
					}
				});

				// remove
				ds.remove(mdl);
			}
		}.schedule(700);
		delayTestFinish(5000);
	}

	public void testUpdate() {
		// setup db + table
		final SQLiteDataStore<TestModel> ds = new TestingSQLiteDataStore("testUpdate", "1", 1024*1024);
		ds.initTable("testTbl", new SQLiteColumnDef[] {
				new SQLiteColumnDef("name", SQLiteColumnDef.Type.TEXT)
		});

		// insert model
		final TestModel mdl = new TestModel();
		new Timer() {
			@Override
			public void run() {
				// create model
				mdl.setField("name", "foo");
				ds.add(mdl);
			}
		}.schedule(300);

		// update
		new Timer() {
			@Override
			public void run() {
				// register update hook
				ds.addModelUpdatedListener(new ModelUpdatedListener<SQLiteDataStoreTest.TestModel>() {
					@Override
					public void onModelUpdated(ModelUpdatedEvent<TestModel> event) {
						assertEquals(mdl.getModelId(), event.getModel().getModelId());

						// check from DB
						ds.get(mdl.getModelId(), new FindCallback<SQLiteDataStoreTest.TestModel>() {
							@Override
							public void onSuccess(ModelsCollection<TestModel> results) {
								Iterator<TestModel> it = results.iterator();
								assertTrue(it.hasNext());
								assertEquals("bar", it.next().getField("name"));
								finishTest();
							}
						});
					}
				});

				// update
				mdl.setField("name", "bar");
				ds.update(mdl);
			}
		}.schedule(600);
		delayTestFinish(1200);
	}
}
