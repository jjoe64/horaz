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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * abstracts a filter pattern, that can be used for filter {@link DataStore}s.
 *
 * {@code
 * 	ds.setFilter(new Filter().whereEquals("id", 5).whereNotEquals("name", "bus"));
 * }
 *
 * @see http://www.dev-horaz.com/dev-guide/datastore
 */
public class Filter {
	private final Map<String, Object> whereEquals = new HashMap<String, Object>();
	private final Map<String, Object> whereNotEquals = new HashMap<String, Object>();
	private final List<String> where = new ArrayList<String>();

	/**
	 * gets sql statement for this filter.
	 * The statement uses paceholders '?'. You need to
	 * use {@link #getValues()} to get the values.
	 * @param colPrefix can be used for table e.g. "users.", must not be null
	 * @return sql
	 */
	public String getSQLStatement(String colPrefix) {
		StringBuffer sql = new StringBuffer();
		for (Map.Entry<String, Object> entry : whereEquals.entrySet()) {
			sql.append("AND " + colPrefix+entry.getKey()+"=? ");
		}

		// not where
		for (Map.Entry<String, Object> entry : whereNotEquals.entrySet()) {
			sql.append("AND " + colPrefix+entry.getKey()+"!=? ");
		}

		// (custom) where
		for (String w : where) {
			sql.append("AND "+w+" ");
		}

		if (sql.length() == 0) {
			return "1=1";
		}
		return sql.substring(4);
	}

	/**
	 * use this in combination with {@link #getSQLStatement(String)}
	 * @return values for the generated sql statement
	 */
	public Object[] getValues() {
		List<Object> values = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : whereEquals.entrySet()) {
			values.add(entry.getValue());
		}
		for (Map.Entry<String, Object> entry : whereNotEquals.entrySet()) {
			values.add(entry.getValue());
		}

		return values.toArray(new Object[values.size()]);
	}

	public boolean match(BaseModel mdl) {
		//TODO refactoring: code in SimpleDataStore

		// whereEquals
		for (Map.Entry<String, Object> entry : whereEquals.entrySet()) {
			if (!mdl.getRawField(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}

		// whereNotEquals
		for (Map.Entry<String, Object> entry : whereNotEquals.entrySet()) {
			if (mdl.getRawField(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * merges another filter into this
	 * @param filter
	 * @return itself
	 */
	public Filter mergeFilter(Filter filter) {
		whereEquals.putAll(filter.whereEquals);
		whereNotEquals.putAll(filter.whereNotEquals);
		where.addAll(filter.where);
		return this;
	}

	/**
	 * custom where clause
	 * @param string
	 * @return itself
	 */
	public Filter where(String string) {
		where.add(string);
		return this;
	}

	/**
	 * adds a where-equals clause ("field == value")
	 * @param field
	 * @param value
	 * @return itself
	 */
	public Filter whereEquals(String field, Object value) {
		whereEquals.put(field, value);
		return this;
	}

	/**
	 * adds a where-not-equals clause ("field != value")
	 * @param field
	 * @param value
	 * @return itself
	 */
	public Filter whereNotEquals(String field, Object value) {
		whereNotEquals.put(field, value);
		return this;
	}
}
