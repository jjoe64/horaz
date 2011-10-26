package com.horaz.client.model;

import java.util.HashMap;
import java.util.Map;

public class Filter {
	private final Map<String, Object> whereEquals = new HashMap<String, Object>();

	public boolean match(BaseModel mdl) {
		// whereEquals
		for (Map.Entry<String, Object> entry : whereEquals.entrySet()) {
			if (!mdl.getField(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}

		return true;
	}

	public Filter whereEquals(String field, Object value) {
		whereEquals.put(field, value);
		return this;
	}
}
