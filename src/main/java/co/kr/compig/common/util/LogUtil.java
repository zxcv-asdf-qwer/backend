package co.kr.compig.common.util;

import co.kr.compig.common.code.AgentCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 로그인, 접속 로그 입력 시 필요한 정보 제공
 */
public class LogUtil {

	/**
	 * 클라이언트 사용자의 IP 가져오기
	 *
	 * @return
	 */
	public static String getUserIp() {

		String ip = null;
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	/**
	 * 접속 사이트 정보를 넘긴다.
	 *
	 * @param request
	 * @return
	 */
	public static Long getSiteId(HttpServletRequest request) {
		String header = request.getHeader("X-Site-Id");
		if (!StringUtils.hasLength(header)) {
			return null;
		}
		return Long.valueOf(header);
	}

	public static AgentCode getAgentCode(HttpServletRequest request) {
		if (request == null) {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			request = attr.getRequest();
		}

		String header = org.apache.commons.lang3.StringUtils.defaultIfBlank(request.getHeader("User-Agent"), "");
		String userAgent = header.toUpperCase();

		//mobile
		if (userAgent.indexOf("MOBI") > -1) {
			if (userAgent.indexOf("ANDROID") > -1) {
				return AgentCode.ANDROID;
			} else if (userAgent.indexOf("IPHONE") > -1 || userAgent.indexOf("IPAD") > -1
					|| userAgent.indexOf("IPOD") > -1) {
				return AgentCode.IOS;
			}
			return AgentCode.MOBILE;

		} else {
			if(userAgent.indexOf("TRIDENT") > -1) {												// IE
				return AgentCode.IE;
			} else if(userAgent.indexOf("EDGE") > -1 || userAgent.indexOf("EDG") > -1) {		// Edge
				return AgentCode.EDGE;
			} else if(userAgent.indexOf("WHALE") > -1) { 										// Naver Whale
				return AgentCode.WHALE;
			} else if(userAgent.indexOf("OPERA") > -1 || userAgent.indexOf("OPR") > -1) { 		// Opera
				return AgentCode.OPERA;
			} else if(userAgent.indexOf("FIREFOX") > -1) { 										 // Firefox
				return AgentCode.FIREFOX;
			} else if(userAgent.indexOf("SAFARI") > -1 && userAgent.indexOf("CHROME") == -1 ) {	 // Safari
				return AgentCode.SAFARI;
			} else if(userAgent.indexOf("CHROME") > -1) {										 // Chrome
				return AgentCode.CHROME;
			}
		}

		return null;
	}
}
