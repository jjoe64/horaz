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
