package com.horaz.client.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.storage.client.Storage;


/**
 * datastore that make the data persistent (html5 local storage).
 *
 * TODO re-implement with html5 database api for performance
 *
 * @param <T> model class
 */
public abstract class PersistentDataStore<T extends BaseModel> extends SimpleDataStore<T> {
	private final static String KEY_STUB_COUNT = "stubCount";
	private final static String KEY_STUB_CONTENT = "stubContent";
	private final static int STUB_SIZE = 30;

	private final String storeName;
	private final Storage localStorage;

	/**
	 * create
	 * @param storeName this is the prefix for the items in the localStorage
	 */
	public PersistentDataStore(String storeName) {
		this.storeName = storeName;
		localStorage = Storage.getLocalStorageIfSupported();
		if (localStorage == null) {
			throw new IllegalStateException("W3C HTML5 LocalStorage is not supported.");
		}
	}

	/**
	 * implement this! the serialized json object has to be converted into a new model
	 * @param attrs serialized data {@link #serializeModel(BaseModel)}
	 * @return model
	 */
	protected abstract T deserializeModel(JSONObject attrs);

	/**
	 * reads the storage and deserialize models
	 */
	public void load() {
		if (localStorage.getItem(storeName+"_"+KEY_STUB_COUNT) != null) {
			int stubCount = Integer.valueOf(localStorage.getItem(storeName+"_"+KEY_STUB_COUNT));
			for (int i=0; i<stubCount; i++) {
				String cont = localStorage.getItem(storeName+"_"+KEY_STUB_CONTENT+i);
				if (cont != null) {
					JSONArray stubModels = JSONParser.parseStrict(cont).isArray();
					for (int ii=0; ii<stubModels.size(); ii++) {
						// hashmap attributes
						JSONObject attrs = stubModels.get(ii).isObject();
						add(deserializeModel(attrs));
					}
				}
			}
		}
	}

	/**
	 * serialize and store the current models
	 */
	public void save() {
		List<T> allModels = getModels();
		int stubCount = (allModels.size() / STUB_SIZE) +1;
		localStorage.setItem(storeName+"_"+KEY_STUB_COUNT, String.valueOf(stubCount));
		for (int stub=0; stub<stubCount; stub++) {
			List<JSONObject> stubModels = new ArrayList<JSONObject>();
			for (int i=0; i<STUB_SIZE; i++) {
				int idx = stub*STUB_SIZE+i;
				if (idx == allModels.size()) break;

				JSONObject attrs = serializeModel(allModels.get(idx));
				stubModels.add(attrs);
			}
			localStorage.setItem(storeName+"_"+KEY_STUB_CONTENT+stub, stubModels.toString());
		}
	}

	/**
	 * every model will be serialized and stored as a json object. you have to implement the routine.
	 * @param model
	 * @return serialized model
	 */
	protected abstract JSONObject serializeModel(T model);
}
