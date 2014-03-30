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
package org.threeylabs.pager.sts;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.threeylabs.pager.panel.PageControl;
/**
 * 分页核心处理器
 * 
 * @author i 聪
 * @version 1.0
 * 
 */
public class PanelTag extends SimpleTagSupport {

	// 每个属性作用和必要性参见 pager-taglib.tld
	private PageControl items;
	private String formname ;
	private String action;
	private String method;
	private String data;
	private String print;
	
	private final String[] native2ascii = { 
		"&nbsp;", 							// --> P[0]  
		"\u9996\u9875 \u4e0a\u4e00\u9875", 	// --> .     #首页 上一页
		"\u9996\u9875",     				// --> .     #首页
		"\u4e0a\u4e00\u9875",    			// --> .     #上一页
		"\u4e0b\u4e00\u9875 \u5c3e\u9875", 	// --> .     #下一页 尾页
		"\u4e0b\u4e00\u9875",   			// --> .     #下一页
		"\u5c3e\u9875",     				// --> .     #尾页
		"\u8f6c\u5230\u7b2c",     			// --> P[7]  #转到第
		"\u9875",                           // --> p[8]  #页
		"\u5171\u6709"                      // --> p[9]  #共有
		};
	
	private enum TAG {
		GET, POST, START, END
	};

	public void doTag() throws JspException, IOException {

		// <c:choose>
		// <c:when test="${pageCtl.curPage eq 1}">首页 上一页</c:when>
		// <c:otherwise>
		// <A HREF="javascript:gotoPage(1)">首页</A>
		// <A HREF="javascript:gotoPage(${pageCtl.curPage-1 })">上一页</A>
		// </c:otherwise>
		// </c:choose>
		// <c:choose>
		// <c:when test="${pageCtl.curPage eq pageCtl.maxPage }">下一页 尾页</c:when>
		// <c:otherwise>
		// <A HREF="javascript:gotoPage(${pageCtl.curPage+1 })">下一页</A>
		// <A HREF="javascript:gotoPage(${pageCtl.maxPage })">尾页</A>
		// </c:otherwise>
		// </c:choose>
		// ${'转到第' }
		// <SELECT name="jumpPage" onchange="Jumping()">
		// <c:forEach var="sel" begin="1" end="${pageCtl.maxPage }"
		// varStatus="o">
		// <c:if test="${o.index eq pageCtl.curPage }">
		// <OPTION selected value=${o.index }>${o.index }</OPTION>
		// </c:if>
		// <c:if test="${o.index ne pageCtl.curPage }">
		// <OPTION value=${o.index }>${o.index }</OPTION>
		// </c:if>
		// </c:forEach>
		// </SELECT>${'页' }

		StringBuilder sb = new StringBuilder("");
		sb.append("<FORM NAME='");
		sb.append(getFormname());
		sb.append("' ACTION='");
		sb.append(getAction());
		sb.append("' METHOD='");
		sb.append(getMethod());
		sb.append("'>");
		
		if (this.items.getCurPage() == 1) {
			sb.append(native2ascii[1]);
			sb.append(native2ascii[0]);
		} else {
			sb.append("<A HREF='JAVASCRIPT:VOID(0)' ONCLICK='");
			sb.append(gotoPage(1));
			sb.append("'>");
			sb.append(native2ascii[2]);
			sb.append("</A>");
			sb.append(native2ascii[0]);
			sb.append("<A HREF='JAVASCRIPT:VOID(0)' ONCLICK='");
			sb.append(gotoPage(this.items.getCurPage() - 1));
			sb.append("'>");
			sb.append(native2ascii[3]);
			sb.append("</A>");
			sb.append(native2ascii[0] + native2ascii[0]);
		}

		indexIterator(sb);

		if (this.items.getCurPage() == this.items.getMaxPage())
			sb.append(native2ascii[4]);
		else {
			sb.append("<A HREF='JAVASCRIPT:VOID(0)' ONCLICK='");
			sb.append(gotoPage(this.items.getCurPage() + 1));
			sb.append("'>");
			sb.append(native2ascii[5]);
			sb.append("</A>");
			sb.append(native2ascii[0]);
			sb.append("<A HREF='JAVASCRIPT:VOID(0)' ONCLICK='");
			sb.append(gotoPage(this.items.getMaxPage()));
			sb.append("'>");
			sb.append(native2ascii[6]);
			sb.append("</A>");
		}

		sb.append(native2ascii[0] + native2ascii[7]);
		sb.append("<SELECT NAME='JUMPPAGE' ONCHANGE='return document.");
		sb.append(getFormname());
		sb.append(".submit()'>");
		for (int i = 1; i <= this.items.getMaxPage(); i++) {
			if (this.items.getCurPage() == i)
				sb.append("<OPTION SELECTED VALUE='");
			else
				sb.append("<OPTION VALUE='");
			sb.append(i);
			sb.append("'>");
			sb.append(i);
			sb.append("</OPTION>");
		}
		sb.append("</SELECT>");
		sb.append(native2ascii[8]);
		if ((this.print != null) && (!"".equals(this.print)) && ("true".equals(this.print.toLowerCase())))
		  print(sb);
		sb.append("</FORM>");
		
		getJspContext().setAttribute(this.data, sb);
		getJspBody().invoke(null);
	}

	// 打印详细页码
	@SuppressWarnings("rawtypes")
	private StringBuilder indexIterator(StringBuilder myBuilder) {
		Map index = this.getIndex();
		for (int i = ((Integer)index.get(TAG.START)).intValue(); i <= ((Integer)index.get(TAG.END)).intValue(); i++)
		{
			if (this.items.getCurPage() == i)
				myBuilder.append(i);
			else {
				myBuilder.append("<A HREF='JAVASCRIPT:VOID(0)' ONCLICK='");
				myBuilder.append(gotoPage(i));
				myBuilder.append("'>");
				myBuilder.append(i);
				myBuilder.append("</A>");
			}
			myBuilder.append(native2ascii[0]);
		}
		myBuilder.append(native2ascii[0]);
		
		return myBuilder;
	}

	// function JUMPING() {
	// document.PageForm.submit();
	// return;
	// }
	// function GOTOPAGE(pagenum) {
	// document.PageForm.JUMPPAGE.value = pagenum;
	// document.PageForm.submit();
	// return;
	// }
	
	// 整合JavaScript
	private String gotoPage(int page) {
		StringBuilder homePage = new StringBuilder("document.");
		homePage.append(getFormname());
		homePage.append(".JUMPPAGE.value = ");
		homePage.append(page);
		homePage.append("; return document.");
		homePage.append(getFormname());
		homePage.append(".submit()");

		return homePage.toString();
	}
	
	// 打印分页统计
	private StringBuilder print(StringBuilder sb){
		sb.append(native2ascii[0]);
		sb.append(native2ascii[9]);
		sb.append(this.items.getMaxPage());
		sb.append(native2ascii[8]);
		
		return sb;
	}
	
	// 移动索引's algo.h
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<Enum, Integer> getIndex() {
		int left = PageControl.getRows() >> 1;
		int right = PageControl.getRows() - left - 1;
		int startIndex;
		int endIndex;
		if (this.items.getCurPage() <= left + 1) {
			startIndex = 1;
			endIndex = PageControl.getRows();
		} else {
			startIndex = this.items.getCurPage() - left;
			endIndex = this.items.getCurPage() + right;
		}

		Map index = new HashMap();
		endIndex = endIndex > this.items.getMaxPage() ? this.items.getMaxPage() : endIndex;
		index.put(TAG.START, Integer.valueOf(startIndex));
		index.put(TAG.END, Integer.valueOf(endIndex));

		return index;
	}

	// 默认及自定义表单名称设定
	public String getFormname() {
		if((this.formname == null) || ("".equals(this.formname)))
			// 缺省既定FORMNAME	默认值
			return "PageForm";
		return this.formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		if((this.method == null) || ("".equals(this.method)))
			// 缺省既定GET传输方式
			return TAG.GET.toString();
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public PageControl getItems() {
		return this.items;
	}

	public void setItems(PageControl items) {
		this.items = items;
	}

	public String getPrint() {
		return this.print;
	}

	public void setPrint(String print) {
		this.print = print;
	}

}
