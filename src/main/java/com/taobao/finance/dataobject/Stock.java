package com.taobao.finance.dataobject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.taobao.finance.util.FetchUtil;

public class Stock implements Comparable<Stock> {
	private Float vrate;
	public Float getVrate() {
		return vrate;
	}
	public void setVrate(Float vrate) {
		this.vrate = vrate;
	}

	private Date goldenDate;

	public Date getGoldenDate() {
		return goldenDate;
	}
	public void setGoldenDate(Date goldenDate) {
		this.goldenDate = goldenDate;
	}

	private String code;
	private String name;
	private Float two = new Float(0);
	private Float three = new Float(0);
	private Float four = new Float(0);
	private Float five = new Float(0);
	private Float ten = new Float(0);
	private Float twenty = new Float(0);
	private Float month = new Float(0);
	private Float twoMonth = new Float(0);
	private String symbol;
	private String _2changes;
	private String _3changes;
	private String _4changes;
	private String _5changes;
	private String _10changes;
	private String _20changes;
	private String _30changes;
	private String _60changes;
	private String startPrice;
	private String endPrice;
	private String highPrice;
	private String lowPrice;
	private Float fundQuota;
	private Boolean statis;
	private int continueDate;
	private Boolean chongzhu=false;;
	private String info;
	private String position;
	private Boolean ting=false;
	
	
	public Boolean getTing() {
		return ting;
	}
	public void setTing(Boolean ting) {
		this.ting = ting;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	private Long tradeNum;
	
	public Long getTradeNum() {
		return tradeNum;
	}
	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
	}
	public Boolean getChongzhu() {
		return chongzhu;
	}
	public void setChongzhu(Boolean chongzhu) {
		this.chongzhu = chongzhu;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getContinueDate() {
		return continueDate;
	}
	public void setContinueDate(int continueDate) {
		this.continueDate = continueDate;
	}


	private Boolean canRong;
	private Float rongNum;
	
	public Boolean getCanRong() {
		return canRong;
	}
	public void setCanRong(Boolean canRong) {
		this.canRong = canRong;
	}


	public Float getRongNum() {
		return rongNum;
	}
	public void setRongNum(Float rongNum) {
		this.rongNum = rongNum;
	}


	private Date hardenDate;
	private String hardenType;
	private Float hardenMoney;
	private String hardenStore;
	private String hardenStoreCode;
	private Float hardenQuota;
	private Float tatalValue;
	
	public Date getHardenDate() {
		return hardenDate;
	}
	public String getHardenDateString() {
		return FetchUtil.FILE_FORMAT.format(hardenDate);
	}
	public void setHardenDate(Date hardenDate) {
		this.hardenDate = hardenDate;
	}
	public String getHardenType() {
		return hardenType;
	}
	public void setHardenType(String hardenType) {
		this.hardenType = hardenType;
	}
	public Float getHardenMoney() {
		return hardenMoney;
	}
	public String getHardenMoneyFormat() {
		return FetchUtil.formatExchange(hardenMoney);
	}
	public void setHardenMoney(Float hardenMoney) {
		this.hardenMoney = hardenMoney;
	}
	public String getHardenStore() {
		return hardenStore;
	}
	public void setHardenStore(String hardenStore) {
		this.hardenStore = hardenStore;
	}

	public String getHardenStoreCode() {
		return hardenStoreCode;
	}
	public void setHardenStoreCode(String hardenStoreCode) {
		this.hardenStoreCode = hardenStoreCode;
	}
	public Float getHardenQuota() {
		return hardenQuota;
	}
	public void setHardenQuota(Float hardenQuota) {
		this.hardenQuota = hardenQuota;
	}
	public Float getTatalValue() {
		return tatalValue;
	}
	public void setTatalValue(Float tatalValue) {
		this.tatalValue = tatalValue;
	}
	public Float getExchangeValue() {
		return exchangeValue;
	}
	public void setExchangeValue(Float exchangeValue) {
		this.exchangeValue = exchangeValue;
	}

	private Float exchangeValue;
	
	

	public Boolean isHarden(){
		if(getRate()>0.0995F){
			return true;
		}else{
			return false;
		}
	}
	public Boolean getStatis() {
		return statis;
	}

	public void setStatis(Boolean statis) {
		this.statis = statis;
	}

	public Float getFundQuota() {
		return fundQuota;
	}
	
	public boolean isChuang(){
		if(this.symbol.startsWith("sz300")){
			return true;
		}
		return false;
	}
	
	public boolean isZhong(){
		if(this.symbol.startsWith("sz002")){
			return true;
		}
		return false;
	}

	public void setFundQuota(Float fundQuota) {
		this.fundQuota = fundQuota;
	}

	public String getStartPrice1() {
		return startPrice1;
	}

	public void setStartPrice1(String startPrice1) {
		this.startPrice1 = startPrice1;
	}

	public String getEndPrice1() {
		return endPrice1;
	}

	public void setEndPrice1(String endPrice1) {
		this.endPrice1 = endPrice1;
	}

	public String getStartPrice2() {
		return startPrice2;
	}

	public void setStartPrice2(String startPrice2) {
		this.startPrice2 = startPrice2;
	}

	public String getEndPrice2() {
		return endPrice2;
	}

	public void setEndPrice2(String endPrice2) {
		this.endPrice2 = endPrice2;
	}

	public String getStartPrice3() {
		return startPrice3;
	}

	public void setStartPrice3(String startPrice3) {
		this.startPrice3 = startPrice3;
	}

	public String getEndPrice3() {
		return endPrice3;
	}

	public void setEndPrice3(String endPrice3) {
		this.endPrice3 = endPrice3;
	}

	public String getStartPrice4() {
		return startPrice4;
	}

	public void setStartPrice4(String startPrice4) {
		this.startPrice4 = startPrice4;
	}

	public String getEndPrice4() {
		return endPrice4;
	}

	public void setEndPrice4(String endPrice4) {
		this.endPrice4 = endPrice4;
	}
	
	public String getEndPrice30() {
		return endPrice30;
	}

	public void setEndPrice30(String endPrice30) {
		this.endPrice30 = endPrice30;
	}

	public Float getRate1() {
		return rate1;
	}

	public void setRate1(Float rate1) {
		this.rate1 = rate1;
	}

	public Float getRate2() {
		return rate2;
	}

	public void setRate2(Float rate2) {
		this.rate2 = rate2;
	}

	public Float getRate3() {
		return rate3;
	}

	public void setRate3(Float rate3) {
		this.rate3 = rate3;
	}

	public Float getRate4() {
		return rate4;
	}

	public void setRate4(Float rate4) {
		this.rate4 = rate4;
	}

	private String startPrice1;
	private String endPrice1;
	private String startPrice2;
	private String endPrice2;
	private String startPrice3;
	private String endPrice3;
	private String startPrice4;
	private String endPrice4;
	private String startPrice30;
	private String endPrice30;

	private Float realRate;
	public Float getRealRate() {
		return realRate;
	}

	public void setRealRate(Float realRate) {
		this.realRate = realRate;
	}

	private Float rate;
	private Float rate1;
	private Float rate2;
	private Float rate3;
	private Float rate4;
	private Float rate30;

	public void setRate30(Float rate30) {
		this.rate30 = rate30;
	}

	public String getStartPrice30() {
		return startPrice30;
	}

	public void setStartPrice30(String startPrice30) {
		this.startPrice30 = startPrice30;
	}

	public Float getRate30() {
		return rate30;
	}

	private String lastEndPrice;
	private Date date;
	private int hardenTimes;
	private boolean strictHarden;
	private boolean isRongquan;
	private Float totalRate;
	private int totalDay;
	
	private int holdByFound;

	public int getHoldByFound() {
		return holdByFound;
	}

	public void setHoldByFound(int holdByFound) {
		this.holdByFound = holdByFound;
	}

	public Float getTotalRate() {
		return totalRate;
	}
	
	
	public String getTotalRatePercent() {
		if(totalRate!=null){
			return FetchUtil.formatRatePercent(totalRate);
		}else{
			return "";
		}
	}
	
	public String getHardenQuotaPercent() {
		if(hardenQuota!=null){
			return FetchUtil.formatRatePercent(hardenQuota);
			
		}else{
			return "";
		}
	}
	

	public void setTotalRate(Float totalRate) {
		this.totalRate = totalRate;
	}

	public int getTotalDay() {
		return totalDay;
	}

	public void setTotalDay(int totalDay) {
		this.totalDay = totalDay;
	}

	public boolean isRongquan() {
		return isRongquan;
	}
	
	public String getRongquanString(){
		if(isRongquan){
			return "��ȯ";
		}else{
			return "";
		}
	}

	public void setRongquan(boolean isRongquan) {
		this.isRongquan = isRongquan;
	}

	public boolean isStrictHarden() {
		return strictHarden;
	}

	public void setStrictHarden(boolean strictHarden) {
		this.strictHarden = strictHarden;
	}

	public int getHardenTimes() {
		return hardenTimes;
	}

	public void setHardenTimes(int hardenTimes) {
		this.hardenTimes = hardenTimes;
	}

	public String getDateString() {
		if (date == null) {
			return "";
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(date);
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLastEndPrice() {
		return lastEndPrice;
	}

	public void setLastEndPrice(String lastEndPrice) {
		this.lastEndPrice = lastEndPrice;
	}

	public String getStartPrice() {
		return startPrice;
	}
	public Float getStartPriceFloat() {
		return Float.parseFloat(startPrice);
	}

	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}

	public String getEndPrice() {
		return endPrice;
	}
	public Float getEndPriceFloat() {
		return Float.parseFloat(this.getEndPrice());
	}

	public void setEndPrice(String endPrice) {
		this.endPrice = endPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public Float getHighPriceFloat() {
		return Float.parseFloat(highPrice);
	}
	
	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getLowPrice() {
		return lowPrice;
	}
	
	public Float getLowPriceFloat() {
		return Float.parseFloat(lowPrice);
	}
	

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Float getRate() {
		return rate;
	}

	public String getRateString() {
		if(rate!=null){
			return FetchUtil.formatRate(rate);
		}else{
			return "";
		}
	}
	
	public String getRatePercent() {
		if(rate!=null){
			return FetchUtil.formatRatePercent(rate);
		}else{
			return "";
		}
	}
	
	public String getRealRatePercent() {
		if(rate!=null){
			return FetchUtil.formatRatePercent(realRate);
		}else{
			return "";
		}
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public String get_10changes() {
		return _10changes;
	}

	public void set_10changes(String _10changes) {
		this._10changes = _10changes;
		try {
			Float value = Float.parseFloat(_10changes);
			this.ten = value;
		} catch (Exception e) {
		}
	}

	public String get_20changes() {
		return _20changes;
	}

	public void set_20changes(String _20changes) {
		this._20changes = _20changes;
		try {
			Float value = Float.parseFloat(_20changes);
			this.twenty = value;
		} catch (Exception e) {
		}
	}

	public String get_30changes() {
		return _30changes;
	}

	public void set_30changes(String _30changes) {
		this._30changes = _30changes;
		try {
			Float value = Float.parseFloat(_30changes);
			this.month = value;
		} catch (Exception e) {
		}
	}

	public String get_60changes() {
		return _60changes;
	}

	public void set_60changes(String _60changes) {
		this._60changes = _60changes;
		try {
			Float value = Float.parseFloat(_60changes);
			this.twoMonth = value;
		} catch (Exception e) {
		}
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String get_2changes() {
		return _2changes;
	}

	public void set_2changes(String _2changes) {
		this._2changes = _2changes;
		try {
			Float value = Float.parseFloat(_2changes);
			this.two = value;
		} catch (Exception e) {
		}
	}

	public String get_3changes() {
		return _3changes;
	}

	public void set_3changes(String _3changes) {
		this._3changes = _3changes;
		try {
			Float value = Float.parseFloat(_3changes);
			this.three = value;
		} catch (Exception e) {
		}
	}

	public String get_4changes() {
		return _4changes;
	}

	public void set_4changes(String _4changes) {
		this._4changes = _4changes;
		try {
			Float value = Float.parseFloat(_4changes);
			this.four = value;
		} catch (Exception e) {
		}
	}

	public String get_5changes() {
		return _5changes;
	}

	public void set_5changes(String _5changes) {
		this._5changes = _5changes;
		try {
			Float value = Float.parseFloat(_5changes);
			this.five = value;
		} catch (Exception e) {
		}
	}

	public String getCode() {
		if(this.symbol!=null){
			String s=symbol.replace("sh","");
			s=s.replace("sz", "");
			return s;
		}
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	
	public String getNameFormat() {
		if(name.getBytes().length==7){
			name=name+"&nbsp;&nbsp;&nbsp";
		}
		if(name.getBytes().length==6){
			name=name+"&nbsp;&nbsp;";
		}
		if(name.getBytes().length==9){
			name=name+"&nbsp;&nbsp;";
		}
		if(this.getSymbol().equals("sh000001")||this.getSymbol().equals("sz399001")||this.getSymbol().equals("sz399006")||this.getSymbol().equals("sz399101")){
			name="<b>"+name+"</b>";
		}
		return  name;
	}
	public String getNameEclipseFormat() {
		if(name.getBytes().length==7){
			name=name+" ";
		}
		if(name.getBytes().length==6){
			name=name+"  ";
		}
		return  name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getTwo() {
		return two;
	}

	public void setTwo(Float two) {
		this.two = two;
	}

	public Float getThree() {
		return three;
	}

	public void setThree(Float three) {
		this.three = three;
	}

	public Float getFour() {
		return four;
	}

	public void setFour(Float four) {
		this.four = four;
	}

	public Float getFive() {
		return five;
	}

	public void setFive(Float five) {
		this.five = five;
	}

	public Float getTen() {
		return ten;
	}

	public void setTen(Float ten) {
		this.ten = ten;
	}

	public Float getTwenty() {
		return twenty;
	}

	public void setTwenty(Float twenty) {
		this.twenty = twenty;
	}

	public Float getMonth() {
		return month;
	}

	public void setMonth(Float month) {
		this.month = month;
	}

	public Float getTwoMonth() {
		return twoMonth;
	}

	public void setTwoMonth(Float twoMonth) {
		this.twoMonth = twoMonth;
	}

	@Override
	public int compareTo(Stock o) {
		if (this.month - o.month > 0) {
			return -1;
		} else if (this.month - o.month < 0) {
			return 1;
		} else {
			if (this.twenty - o.twenty > 0) {
				return -1;
			} else if (this.twenty - o.twenty < 0) {
				return 1;
			} else {
				if (this.ten - o.ten > 0) {
					return -1;
				} else if (this.ten - o.ten < 0) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}

	public String toString() {

		if (getRate() == null) {
			return this.getDateString()+"-"+this.getSymbol() + "-" + this.getName() + "-"
			        + this.getHoldByFound()+"-"+this.isRongquan+"-"
					+ this.getRate() + "-" + this.getStartPrice() + "-"
					+ this.getHighPrice() + "-" + this.getLowPrice() + "-"
					+ this.getEndPrice();
		} else {
			return this.getDateString()+"-"+this.getSymbol() + "-" + this.getName() + "-"
			        + this.getHoldByFound()+"-"+this.isRongquan+"-"
					+ this.getRateString() + "-" + this.getStartPrice() + "-"
					+ this.getHighPrice() + "-" + this.getLowPrice() + "-"
					+ this.getEndPrice();
		}

	}
	
	public String toHistoryString() {
			return this.getDateString()+" " + this.getStartPrice() + " "
					+ this.getHighPrice() + " " + this.getLowPrice() + " "
					+ this.getEndPrice()+" "+this.getTradeNum();
	}
	
	public String toTmpString() {
		return this.getStartPrice() + " "
				+ this.getHighPrice() + " " + this.getLowPrice() + " "
				+ this.getEndPrice()+" "+this.getTradeNum();
}
	
	public String toStringCount() {

		return this.getSymbol()+"\t"+this.getHoldByFound();

	}

	public String toStringX() {
		return 
		this.getSymbol() + "\t" 
		+ this.getName() + "\t"
		+ this.getStartPrice() + "\t" 
		+ this.getEndPrice() + "\t"
		+ this.getStartPrice1() + "\t" 
		+ this.getEndPrice1() + "\t" 
		+ this.getStartPrice2() + "\t" 
		+ this.getEndPrice2() + "\t" 
		+ this.getStartPrice3() + "\t" 
		+ this.getEndPrice3() + "\t" 
		+ this.getStartPrice4() + "\t" 
		+ this.getEndPrice4() + "\t" 
		+ this.getRate() + "\t" 
		+ this.getRate1()+ "\t" 
		+ this.getRate2() + "\t" 
		+ this.getRate3() + "\t"
		+ this.getRate4() + "\t" 
		+ this.getTwoMonth() + "\t" 
		+ this.getMonth()+"\t"
		+ this.getHoldByFound()+"\t"
		+ this.getHardenTimes()+"\t"
		+ this.isStrictHarden()+"\t"
		+ this.isRongquan()+"\t"
		+this.getInfo();
	}

	public String toSymbolString() {

		return this.getSymbol();
	}
	
	public String toSymbolHotMoney() {

		return this.getSymbol()+"\t"
		       +this.getNameEclipseFormat()+"\t"
		       +this.getHardenDateString()+"\t"
		       +this.getHardenMoneyFormat()+"\t"
		       +this.getHardenQuotaPercent()+"\t"
		       +this.getHardenType();
		       
	}
	
	public String toSymbolAndNameString() {

		return this.getSymbol()+"\t"+this.getName();
	}


	public String toExamString() {
		return this.getSymbol() + "\t" + this.getRateString();
	}

	public String toHardenString() {
		if (isStrictHarden()) {
			return this.getSymbol() + "\t" + this.getName()
			+"\t"+this.getRateString() + "\t"
					+ this.getHardenTimes() + "\t������ͣ";
		} else {
			return this.getSymbol() + "\t" + this.getName()
			+"\t"+ this.getRateString() + "\t"
					+ this.getHardenTimes() + "\t��";
		}

	}

	public static class RateDescComparator implements Comparator<Stock> {

		@Override
		public int compare(Stock o1, Stock o2) {
			if ((o2.getRate() - o1.getRate()) > 0) {
				return 1;
			} else if ((o2.getRate() - o1.getRate()) == 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	public static class HardenTimesDescComparator implements Comparator<Stock> {

		@Override
		public int compare(Stock o1, Stock o2) {
			return o2.getHardenTimes() - o1.getHardenTimes();
		}
	}
	
	public static void mian(String args[]){
		System.out.println("中".getBytes().length);
		System.out.println("A".getBytes().length);
	}
}
