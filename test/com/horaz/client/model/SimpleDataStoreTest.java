/**
 * This file is part of Horaz.
 *
 * Horaz is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Horaz is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Horaz.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Joe's App Factory UG (haftungsbeschränkt)
 */

package com.horaz.client.model;

import com.google.gwt.junit.client.GWTTestCase;
import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelAddedListener;

public class SimpleDataStoreTest extends GWTTestCase {
	class TestModel extends BaseModel {
		@Override
		protected ModelField[] getStructure() {
			return new ModelField[] {
					new ModelField("name")
			};
		}
	}

	@Override
	public String getModuleName() {
		return "com.horaz.Horaz";
	}

	public void testAdd() {
		SimpleDataStore<TestModel> ds = new SimpleDataStore<TestModel>();
		TestModel mdl = new TestModel();
		mdl.setField("name", "foo");

		// register hook
		ds.addModelAddedListener(new ModelAddedListener<SimpleDataStoreTest.TestModel>() {
			@Override
			public void onModelAdded(ModelAddedEvent<TestModel> event) {
				assertEquals(1, event.getModel().getModelId());
				assertEquals("foo", event.getModel().getField("name"));
			}
		});

		ds.add(mdl);
	}
}
