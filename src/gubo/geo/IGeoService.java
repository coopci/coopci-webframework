package gubo.geo;

public interface IGeoService {

	/**
	 * 从ip地址获取所在位置。
	 * @param ipaddr ip 地址
	 * @param level GeoLevel里面定义的级别。 
	 **/
	default String getLocationByIpAddr(String ipaddr, String level) {
		
		throw new UnsupportedOperationException();
	}
}
