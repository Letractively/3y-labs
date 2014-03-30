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
package org.threeylabs.pager.panel;

import org.threeylabs.pager.data.PageLayer;

/**
 * 分页工厂
 * 
 * @author i 聪
 * @version 1.0
 * 
 */
public class PageControl {

	private int curPage; 				// #当前页码
	private int rowsPerPage = 10;	 	// #页码容量
	private int maxPage; 				// #页码统计
	private int maxRowCount; 			// #行数统计
	private Object originalData; 		// #原始数据
	protected static int ROWS;	    	// #单页容量
	
	static{
		ROWS = 0;
	}

	// 使用获取的最大数据容量来计算能进行分页的最大数量
	private void countMaxPage() {
		if (ROWS != 0)
			this.rowsPerPage = ROWS;
		if (this.maxRowCount % this.rowsPerPage == 0) {
			this.maxPage = this.maxRowCount / this.rowsPerPage;
		} else {
			// 余下的数据不足一页容量继续补1
			this.maxPage = this.maxRowCount / this.rowsPerPage + 1;
		}
	}

	// 业务化(组装)完整分页对象
	public PageControl(PageLayer myPL) throws Exception {

		// 获取数据容量大小
		this.maxRowCount = myPL.getAvailableCount();
		// 获取分页标准容量的数据
		this.originalData = myPL.getResult();
		// 统计能进行分页的页码总数
		countMaxPage();
		// 缓存当前页码
		this.curPage = myPL.getCurPage();
	}
	
	public static void setRows(int rows) {
		ROWS = rows;
	}
	
	public static int getRows(){
		return ROWS;
	}

	public int getCurPage() {
		return this.curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getRowsPerPage() {
		return this.rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getMaxPage() {
		return this.maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getMaxRowCount() {
		return this.maxRowCount;
	}

	public void setMaxRowCount(int maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	public Object getOriginalData() {
		return this.originalData;
	}

	public void setOriginalData(Object originalData) {
		this.originalData = originalData;
	}

}
