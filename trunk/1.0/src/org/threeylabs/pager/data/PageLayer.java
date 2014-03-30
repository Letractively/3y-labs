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
package org.threeylabs.pager.data;

/**
 * 分页工厂接口 
 * <br/><br/>需要进行分页的业务体(对象或者接口) 必须实现(或继承)此接口。 mypager.jar 所需要的分页数据对获取方式没有限定。
 * 即可以在Frame或非Frame's MVC体系中生存。 所以要求使用者在获取(分页)数据后有硬性业务规定：
 * (下面三个方法是使用mypager所必需实现的Service。缺一不可。)
 * <br/><font color='purple' size='+1'>
 * 一、 获取数据(Table/Collections)总体容量 。<br/>
 * 二、获取标准分页容量的数据(即分页数据)。<br/>
 * 三、缓存当前页码(初始化需设置默认值)。 </font>
 * 
 * @author i 聪
 * @version 1.0
 * 
 */
public interface PageLayer {

	// 获取数据容量
	int getAvailableCount() throws Exception;

	// 获取分页数据
	Object getResult() throws Exception;

	// 缓存当前页码
	int getCurPage() throws Exception;
}
