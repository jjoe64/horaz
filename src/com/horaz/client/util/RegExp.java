/*
 * Copyright 2008 Folke Behrens (fb at toxis. com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.horaz.client.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper for the RegExp JavaScript object. Can be passed as parameter to JSNI
 * methods. Almost all of its methods get inlined. No extra weight.
 * <p>
 * RegExp literals like /abc/ are not possible. For this we need a Generator
 * that can except parameters which is currently not possible with GWT.
 *
 * @author Folke Behrens (fb at toxis. com)
 * @version 1.0
 */
/*
 * Please leave the JSNI opening tag "/*-{" on its own line to workaround a bug
 * in Eclipse' formatter.
 */
public final class RegExp extends JavaScriptObject {

  /**
   * Wraps the result of any RegExp operation which is either an object or an
   * array of strings. Use the accessor methods, or use the iterator, or
   * <code>cast()</code> it to JsArrayString to access the matched strings.
   */
  public static final class Result extends JavaScriptObject {

    public class IterableIterator implements Iterable<String>, Iterator<String> {
      private int index = 0;

      @Override
	public boolean hasNext() {
        return index < length();
      }

      @Override
	public Iterator<String> iterator() {
        return this;
      }

      @Override
	public String next() {
        if (index == length())
          throw new NoSuchElementException();
        assert index < length() : index + " > " + length();
        return get(index++);
      }

      @Override
	public void remove() {
        throw new UnsupportedOperationException();
      }
    }

    protected Result() {
    }

    /**
     * Depending on the global ('g') flag this either returns a 0-indexed match
     * or 1-indexed submatch.
     *
     * @param num
     * @return match (0-based index) or submatch (1-based index)
     */
    public native String get(int num)
    /*-{
      return this[num];
    }-*/;

    /**
     * @return The index of the match. Always <code>0</code> in global matching
     *         mode.
     */
    public native int index()
    /*-{
      return this.index || 0;
    }-*/;

    /**
     * @return The input string. Always <code>null</code> in global matching
     *         mode.
     */
    public native String input()
    /*-{
      return this.input || null;
    }-*/;

    /**
     * Returns an Iterator which is also Iterable and can be used in foreach
     * loops. This is because JavaScriptObject cannot implement interfaces. :-(
     */
    public IterableIterator iterator() {
      return new IterableIterator();
    }

    /**
     * @return the number of matches.
     */
    public native int length()
    /*-{
      return this.length;
    }-*/;

    /**
     * @return the matched string.
     */
    public native String match()
    /*-{
      return this[0];
    }-*/;

    public String[] toArray() {
      return toArray(new String[length()]);
    }

    /**
     * Copies the matches into <code>stringArray</code>.
     *
     * @param stringArray
     * @return stringArray
     */
    public String[] toArray(String[] stringArray) {
      for (int i = 0; i < length(); ++i) {
        stringArray[i] = this.get(i);
      }
      return stringArray;
    }
  }

  /**
   * Factory method for RegExp object.
   *
   * @param pattern
   * @return the newly created RegExp object
   */
  public static native RegExp create(String pattern)
  /*-{
    return new RegExp(pattern);
  }-*/;

  /**
   * Factory method for RegExp object.
   *
   * @param pattern
   * @param flags Any combination of <code>'g'</code>, <code>'m'</code>, and
   *          <code>'i'</code>, and some browser-specific flags like
   *          <code>'y'</code>.
   * @return the newly created RegExp object
   */
  public static native RegExp create(String pattern, String flags)
  /*-{
    return new RegExp(pattern, flags);
  }-*/;

  protected RegExp() {
  }

  /**
   * @param str
   * @return result
   */
  public native Result exec(String str)
  /*-{
    return this.exec(str);
  }-*/;

  /**
   * @return the index following the end of the last match
   */
  public native int getLastIndex()
  /*-{
    return this.lastIndex || 0;
  }-*/;

  /**
   * @return the pattern without surrounding quotes or slashes
   */
  public native String getSource()
  /*-{
    return this.source || null;
  }-*/;

  /**
   * @return true if 'g' flag is set.
   */
  public native boolean isGlobal()
  /*-{
    return this.global || false;
  }-*/;

  /**
   * @return true if 'i' flag is set.
   */
  public native boolean isIgnoringCase()
  /*-{
    return this.ignoreCase || false;
  }-*/;

  /**
   * @return true if 'm' flag is set.
   */
  public native boolean isMultiline()
  /*-{
    return this.multiline || false;
  }-*/;

  /**
   * @return true if 'y' flag is set. This is currently only supported by
   *         Firefox 3.
   */
  public native boolean isSticky()
  /*-{
    return this.sticky || false;
  }-*/;

  /**
   * @param str
   * @return result
   */
  public native Result match(String str)
  /*-{
    return str.match(this);
  }-*/;

  /**
   * @param str
   * @param replacement
   * @return result
   */
  public native String replace(String str, String replacement)
  /*-{
    return str.replace(this, replacement);
  }-*/;

  /**
   * @param str
   * @return the index of the match inside <code>str</code>. Otherwise, it
   *         returns -1.
   */
  public native int search(String str)
  /*-{
    return str.search(this);
  }-*/;

  /**
   * @param lastIndex
   */
  public native void setLastIndex(int lastIndex)
  /*-{
    this.lastIndex = lastIndex;
  }-*/;

  /**
   * @param str
   * @return result
   */
  public native Result split(String str)
  /*-{
    return str.split(this);
  }-*/;

  /**
   * This split() is different from java.lang.String.split(). The limit is
   * applies after the complete string is split.
   *
   * @param str
   * @param limit
   * @return result
   */
  public native Result split(String str, int limit)
  /*-{
    return str.split(this, limit);
  }-*/;

  /**
   * This method tests if a string matches the specified pattern.
   *
   * @param str the string to be tested
   * @return true if <code>text</code> matches the pattern.
   */
  public native boolean test(String str)
  /*-{
    return this.test(str);
  }-*/;
}
