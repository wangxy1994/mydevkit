/******************************************** 
 * 文件名称: ErrMsg.java 
 * 系统名称: 资金业务管理系统
 * 模块名称: 
 * 软件版权:   
 * 功能说明: 
 * 系统版本: 2.5.0.1 
 * 开发人员: stone
 * 开发时间: 2010-12-24 上午09:50:52 
 * 审核人员: 
 * 相关文档: 
 * 修改记录: 修改日期     修改人员     修改说明 
 * 	               20140919    qibb           增加中债直连的错误定义 
 *********************************************/
package com.wangxy.exoskeleton.risk.exception;

public class IErrMsg {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2014-07-09  @describe ";
	/**成功*/
	public static final String ERR_SUCCESS                = "0000";
	/**部分成功*/
	public static final String ERR_PARTSUCC               = "0001";
	/**全部失败*/
	public static final String ERR_ALLFAIL                = "0002";
	/**未知错误*/
	public static final String ERR_DEFAULT                = "9999";

	/*系统类(10XX)*/
	/**前台请求解包失败*/
	public static final String ERR_UNPACKQT               = "1000";
	/**返回前台打包失败*/
	public static final String ERR_PACKQT                 = "1001";
	/**接收后台解包失败*/
	public static final String ERR_UNPACKHT               = "1002";
	/**发送后台打包失败*/
	public static final String ERR_PACKHT                 = "1003";
	/**发送后台接口失败*/
	public static final String ERR_SENDPKG                = "1004";
	/**通讯超时*/
	public static final String ERR_TIMEOUT                = "1005";
	/**接收后台接口失败*/
	public static final String ERR_RECVPKG                = "1006";
	/**取系统参数失败*/
	public static final String ERR_GETSYSARG              = "1007";
	/**生成交易流水号失败*/
	public static final String ERR_GENSERIAL              = "1008";
	/**打开文件失败*/
	public static final String ERR_FILEOPEN               = "1009";
	/**创建文件失败*/
	public static final String ERR_FILECREATE             = "1010";
	/**文件校验失败*/
	public static final String ERR_FILECHECK              = "1011";
	/**FIX打开错*/
	public static final String ERR_FIXOPEN                = "1012";
	/**取配置参数错*/
	public static final String ERR_GETCFGITEM             = "1013";
	/**修改配置参数错*/
	public static final String ERR_MODIFYCFGITEM          = "1014";
	/**报文非法*/
	public static final String ERR_PACKAGE                = "1015";
	/**参数错*/
	public static final String ERR_PARAMETER              = "1016";
	/**转换成请求包出错*/
	public static final String ERR_TRANSTOREPORT          = "1017";
	/**交易参数配置错*/
	public static final String ERR_TRANSINI               = "1018";
	/**联动交易调用共享库错*/
	public static final String ERR_LINK_TRANS             = "1019";
	/**联动交易获取交易配置参数错*/
	public static final String ERR_LINK_NOTRANS           = "1020";
	/**发送主机失败*/
	public static final String ERR_SENDHOST               = "1021";
	/**接收主机应答失败*/
	public static final String ERR_RECVHOST               = "1022";
	/**信号灯初始化错*/
	public static final String ERR_SIMINIT                = "1023";
	/**MFS转struct失败*/
	public static final String ERR_MFSTOSTRUCT            = "1024";
	/**主机交易需要冲正*/
	public static final String ERR_NEEDHOSTREPEAL         = "1025";
	/**文件不存在*/
	public static final String ERR_FILENOTEXIST           = "1026";
	/**物理日期和系统日期不一致,当日无需跑日启和日终*/
	public static final String ERR_NOSYSDATE              = "1027";
	/**文件长度为0*/
	public static final String ERR_FILELENGTHZERO         = "1028";
	/**文件传输完成*/
	public static final String ERR_FILEFINISH             = "1029";
	/**由于ERR_FILENOTEXIST已被其它情况使用，新增错误类型：文件找不到，用于清结算时未提供文件处理*/
	public static final String ERR_FILENOTFOUND           = "1030";

	/*委托类(11XX)*/
	/**无此委托*/
	public static  String ERR_NOTREPORT                   = "1101";
	/**委托部撤*/
	public static  String ERR_PARTCANCELED                = "1102";
	/**委托全部撤单*/
	public static  String ERR_ALLCANCELED              = "1103";
	/**委托全部成交*/
	public static  String ERR_ALLSUCCEED              = "1104";
	/**委托废单*/
	public static  String ERR_ENTRUSTFAIL                = "1105";

	
	/**交易码非法*/
	public static  String ERR_NOTRANS                     = "1300";
	/**交易日期非法*/
	public static final String ERR_DATE                   = "1372";
	/**查询数据字典表错误*/
	public static final String ERR_DICTQUERY              = "1418";
	/**查询交易日信息表错误*/
	public static final String ERR_WORKDAYQUERY           = "1419";
	/**查询机构信息表错误*/
	public static final String ERR_BRANCHQUERY            = "1420";
	/**查询TA信息表错误*/
	public static final String ERR_TAINFOQUERY            = "1421";
	/**查询错误信息表错误*/
	public static final String ERR_GETERRMSG              = "1422";

	/*日终批量类(18XX)*/
	/**批量有未完成步骤,不允许此操作*/
	public static final String ERR_NOTREADY               = "1800";
	/**批量正在执行,不允许再次操作*/
	public static final String ERR_ONDOING                = "1801";
	/**批量已经执行,不允许重复操作*/
	public static final String ERR_HAVEDONE               = "1802";
	/**更新批处理状态失败*/
	public static final String ERR_UPDBATSTATUS           = "1803";
	/**批量文件头校验失败*/
	public static final String ERR_FILEHEAD               = "1804";
	/**数据准备失败*/
	public static final String ERR_PREPAREDATA            = "1805";
	/**执行SHELL脚本错*/
	public static final String ERR_EXECSHELL              = "1806";
	/**组合文件名出错*/
	public static final String ERR_REPLACEFILETYPE        = "1807";
	/**是否重复执行备份*/
	public static final String ERR_BACKUPAGAIN            = "1808";
	/**备份文件未生成*/
	public static final String ERR_BACKUPNOTBUILD         = "1809";
	/**修改前备份日期参数失败*/
	public static final String ERR_MODIBACKUP1DATE        = "1810";
	/**创建备份日志文件失败*/
	public static final String ERR_CREATEBACKUPLOG        = "1811";
	/**恢复数据库失败*/
	public static final String ERR_RESTOREBACKUP          = "1812";
	/**打开数据库失败*/
	public static final String ERR_OPENDATABASE           = "1813";
	/**系统正在清算,只允许查询*/
	public static final String ERR_NOTBUSINFINISH         = "1814";
	/**无后备份路径参数*/
	public static final String ERR_NOBACKUP2DIR           = "1815";
	/**提示后备份文件及路径*/
	public static final String ERR_NOBACKUP2FNAME         = "1816";
	/**提示覆盖后备份文件*/
	public static final String ERR_UPBACKUP2FNAME         = "1817";
	/**无后备份日期参数*/
	public static final String ERR_NOBACKUP2DATE          = "1818";
	/**未知后备份检查标志*/
	public static final String ERR_UKBACKUP2FLAG          = "1819";
	/**未设置后备份允许标志*/
	public static final String ERR_NOBACKUP2FLAG          = "1820";
	/**禁止后备份*/
	public static final String ERR_FORBIDBACKUP2          = "1821";
	/**修改后备份日期参数失败*/
	public static final String ERR_MODIBACKUP2DATE        = "1822";
	/**设置系统清算标志错*/
	public static final String ERR_SETSYSCLEARFLAG        = "1823";
	/**获取交易日信息失败*/
	public static final String ERR_GETEXDATE              = "1837";
	/**获取下一交易日帅败*/
	public static final String ERR_GETNEXTDATE            = "1838";
	/**系统未做后备份*/
	public static final String ERR_NOBACKUP2              = "1839";
	/**系统无DETAIL目录*/
	public static final String ERR_NODETAILPATH           = "1840";
	/**初始化数据汇总失败*/
	public static final String ERR_DATATOT                = "1841";
	/**初始化扎账汇总失败*/
	public static final String ERR_SQUAREFUND             = "1842";
	/**初始化资金汇总失败*/
	public static final String ERR_FUNDTOT                = "1843";
	/**初始化生成账户汇总表失败*/
	public static final String ERR_CREATEZHHZB            = "1844";
	/**初始化业务汇总失败*/
	public static final String ERR_BUSINTOT               = "1845";
	/**初始化生成交易统计表失败*/
	public static final String ERR_CREATEJYTJB            = "1846";
	/**初始化导入历史库失败*/
	public static final String ERR_ADDTOHIS               = "1847";
	/**初始化归零失败*/
	public static final String ERR_RZCLEAR                = "1848";
	/**系统正在系统初始化*/
	public static final String ERR_INITING                = "1849";
	/**系统初始化失败*/
	public static final String ERR_INITERR                = "1850";
	/**系统未设置INIT参数*/
	public static final String ERR_NOSETINIT              = "1851";
	/**系统未设置RZSZ参数*/
	public static final String ERR_NORZSZ                 = "1852";
	/**系统未设置数据上账返回码*/
	public static final String ERR_NORZSZRETCODE          = "1853";
	/**TA未置清算正确*/
	public static final String ERR_NOCLEAROK              = "1854";
	/**系统未执行系统初始化*/
	public static final String ERR_NOINIT                 = "1855";
	/**备份目录下未建LOG目录*/
	public static final String ERR_NOBAKLOG               = "1856";
	/**备份目录下未建REPORT目录*/
	public static final String ERR_NOBAKREPORT            = "1857";
	/**备份目录下未建DETAIL目录*/
	public static final String ERR_NOBAKDETAIL            = "1858";
	/**修改数据归档日期参数失败*/
	public static final String ERR_MODIBACKUP3DATE        = "1859";
	/**未设置数据归档日期参数*/
	public static final String ERR_NOBACKUP3DATE          = "1860";
	/**数据归档日期不匹配*/
	public static final String ERR_BAK3NOTMATCH           = "1861";
	/**请求包中operflag值非法*/
	public static final String ERR_OPERFLAG               = "1862";
	/**对账模式值非法*/
	public static final String ERR_CHECKMODE              = "1863";
	/**日期非法*/
	public static final String ERR_ASSODATE               = "1864";
	/**历史库无该卸载记录*/
	public static final String ERR_NOBACKUPRECD           = "1865";
	/**数据已卸载,不能重复卸载数据*/
	public static final String ERR_UNLOADAGAIN            = "1866";
	/**数据已加载,不能重复加载数据*/
	public static final String ERR_LOADAGAIN              = "1867";
	/**卸载文件不存在*/
	public static final String ERR_NOBACKUPFILE           = "1868";
	/**取本机构所有上级机构内码失败*/
	public static final String ERR_GETUPBRANCHS           = "1869";
	/**汇总本机构所有上级机构数据失败*/
	public static final String ERR_HZUPBRANCHS            = "1870";
	/**SQL语句汇总失败*/
	public static final String ERR_SQLSTATISTIC           = "1871";
	/**初始化清算日志失败*/
	public static final String ERR_INITCLEARLOG           = "1872";
	/**数据库缺少配置*/
	public static final String ERR_DBNOSETTING            = "1873";
	/**账户类清算失败*/
	public static final String ERR_ACCCLEAR               = "1874";
	/**交易类清算失败*/
	public static final String ERR_TRANSCLEAR             = "1875";
	/**系统暂停,请稍后再试*/
	public static final String ERR_SYSSTOP                = "1876";
	/**上海资金对账错误*/
	public static final String ERR_FUNDSHA                = "1877";
	/**深圳资金对账错误*/
	public static final String ERR_FUNDSZA                = "1878";
	/**上海持仓对账错误*/
	public static final String ERR_STOCKSHA                = "1879";
	/**深圳持仓对账错误*/
	public static final String ERR_STOCKSZA                = "1880";

	/** 其他类(19XX)*/
	/**数据库错误,详看错误日志*/
	public static final String ERR_DBERR                  = "1900";
	/**查询数据库失败*/
	public static final String ERR_DBSELECT               = "1901";
	/**插入数据库失败*/
	public static final String ERR_DBINSERT               = "1902";
	/**更新数据库失败*/
	public static final String ERR_DBUPDATE               = "1903";
	/**删除记录失败*/
	public static final String ERR_DBDELETE               = "1904";
	/**查询无记录*/
	public static final String ERR_NORECORD               = "1905";
	
	/** 工作流异常 */
	public static final String ERR_WORKFLOWEXP               = "2000";

	/** 数据库错误 */
	public static final String ERR_SQLERR = "3103";
	/** 日期有误 */
	public static final String ERR_DATEERR = "3104";
	/** 交易请求报文格式错误 */
	public static final String ERR_REQFMTERR = "3105";
	/** 抛出其他异常 */
	public static final String ERR_OTHEREXP               = "4696";
	
	/**推送合同状态到资债系统出错*/
	public static final String ERR_DIRECT2BFM              = "1300";
	
	/**对账查询没有返回数据*/
	public static final String ERR_KEEPACCOUNT            = "1502";
	/**查询条件有误*/
	public static final String ERR_QUERY                  = "1500";
	
	
	/**查询时间不在可查询范围*/
	public static final String ERR_QUERYTIME              = "1501";
	
	/** 通用错误 */
	public static final String ERR_COMMERR = "0003";
}

