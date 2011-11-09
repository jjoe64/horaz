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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TextAreaElement;

/**
 * Models represent the data in your app.
 * To create a own Model you have to extend the this class.
 * Every Model has to implement the getStructure method.
 * This method must return the fields of the model.
 * At this point we can also set a validation rule for the fields.
 *
 * Models can have children of models. So you can build trees of models.
 *
 * @see http://www.dev-horaz.com/dev-guide/create-a-model
 */
public abstract class BaseModel {
	/**
	 * defines a model field
	 */
	public class ModelField {
		private final String name;
		private final Validation validation;

		/**
		 * @param name field name
		 */
		public ModelField(String name) {
			this.name = name;
			this.validation = null;
		}

		/**
		 * @param name field name
		 * @param validation used validation
		 */
		public ModelField(String name, Validation validation) {
			this.name = name;
			this.validation = validation;
		}

		public String getName() {
			return name;
		}

		public Validation getValidation() {
			return validation;
		}
	}

	final private Map<String, Object> fields;
	final private int modelId;
	private ArrayList<BaseModel> children;
	static private int lastId = 0;

	/**
	 * creates an empty model
	 */
	public BaseModel() {
		fields = new HashMap<String, Object>(getStructure().length);
		modelId = lastId++;
	}

	/**
	 * add a child to a model. so you can build trees of models
	 * @param mdl
	 */
	public void addChild(BaseModel mdl) {
		if (children == null) {
			children = new ArrayList<BaseModel>();
		}
		children.add(mdl);
	}

	/**
	 * @return all children of this model. if there were no children an empty list returns.
	 */
	public List<BaseModel> getChildren() {
		return children==null?new ArrayList<BaseModel>():new ArrayList<BaseModel>(children);
	}

	/**
	 * reads the property for a given field name
	 * @param fieldname
	 * @return value
	 */
	public Object getField(String fieldname) {
		return fields.get(fieldname);
	}

	/**
	 * must return a unique model id
	 * @return unique model id
	 */
	public int getModelId() {
		return modelId;
	}

	/**
	 * Every model has to implement the getStructure method.
	 * This method must return the fields of the model.
	 * At this point we can also set a validation rule for the fields.
	 * @return array of ModelField. For every field a ModelField.
	 */
	protected abstract ModelField[] getStructure();

	/**
	 * sets a single field
	 * @param fieldname
	 * @param value
	 */
	public void setField(String fieldname, Object value) {
		// TODO validation
		fields.put(fieldname, value);
	}

	/**
	 * reads data from form elements
	 * @param form
	 * @throws ValidationException if validation fails
	 */
	public void setFields(FormElement form) throws ValidationException {
		for (ModelField fld : getStructure()) {
			Element elm = form.getElements().getNamedItem(fld.name);
			if (elm == null) {
				continue;
			}
			// get values
			Object value;
			if (elm instanceof InputElement) {
				value = ((InputElement) elm).getValue();
			} else if (elm instanceof TextAreaElement) {
				value = ((TextAreaElement) elm).getValue();
			} else {
				// TODO vervollständigen
				value = elm.toString();
			}
			if (fld.validation != null) {
				if (!fld.validation.validate(value)) {
					throw new ValidationException(fld);
				}
			}
			fields.put(fld.name, value);
		}
	}

	@Override
	public String toString() {
		return super.toString()+"//"+fields;
	}
}
