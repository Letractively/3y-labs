/*
 *		Copyright 2013 code.google.com/p/3y-labs
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */
package org.threeylabs.pager.globalconf;

import java.util.ResourceBundle;

/**
 * 资源配置文件读取工具类
 * 
 * @author i 聪
 * @version 1.0
 * 
 */
public enum LoaderUtil {

	// 枚举this实例
	instance;

	private String[] getValue(String path, String[] key) {
		try {
			String[] args = new String[key.length];
			ResourceBundle rb = ResourceBundle.getBundle(path);

			for (int i = 0; i < key.length; i++) {
				args[i] = rb.getString(key[i]);
			}
			return args;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String[] find(String path, String[] args) {
		return getValue(path, args);
	}
}