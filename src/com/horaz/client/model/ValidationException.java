package com.horaz.client.model;

import com.horaz.client.model.BaseModel.ModelField;

/**
 * this exception is thrown when a validation failed.
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 6462853255638606639L;

	final private ModelField field;

	/**
	 * @param field the affected field
	 */
	public ValidationException(ModelField field) {
		this.field = field;
	}

	/**
	 * @return the affected field
	 */
	public ModelField getField() {
		return field;
	}
}
