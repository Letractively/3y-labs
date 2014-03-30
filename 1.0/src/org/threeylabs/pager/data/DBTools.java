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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
// 全局资源配置文件读取工具类
import org.threeylabs.pager.globalconf.LoaderUtil;

/**
 * 数据通用工具类
 * 
 * @author i 聪
 * @version 1.0
 * 
 */
public class DBTools {
	
	private ResultSet rs = null;
	private Connection conn = null;
	private PreparedStatement ps = null;

	@SuppressWarnings("rawtypes")
	protected List args;
	protected String sql;
	
	// dbconf :load DataSource.properties package's addr.
	public static String DBCONF = null;
	// dbtype :load DB conn's type(jdbc/jndi).
	public static String DBTYPE = null;
	
	// JNDI 连接池获取数据库连接对象
	protected Connection openConn(String jndiConfig) throws Exception {
		
		Context c = new InitialContext();
		DataSource ds = (DataSource) c.lookup("java:comp/env/" + jndiConfig);
		return ds.getConnection();
	}

	// JDBC 普式获取数据库连接对象
	private Connection openConn2() throws Exception {
			String[] ds_conf = LoaderUtil.instance.find(DBCONF, new String[] { 
					"SQL_DRIVER", 
					"SQL_URL", 
					"SQL_USER",
					"SQL_PWD" 
					});
			if ((null == ds_conf) || (ds_conf.length == 0))
				return null;
			Class.forName(ds_conf[0]);
			this.conn = DriverManager.getConnection(ds_conf[1], ds_conf[2], ds_conf[3]);
			return this.conn;
			
	}

	// manual to do GC's work.
	private void closeConn(Connection conn, PreparedStatement ps, ResultSet rs)
			throws Exception {
		if (rs != null) {
			rs.close();
			rs = null;
		}
		if (ps != null) {
			ps.close();
			ps = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	// SQL参数通用赋值
	private void setValues(PreparedStatement ps, List<?> args) throws Exception {
		if ((ps != null) && (args != null) && (args.size() > 0)) {
			int total_args = args.size();
			for (int i = 0; i < total_args; i++) {
				ps.setObject(i + 1, args.get(i));
			}
		}
	}

	// 通用更新
	protected int executeModify(String sql, List<?> args) throws Exception {
		try 
		{
			if(isUseJNDI()) this.conn = openConn("mypager");
			else this.conn = openConn2();
			this.ps = this.conn.prepareStatement(sql);
			setValues(this.ps, args);
			return this.ps.executeUpdate();
		} finally {
			this.closeConn(this.conn, this.ps, null);
		}
	}
	
	// 通用检索
	protected Result executeQuery(String sql, List<?> args) throws Exception {
			if(!checkDBConf()){
				throw new NullPointerException("dbconf error.");
			}
			try
			{
			if(isUseJNDI()) this.conn = openConn("mypager");
			else this.conn = openConn2();
			this.ps = this.conn.prepareStatement(sql);
			setValues(this.ps, args);
			this.rs = this.ps.executeQuery();
			return ResultSupport.toResult(this.rs);
			} finally {
				closeConn(this.conn, this.ps, this.rs);
			}
	}
	
	// 检查JDBC配置环境是否正常
	private boolean checkDBConf() {
		if((null == DBCONF) && (!isUseJNDI())) return false;
		return true;
	}
	
	// 检查JNDI配置环境是否正常
	private boolean isUseJNDI(){
		if(DBTYPE != null) return DBTYPE.toLowerCase().equals("jndi");
		return false;
	}
}