package com.wangxy.devkit.business.market;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangxy.devkit.business.market.interfaces.IFxRealrate;
import com.wangxy.devkit.cache.CurrencyPairCache;
import com.wangxy.devkit.domain.bean.ReutersFxFwdSwapRealrate;
import com.wangxy.devkit.domain.bean.ReutersFxSpotRealrate;
import com.wangxy.devkit.domain.dao.ReutersFxFwdSwapRealrateDao;
import com.wangxy.devkit.domain.dao.ReutersFxSpotRealrateDao;
import com.wangxy.devkit.exception.CommonBusinessException;
import com.wangxy.devkit.kafka.consumer.ConsumerClient;
import com.wangxy.devkit.util.DateUtil;

/********************************************
 * 文件名称: FxRealrateImp.java
 * 系统名称: 资金风险管理系统
 * 模块名称: 外汇实时行情服务
 * 软件版权:
 * 功能说明:
 * 系统版本:
 * 开发人员: wangxy
 * 开发时间: 2018-5-14
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期 修改人员 修改说明
 *********************************************/
@Service
public class FxRealrateImp implements IFxRealrate {
	private static final String SWAP = "SWAP";
	private static final String FWD = "FWD";
	private static final String SPOT = "SPOT";
	private static Logger logger = LoggerFactory.getLogger(ConsumerClient.class);
	@Autowired
	private ReutersFxSpotRealrateDao reutersFxSpotRealrateDao;
	@Autowired
	private ReutersFxFwdSwapRealrateDao reutersFxForwardSwapRealrateDao;
	
	@Override
	public void impFxRealrate(ConsumerRecords<String, String> fxPriceRealTimeRecords) {
		for (ConsumerRecord<String, String> record : fxPriceRealTimeRecords) {
			logger.info(record.key() + ", " + record.value() + ", " + record.offset());
			String[] obj = record.value().split("===");
			String productName = obj[4];
			String cyPairName;
			
			
			try {
				if (SPOT.equals(productName)) {
					cyPairName = obj[5];
					cyPairName = cyPairName.substring(0, 3) + "/" + cyPairName.substring(3, 6);
					// 只要我们需要的货币对行情
					if (CurrencyPairCache.getInstance().getCurrencyPair(cyPairName, false) != null) {
						ReutersFxSpotRealrate fxSpotRealrate = new ReutersFxSpotRealrate();
						parseField4FxSpotRealrate(obj, fxSpotRealrate);

//					if (reutersFxSpotRealrateDao.locate(fxSpotRealrate) != null) {
//						reutersFxSpotRealrateDao.update(fxSpotRealrate);
//					} else {
							reutersFxSpotRealrateDao.add(fxSpotRealrate);
//					}
					}

				} else if (FWD.equals(productName) || SWAP.equals(productName)) {
					cyPairName = obj[6];
					cyPairName = cyPairName.substring(0, 3) + "/" + cyPairName.substring(3, 6);
					// 只要我们需要的货币对行情
					if (CurrencyPairCache.getInstance().getCurrencyPair(cyPairName, false) != null) {
						ReutersFxFwdSwapRealrate fxFwdSwapRealrate = new ReutersFxFwdSwapRealrate();
						parseField4FxFwdSwapRealrate(obj, fxFwdSwapRealrate);

//					if (reutersFxForwardSwapRealrateDao.locate(fxFwdSwapRealrate) != null) {
//						reutersFxForwardSwapRealrateDao.update(fxFwdSwapRealrate);
//					} else {
							reutersFxForwardSwapRealrateDao.add(fxFwdSwapRealrate);
//					}
					}

				} else {
					logger.info("不支持当前记录的导入。产品类型名称=" + productName + "：" + record.key() + ", " + record.value() + ", " + record.offset());

				}
			} catch (Exception e) {
				logger.error("实时行情数据导入失败!", e);
			}

		}

	}
	@Override
	public void impFxRealrate4Test(ConsumerRecords<String, String> fxPriceRealTimeRecords) {
		
		List<String> testData = getTestData();
		for (String string : testData) {
			
			try {
				// for (ConsumerRecord<String, String> record : fxPriceRealTimeRecords) {
				// logger.info(record.key() + ", " + record.value() + ", " + record.offset());
				// String[] obj = record.value().split("===");
				String[] obj = string.split("===");
				String productName = obj[4];
				String cyPairName;
				
				if (SPOT.equals(productName)) {
					cyPairName = obj[5];
					cyPairName = cyPairName.substring(0, 3) + "/" + cyPairName.substring(3, 6);
					// 只要我们需要的货币对行情
					if (CurrencyPairCache.getInstance().getCurrencyPair(cyPairName, false) != null) {
						ReutersFxSpotRealrate fxSpotRealrate = new ReutersFxSpotRealrate();
						parseField4FxSpotRealrate(obj, fxSpotRealrate);
						
						if (reutersFxSpotRealrateDao.locate(fxSpotRealrate) != null) {
							reutersFxSpotRealrateDao.update(fxSpotRealrate);
						} else {
							reutersFxSpotRealrateDao.add(fxSpotRealrate);
						}
					}
					
				} else if (FWD.equals(productName) || SWAP.equals(productName)) {
					cyPairName = obj[6];
					cyPairName = cyPairName.substring(0, 3) + "/" + cyPairName.substring(3, 6);
					// 只要我们需要的货币对行情
					if (CurrencyPairCache.getInstance().getCurrencyPair(cyPairName, false) != null) {
						ReutersFxFwdSwapRealrate fxFwdSwapRealrate = new ReutersFxFwdSwapRealrate();
						parseField4FxFwdSwapRealrate(obj, fxFwdSwapRealrate);
						
						if (reutersFxForwardSwapRealrateDao.locate(fxFwdSwapRealrate) != null) {
							reutersFxForwardSwapRealrateDao.update(fxFwdSwapRealrate);
						} else {
							reutersFxForwardSwapRealrateDao.add(fxFwdSwapRealrate);
						}
					}
					
				} else {
					// logger.info("不支持当前记录的导入。产品类型名称=" + productName + "：" + record.key() + ", " + record.value() + ", " + record.offset());
					logger.info("不支持当前记录的导入。产品类型名称=" + productName);
					
				}
				
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("问题语句=="+string);
				e.printStackTrace();
				throw new CommonBusinessException("guale");
			}
			
			
		}
		
	}

	private List<String> getTestData() {
		List<String> test = new ArrayList<>();
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===3Y===SWAP===SWAP===AUDUSD===4===86400===Y===105.00===105.0===125.00===003617===4===R===4===20191112===20221114===0.6855===Y===T===RMDS===20191109===0===2===663923===0.6853===125.0===1===115.000000===115.000000===0.685400===105.0===125.0===115.000000===105.0===125.0===115.000000===105.0===125.0===115.000000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===SW===SWAP===SWAP===AUDUSD===4===86400===Y===1.02===1.02===1.21===003617===4===R===4===20191112===20191119===0.6855===Y===T===RMDS===20191109===0===2===571893===0.6853===1.21===1===1.115000===1.115000===0.685400===1.02===1.21===1.115000===1.02===1.21===1.115000===1.02===1.21===1.115000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");

		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===3M===SWAP===SWAP===CADCNY===4===86400===Y===144.56===144.56===150.60===003617===4===R===4===20191113===20200213===5.2959===Y===T===CROSS===20191109===0,0===2===942011===5.2907===150.6===1===147.580000===147.580000===5.293300===144.56===150.6===147.580000===144.56===150.6===147.580000===144.56===150.6===147.580000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===ON===SWAP===SWAP===CADCNY===4===86400===Y===4.09===4.09===12.91===003617===4===R===4===20191109===20191112===5.2959===Y===T===CROSS===20191109===0,0===2===918253===5.2907===12.91===1===8.500000===8.500000===5.293300===4.09===12.91===8.500000===4.09===12.91===8.500000===4.09===12.91===8.500000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===3W===SWAP===SWAP===EURAUD===4===86400===Y===12.55===12.55===13.01===003617===4===R===4===20191112===20191203===1.6083===Y===T===CROSS===20191109===0,0===2===979304===1.6076===13.01===1===12.780000===12.780000===1.607950===12.55===13.01===12.780000===12.55===13.01===12.780000===12.55===13.01===12.780000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===SN===SWAP===SWAP===EURAUD===4===86400===Y===0.62===0.62===0.76===003617===4===R===4===20191112===20191113===1.6083===Y===T===CROSS===20191109===0,0===2===789248===1.6076===0.76===1===0.690000===0.690000===1.607950===0.62===0.76===0.690000===0.62===0.76===0.690000===0.62===0.76===0.690000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:28===23===2Y===SWAP===SWAP===EURCAD===4===86400===Y===653.78===653.78===671.67===003627===4===R===4===20191113===20211115===1.4575===Y===R===CROSS===20191109===0,0===2===2495750===1.4558===671.67===1===662.725000===662.725000===1.456650===653.78===671.67===662.725000===653.78===671.67===662.725000===653.78===671.67===662.725000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:28===23===9M===SWAP===SWAP===EURCAD===4===86400===Y===245.32===245.32===248.55===003628===4===R===4===20191113===20200813===1.4575===Y===R===CROSS===20191109===0,0===2===4145617===1.4558===248.55===1===246.935000===246.935000===1.456650===245.32===248.55===246.935000===245.32===248.55===246.935000===245.32===248.55===246.935000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===2M===SWAP===SWAP===EURCNY===4===86400===Y===462.10===462.1===472.17===003617===4===R===4===20191112===20200113===7.7121===Y===T===CROSS===20191109===0,0===2===1268680===7.7084===472.17===1===467.135000===467.135000===7.710250===462.1===472.17===467.135000===462.1===472.17===467.135000===462.1===472.17===467.135000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:28===23===7M===SWAP===SWAP===EURCNY===4===86400===Y===1364.78===1364.78===1386.25===003628===4===R===4===20191112===20200612===7.7121===Y===R===CROSS===20191109===0,0===2===2085877===7.7084===1386.25===1===1375.515000===1375.515000===7.710250===1364.78===1386.25===1375.515000===1364.78===1386.25===1375.515000===1364.78===1386.25===1375.515000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");
		test.add(
				"2019-11-09===2019-11-09 00:36:18===23===1M===SWAP===SWAP===EURGBP===4===86400===Y===8.17===8.17===9.60===003617===4===R===4===20191112===20191212===0.8621===Y===R===CROSS===20191109===0,0===2===1412893===0.8612===9.6===1===8.885000===8.885000===0.861650===8.17===9.6===8.885000===8.17===9.6===8.885000===8.17===9.6===8.885000===RC_BC_NEW_SWAP_INFO===NEW_SWAP_INFO===2019-11-09===2019-11-09 00:36:43");

		test.add(
				"2019-11-09===2019-11-09 02:30:08===17===SPOT===SPOT===AGYCNY===2===Y===86400===4055.00===4055.0===4065.00===023003===R===20191113===R===RMDS===0===20191109===782543===2===4065.0===1===4060.000000===4060.000000===4054.95===4065.05===4060.000000===4054.9===4065.1===4060.000000===4054.8===4065.2===4060.000000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30         ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===CHFCNY===4===Y===86400===7.0122===7.0122===7.0163===083706===R===20191112===R===CROSS===0,0===20191109===2795590===4===7.0163===1===7.014250===7.014250===7.0091===7.0194===7.014250===6.9865===7.042===7.014250===6.7845===7.042===6.913250===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                          ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===AUDCNY===4===Y===86400===4.7981===4.7981===4.8018===083706===R===20191112===R===CROSS===0,0===20191109===3518240===4===4.8018===1===4.799950===4.799950===4.7953===4.8046===4.799950===4.7789===4.821===4.799950===4.6365===4.821===4.728750===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                          ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===DKKCNY===4===Y===86400===1.0317===1.0317===1.0319===083706===R===20191112===R===CROSS===0,0===20191109===4902995===4===1.0319===1===1.031800===1.031800===1.0297===1.0339===1.031800===1.0281===1.0355===1.031800===0.9998===1.0355===1.017650===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                        ");
		test.add(
				"2019-11-09===2019-11-09 02:29:48===17===SPOT===SPOT===AUYCNY===2===Y===86400===329.80===329.8===329.90===022948===R===20191112===R===RMDS===0===20191109===1047026===2===329.9===1===329.850000===329.850000===329.75===329.95===329.850000===329.7===330.0===329.850000===329.6===330.1===329.850000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                       ");
		test.add(
				"2019-11-09===2019-11-09 00:00:13===17===SPOT===SPOT===AUXCNY===2===Y===86400===320.06===320.06===339.86===000010===R===20191112===C===RMDS===0===20191109===509232===2===339.86===1===329.960000===329.960000===320.01===339.91===329.960000===319.96===339.96===329.960000===319.86===340.06===329.960000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                  ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===EURSGD===4===Y===86400===1.4970===1.497===1.4986===060000===R===20191112===R===CROSS===0,0===20191109===5025803===4===1.4986===1===1.497800===1.497800===1.4965===1.4991===1.497800===1.496===1.4996===1.497800===1.495===1.5006===1.497800===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===HKDCNY===4===Y===86400===0.89373===0.89373===0.89393===083706===R===20191112===R===CROSS===0,0===20191109===1332862===5===0.89393===1===0.893830===0.893830===0.89353===0.89413===0.893830===0.89213===0.89553===0.893830===0.88593===0.89553===0.890730===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30              ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===USDCNY===4===Y===86400===6.9954===6.9954===6.9967===083706===R===20191112===R===CMDS===0===20191109===874271===4===6.9967===1===6.996050===6.996050===6.9954===6.9982===6.996800===6.9839===7.0138===6.998850===6.9339===7.0138===6.973850===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                            ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===USDNOK===4===Y===86400===9.1311===9.1311===9.1341===060000===R===20191112===R===RMDS===0===20191109===2690412===4===9.1341===1===9.132600===9.132600===9.1306===9.1346===9.132600===9.1301===9.1351===9.132600===9.1291===9.1361===9.132600===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 06:00:13===17===SPOT===SPOT===GBPUSD===4===Y===86400===1.2768===1.2768===1.2773===060009===R===20191112===R===RMDS===0===20191109===5562365===4===1.2773===1===1.277050===1.277050===1.2763===1.2778===1.277050===1.2758===1.2783===1.277050===1.2748===1.2793===1.277050===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===NZDCNY===4===Y===86400===4.4267===4.4267===4.4303===083706===R===20191112===R===CROSS===0,0===20191109===2296878===4===4.4303===1===4.428500===4.428500===4.4229===4.4341===4.428500===4.4087===4.4483===4.428500===4.2567===4.4483===4.352500===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                        ");
		test.add(
				"2019-11-09===2019-11-09 05:59:53===17===SPOT===SPOT===USDCNH===4===Y===86400===6.9857===6.9857===6.9897===055950===R===20191112===R===RMDS===0===20191109===4571566===4===6.9897===1===6.987700===6.987700===6.9852===6.9902===6.987700===6.9847===6.9907===6.987700===6.9837===6.9917===6.987700===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===ZARCNY===4===Y===86400===0.4704===0.4704===0.4711===083706===R===20191112===R===CROSS===0,0===20191109===3097244===4===0.4711===1===0.470750===0.470750===0.4699===0.4716===0.470750===0.4694===0.4721===0.470750===0.4684===0.4731===0.470750===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                        ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===EURJPY===2===Y===86400===120.37===120.37===120.45===060000===R===20191112===R===CROSS===0,0===20191109===7939750===2===120.45===1===120.410000===120.410000===120.32===120.5===120.410000===120.27===120.55===120.410000===120.17===120.65===120.410000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30               ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===MOPCNY===4===Y===86400===0.8669===0.8669===0.8682===083706===R===20191113===R===CROSS===0,0===20191109===913990===4===0.8682===1===0.867550===0.867550===0.8662===0.8689===0.867550===0.8653===0.8698===0.867550===0.8584===0.8698===0.864100===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                         ");
		test.add(
				"2019-11-09===2019-11-09 05:50:18===17===SPOT===SPOT===USDKRW===2===Y===86400===1158.60===1158.6===1158.92===055016===R===20191112===R===RMDS===0===20191109===956788===2===1158.92===1===1158.760000===1158.760000===1158.55===1158.97===1158.760000===1158.5===1159.02===1158.760000===1158.4===1159.12===1158.760000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30      ");
		test.add(
				"2019-11-09===2019-11-09 05:59:33===17===SPOT===SPOT===USDZAR===4===Y===86400===14.8511===14.8511===14.8700===055931===R===20191112===R===RMDS===0===20191109===2723104===4===14.87===1===14.860550===14.860550===14.8506===14.8705===14.860550===14.8501===14.871===14.860550===14.8491===14.872===14.860550===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===GBPCNY===4===Y===86400===8.9317===8.9317===8.9369===083706===R===20191112===R===CROSS===0,0===20191109===5936508===4===8.9369===1===8.934300===8.934300===8.9281===8.9405===8.934300===8.8991===8.9695===8.934300===8.6135===8.9695===8.791500===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                        ");
		test.add(
				"2019-11-09===2019-11-09 05:59:48===17===SPOT===SPOT===USDCAD===4===Y===86400===1.3228===1.3228===1.3229===055944===R===20191112===R===RMDS===0===20191109===3709837===4===1.3229===1===1.322850===1.322850===1.3223===1.3234===1.322850===1.3218===1.3239===1.322850===1.3208===1.3249===1.322850===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 00:00:13===17===SPOT===SPOT===USDKZT===4===Y===86400===387.7500===387.75===389.7500===000010===R===20191112===C===RMDS===0===20191109===516762===4===389.75===1===388.750000===388.750000===387.7495===389.7505===388.750000===387.749===389.751===388.750000===387.748===389.752===388.750000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30      ");
		test.add(
				"2019-11-09===2019-11-09 06:05:08===17===SPOT===SPOT===XAGUSD===3===N===86400===16.797===16.797===16.817===060505===R===20191112===R===RMDS===0===20191109===1459368===3===16.817===1===16.807000===16.807000===16.792===16.822===16.807000===16.787===16.827===16.807000===16.777===16.837===16.807000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                      ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===JPYCNY===4===Y===86400===6.4002===6.4002===6.4031===083706===R===20191112===R===CROSS===0,0===20191109===4992348===4===6.4031===100===6.401650===6.401650===6.3974===6.4059===6.401650===6.3801===6.4232===6.401650===6.2254===6.4232===6.324300===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                      ");
		test.add(
				"2019-11-09===2019-11-09 05:59:58===17===SPOT===SPOT===USDHKD===4===Y===86400===7.8269===7.8269===7.8272===055957===R===20191112===R===RMDS===0===20191109===958717===4===7.8272===1===7.827050===7.827050===7.8264===7.8277===7.827050===7.8259===7.8282===7.827050===7.8249===7.8292===7.827050===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                            ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===USDSEK===4===Y===86400===9.6978===9.6978===9.7008===060000===R===20191112===R===RMDS===0===20191109===3480716===4===9.7008===1===9.699300===9.699300===9.6973===9.7013===9.699300===9.6968===9.7018===9.699300===9.6958===9.7028===9.699300===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===EURCNY===4===Y===86400===7.7061===7.7061===7.7104===083706===R===20191112===R===CROSS===0,0===20191109===4195813===4===7.7104===1===7.708250===7.708250===7.7029===7.7136===7.708250===7.6828===7.7337===7.708250===7.467===7.7337===7.600350===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                         ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===EURUSD===4===Y===86400===1.1016===1.1016===1.1020===060000===R===20191112===R===RMDS===0===20191109===3821670===4===1.102===1===1.101800===1.101800===1.1011===1.1025===1.101800===1.1006===1.103===1.101800===1.0996===1.104===1.101800===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                              ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===NZDUSD===4===Y===86400===0.6328===0.6328===0.6332===060000===R===20191112===R===RMDS===0===20191109===1922737===4===0.6332===1===0.633000===0.633000===0.6323===0.6337===0.633000===0.6318===0.6342===0.633000===0.6308===0.6352===0.633000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===USDDKK===4===Y===86400===6.7804===6.7804===6.7805===055959===R===20191112===R===RMDS===0===20191109===4528857===4===6.7805===1===6.780450===6.780450===6.7799===6.781===6.780450===6.7794===6.7815===6.780450===6.7784===6.7825===6.780450===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                            ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===EURAUD===4===Y===86400===1.6051===1.6051===1.6066===060000===R===20191112===R===CROSS===0,0===20191109===6465661===4===1.6066===1===1.605850===1.605850===1.6046===1.6071===1.605850===1.6041===1.6076===1.605850===1.6031===1.6086===1.605850===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                        ");
		test.add(
				"2019-11-09===2019-11-09 08:37:08===17===SPOT===SPOT===NOKCNY===4===Y===86400===0.7659===0.7659===0.7662===083706===R===20191112===R===CROSS===0,0===20191109===3064551===4===0.7662===1===0.766050===0.766050===0.7641===0.768===0.766050===0.7628===0.7693===0.766050===0.7387===0.7693===0.754000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                         ");
		test.add(
				"2019-11-09===2019-11-09 06:00:03===17===SPOT===SPOT===USDCHF===4===Y===86400===0.9972===0.9972===0.9976===060000===R===20191112===R===RMDS===0===20191109===2421449===4===0.9976===1===0.997400===0.997400===0.9967===0.9981===0.997400===0.9962===0.9986===0.997400===0.9952===0.9996===0.997400===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                           ");
		test.add(
				"2019-11-09===2019-11-09 01:33:43===17===SPOT===SPOT===USDMOP===4===Y===86400===8.0590===8.059===8.0690===013339===R===20191113===R===RMDS===0===20191109===539850===4===8.069===1===8.064000===8.064000===8.0585===8.0695===8.064000===8.058===8.07===8.064000===8.057===8.071===8.064000===RC_BC_NEW_SPOTCOM_INFO===NEW_SPOTCOM_INFO===2019-11-09===2019-11-09 11:15:30                                   ");
		return test;
		
	}

	private void parseField4FxSpotRealrate(String[] obj, ReutersFxSpotRealrate fxSpotRealrate) {
		//数量
		String countnum = obj[2];
		//牌价类型
		String quotetype = obj[3];
		//产品名称
		String productname = obj[4];
		//货币对
		String cypairname = obj[5];
		//基点位数
		int basepoint = Integer.valueOf(obj[6]);
		//是否可交易标识
		String trademark = obj[7];
		//报价有效时间
		String quotettl = obj[8];
		//升贴水买价（核心管理价格数据）
		double bid = Double.valueOf(obj[9]);
		//升贴水买价(源价格数据)
		double sourcebid = Double.valueOf(obj[10]);
		//升贴水卖价（核心管理价格数据）
		double ask = Double.valueOf(obj[11]);
		//牌价时间
		int updatetime = Integer.valueOf(obj[12]);
		//价格开停价状态
		String runstate = obj[13];
		//起息日
		int valuedate = Integer.valueOf(obj[14]);
		//价格类型
		String pricetype = obj[15];
		//价源代码
		String feedcode = obj[16];
		//原始数据实际外部流水号
		String datasourceid = obj[17];
		//牌价日期
		int updatedate = Integer.valueOf(obj[18]);
		//牌价批次号
		String priceid = obj[19];
		//小数有效位数
		int ticksize = Integer.valueOf(obj[20]);
		//升贴水卖价(源价格数据)
		double sourceask = Double.valueOf(obj[21]);
		//报价单位
		int quoteunit = Integer.valueOf(obj[22]);
		//升贴水中间价(源价格数据)
		double sourcemid = Double.valueOf(obj[23]);
		//升贴水中间价（核心管理价格数据）
		double mid = Double.valueOf(obj[24]);
		//成本买入价
		double costbid = Double.valueOf(obj[25]);
		//成本卖出价
		double costask = Double.valueOf(obj[26]);
		//成本中间价
		double costmid = Double.valueOf(obj[27]);
		//现汇买入价
		double exchbid = Double.valueOf(obj[28]);
		//现汇卖出价
		double exchask = Double.valueOf(obj[29]);
		//现汇中间价
		double exchmid = Double.valueOf(obj[30]);
		//现钞买入价
		double cashbid = Double.valueOf(obj[31]);
		//现钞卖出价
		double cashask = Double.valueOf(obj[32]);
		//现钞卖中间价
		double cashmid = Double.valueOf(obj[33]);
		//导入日期
		int impdate = DateUtil.getSysDate();
		//导入时间
		int imptime = DateUtil.getSysTime();
		
		fxSpotRealrate.setCypairname(cypairname);
		long datetime = ((long)updatedate)*1000000+updatetime;
		fxSpotRealrate.setUpdatedatetime(datetime);
		
		fxSpotRealrate.setAsk(ask);
		fxSpotRealrate.setBasepoint(basepoint);
		fxSpotRealrate.setBid(bid);
		fxSpotRealrate.setCashask(cashask);
		fxSpotRealrate.setCashbid(cashbid);
		fxSpotRealrate.setCashmid(cashmid);
		fxSpotRealrate.setCostask(costask);
		fxSpotRealrate.setCostbid(costbid);
		fxSpotRealrate.setCostmid(costmid);
		fxSpotRealrate.setCountnum(countnum);
		fxSpotRealrate.setDatasourceid(datasourceid);
		fxSpotRealrate.setExchask(exchask);
		fxSpotRealrate.setExchbid(exchbid);
		fxSpotRealrate.setExchmid(exchmid);
		fxSpotRealrate.setFeedcode(feedcode);
		fxSpotRealrate.setMid(mid);
		fxSpotRealrate.setPriceid(priceid);
		fxSpotRealrate.setPricetype(pricetype);
		fxSpotRealrate.setProductname(productname);
		fxSpotRealrate.setQuotettl(quotettl);
		fxSpotRealrate.setQuotetype(quotetype);
		fxSpotRealrate.setQuoteunit(quoteunit);
		fxSpotRealrate.setRunstate(runstate);
		fxSpotRealrate.setSourceask(sourceask);
		fxSpotRealrate.setSourcebid(sourcebid);
		fxSpotRealrate.setSourcemid(sourcemid);
		fxSpotRealrate.setTicksize(ticksize);
		fxSpotRealrate.setTrademark(trademark);
		fxSpotRealrate.setValuedate(valuedate);
		
		fxSpotRealrate.setImpdate(impdate);
		fxSpotRealrate.setImptime(imptime);
	}
	
	private void parseField4FxFwdSwapRealrate(String[] obj, ReutersFxFwdSwapRealrate fxFwdSwapRealrate) {
		//保留字段域个数
		String countnum = obj[2];
		//期限类型
		String periodcode = obj[3];
		//牌价类型
		String quotetype = obj[4];
		//产品名称
		String productname = obj[5];
		//货币对
		String cypairname = obj[6];
		//基点位数
		int basepoint = Integer.valueOf(obj[7]);
		//报价有效时间
		String quotettl = obj[8];
		//是否可交易标识
		String trademark = obj[9];
		//升贴水买价（核心管理价格数据）
		double bid = Double.valueOf(obj[10]);
		//升贴水买价(源价格数据)
		double sourcebid = Double.valueOf(obj[11]);
		//升贴水卖价（核心管理价格数据）
		double ask = Double.valueOf(obj[12]);
		//牌价时间
		int updatetime = Integer.valueOf(obj[13]);
		//即期精度位数
		int spotticksize = Integer.valueOf(obj[14]);
		//价格开停价状态
		String runstate = obj[15];
		//远期全价精度位数
		int fwdticksize = Integer.valueOf(obj[16]);
		//起息日
		int valuedate = Integer.valueOf(obj[17]);
		//到期日
		int maturitydate = Integer.valueOf(obj[18]);
		//对应即期卖出价
		double spotask = Double.valueOf(obj[19]);
		//即期可交易标识
		String spottrademark = obj[20];
		//价格类型
		String pricetype = obj[21];
		//价源代码
		String feedcode = obj[22];
		//牌价日期
		int updatedate = Integer.valueOf(obj[23]);
		//原始数据实际外部流水号
		String datasourceid = obj[24];
		//升贴水精度位数
		int swapticksize = Integer.valueOf(obj[25]);
		//牌价批次号
		String priceid = obj[26];
		//对应即期买入价
		double spotbid = Double.valueOf(obj[27]);
		//升贴水卖价(源价格数据)
		double sourceask = Double.valueOf(obj[28]);
		//报价单位
		int quoteunit = Integer.valueOf(obj[29]);
		//升贴水中间价(源价格数据)
		double sourcemid = Double.valueOf(obj[30]);
		//升贴水中间价（核心管理价格数据）
		double mid = Double.valueOf(obj[31]);
		//对应即期中间价
		double spotmid = Double.valueOf(obj[32]);
		//成本买入价
		double costbid = Double.valueOf(obj[33]);
		//成本卖出价
	  	double costask = Double.valueOf(obj[34]);
	  	//成本中间价
	  	double costmid = Double.valueOf(obj[35]);
	  	//现汇买入价
	  	double exchbid = Double.valueOf(obj[36]);
	  	//现汇卖出价
	  	double exchask = Double.valueOf(obj[37]);
	  	//现汇中间价
	  	double exchmid = Double.valueOf(obj[38]);
	  	//现钞买入价
	  	double cashbid = Double.valueOf(obj[39]);
	  	//现钞卖出价
	  	double cashask = Double.valueOf(obj[40]);
	  	//现钞卖中间价
	  	double cashmid = Double.valueOf(obj[41]);
		//导入日期
		int impdate = DateUtil.getSysDate();
		//导入时间
		int imptime = DateUtil.getSysTime();
		
		fxFwdSwapRealrate.setCypairname(cypairname);
		fxFwdSwapRealrate.setPeriodcode(periodcode);
		long datetime = ((long)updatedate)*1000000+updatetime;
		fxFwdSwapRealrate.setUpdatedatetime(datetime);
		
		fxFwdSwapRealrate.setAsk(ask);
		fxFwdSwapRealrate.setBasepoint(basepoint);
		fxFwdSwapRealrate.setBid(bid);
		fxFwdSwapRealrate.setCashask(cashask);
		fxFwdSwapRealrate.setCashbid(cashbid);
		fxFwdSwapRealrate.setCashmid(cashmid);
		fxFwdSwapRealrate.setCostask(costask);
		fxFwdSwapRealrate.setCostbid(costbid);
		fxFwdSwapRealrate.setCostmid(costmid);
		fxFwdSwapRealrate.setCountnum(countnum);
		fxFwdSwapRealrate.setDatasourceid(datasourceid);
		fxFwdSwapRealrate.setExchask(exchask);
		fxFwdSwapRealrate.setExchbid(exchbid);
		fxFwdSwapRealrate.setExchmid(exchmid);
		fxFwdSwapRealrate.setFeedcode(feedcode);
		fxFwdSwapRealrate.setFwdticksize(fwdticksize);
		fxFwdSwapRealrate.setMid(mid);
		fxFwdSwapRealrate.setMaturitydate(maturitydate);
		fxFwdSwapRealrate.setPriceid(priceid);
		fxFwdSwapRealrate.setPricetype(pricetype);
		fxFwdSwapRealrate.setProductname(productname);
		fxFwdSwapRealrate.setQuotettl(quotettl);
		fxFwdSwapRealrate.setQuotetype(quotetype);
		fxFwdSwapRealrate.setQuoteunit(quoteunit);
		fxFwdSwapRealrate.setRunstate(runstate);
		fxFwdSwapRealrate.setSourceask(sourceask);
		fxFwdSwapRealrate.setSourcebid(sourcebid);
		fxFwdSwapRealrate.setSourcemid(sourcemid);
		fxFwdSwapRealrate.setSpotask(spotask);
		fxFwdSwapRealrate.setSpotbid(spotbid);
		fxFwdSwapRealrate.setSpotmid(spotmid);
		fxFwdSwapRealrate.setSpotticksize(spotticksize);
		fxFwdSwapRealrate.setSpottrademark(spottrademark);
		fxFwdSwapRealrate.setSwapticksize(swapticksize);
		fxFwdSwapRealrate.setTrademark(trademark);
		fxFwdSwapRealrate.setValuedate(valuedate);
		
		fxFwdSwapRealrate.setImpdate(impdate);
		fxFwdSwapRealrate.setImptime(imptime);
	}
}
