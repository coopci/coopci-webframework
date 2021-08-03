package springless.geo;

public interface IGeoService {

	/**
	 * 从ip地址获取所在位置。
	 * @param ipaddr ip 地址
	 * @param level GeoLevel里面定义的级别。 
	 **/
	default String getLocationByIpAddr(String ipaddr, String level) {
		if (ipaddr.startsWith("127.")) {
			return "local"; // 本地开发用，就算没连网也能返回。
		}
		throw new UnsupportedOperationException(this.getClass() + " not implemented for " + ipaddr);
	}
}
