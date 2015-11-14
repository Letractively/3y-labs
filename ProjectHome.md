
---

<font color='#CDAA7D'><pre>
mypager1.1 next final version Pre:<br>
1、Panel 标签内容可定义<br>
2、分页交互参数可定义<br>
3、PanelTag 重构(用于提供可扩展Panel)<br>
4、Dyna Xml2Beans 特性<br>
5、Source code official version<br>
</pre></font>

---

The following is:<br />
一、pager-taglib.tld 的使用

```
  * 将mypager.jar org.threeylabs.pager.sts 包下 pager-taglib.tld拷贝到WEB-INF及任意子目录。

  * TLD使用 Apache License 2.0开源协议。
```

二、实现分页工厂定义的必要方法

```
PageLayerExt implements PageLayer  // PageLayerExt是示例中的业务实现类
```
  * int getAvailableCount()  **获取数据容量 。**<br />
  * Object getResult()  **获取分页数据。**<br />
  * int getCurPage()  **缓存页码。**
> _需要进行分页的业务类(对象或接口)必须实现(或继承)此接口_。

三、 Controller中接收参数名为"_**JUMPPAGE**_"的值

```
String nextPage = request.getParameter("JUMPPAGE");  // 初始化需要设定为1
```

四、使用 _**PageControl**_对象

```
import org.threeylabs.pager.panel.PageControl;

PageControl pageCtl = new PageControl(new PageLayerExt(nextPage));
```

五、保存数据

```
request.setAttribute("pageCtl", pageCtl);
```

六、页面使用mypager

  * 导入mypager标签库
```xml

<%@ taglib uri="/mypager" prefix="sts"%>
```
  * 加入mypager分页标签
```xml

<sts:panel items="${pageCtl }" data="infos" action="mp">${infos} 

Unknown end tag for &lt;/panel&gt;


```
  * Panel标签是翻页的动作体。包含翻页的必要元素(首页/上一页/各页码/下一页/尾页/跳转到/及统计)。显示分页数据时需要遍历使用 _**PageControl**_'s _**originalData**_属性。它是查询的分页数据 _**Collections**_。
```xml

<c:forEach items="${pageCtl.originalData }" var="data">  ${data.[attribute] }
.....
```

七、Panel标签属性：

| **属性名称** | **作用** | **必要** |
|:---------|:-------|:-------|
| items    | Controller在Request域设置的 _**PageControl**_。 | √      |
| rows     | 单页数据容量。默认每页显示10条。 | x      |
| data     | 指this。Panel标签中使用EL输出。 | √      |
| formname | 翻页动作的表单名称。默认"_**PageForm**_"。 | x      |
| action   | 翻页动作的表单提交地址。 |  √     |
| method   | 翻页动作的表单提交方式。默认GET。 | x      |
| print    | 显示统计信息。默认FALSE。 | x      |
| dbconf   | DB2s连接配置文件地址。文件签名使用.properties。 | x      |
| dbtype   | DB2s连接方式(JDBC/JNDI)。 | x      |

DEMO
_**[下载地址](http://code.google.com/p/3y-labs/downloads/list)**_