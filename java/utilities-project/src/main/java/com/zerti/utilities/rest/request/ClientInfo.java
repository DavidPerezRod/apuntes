package com.zerti.utilities.rest.request;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.zerti.utilities.message.ZertiMessageResolver;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ClientInfo {

	@Autowired
	ZertiMessageResolver zertiMessageResolver;
	
    private String ipAddress;
    
    private int port;

    private String browser;

    private String os;

    private String referer;

    private String userAgent;
    
    private String city;
    
    private String country;
    
    private String geoLocation;

    public ClientInfo(HttpServletRequest request) throws LocationException {
        this.getClientReferer(request);
        this.getClientIpAddr(request);
        this.getClientOS(request);
        this.getClientBrowser(request);
        this.getClientUserAgent(request);
        this.getLocation();
        this.getIpPort(request);
    }
    
    public ClientInfo(Header[] headers) {
        this.getClientIpAddr(headers);
    }

    private void getClientReferer(HttpServletRequest request) {
        this.referer = request.getHeader(HttpHeaders.REFERER);
    }


    private void getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        this.ipAddress = ip;
    }
    
    
    private void getClientIpAddr(Header[] headers) {
    	Map<String, String> map = IntStream.range(0, headers.length)
    	        .collect(HashMap::new, (m,i)->m.put(headers[i].getName(), headers[i].getValue()), Map::putAll);

        String ip = map.get("X-Forwarded-For");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = map.get("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = map.get("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = map.get("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = map.get("HTTP_X_FORWARDED_FOR");
        }
        this.ipAddress = ip;
    }

    private void getClientOS(HttpServletRequest request) {
        final String browserDetails = request.getHeader(HttpHeaders.USER_AGENT);

        String clientOS = "UnKnown, More-Info: " + browserDetails;
        final String lowerCaseBrowser = browserDetails.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            clientOS = "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            clientOS = "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            clientOS = "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            clientOS = "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            clientOS = "IPhone";
        }
        this.os = clientOS;
    }

    private void getClientBrowser(HttpServletRequest request) {
        final String browserDetails = request.getHeader(HttpHeaders.USER_AGENT);
        final String user = browserDetails.toLowerCase();

        String clientBrowser = "";

        if (user.contains("msie")) {
            String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
            clientBrowser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else {
        	if(browserDetails.contains("Version")) {
            final String[] split = browserDetails.substring(
                    browserDetails.indexOf("Version")).split(" ");
	            if (user.contains("safari") && user.contains("version")) {
	                clientBrowser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
	                        "/")[0] + "-" + (split[0]).split("/")[1];
	            } else if (user.contains("opr") || user.contains("opera")) {
	                if (user.contains("opera"))
	                    clientBrowser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
	                            "/")[0] + "-" + (split[0]).split("/")[1];
	                else if (user.contains("opr"))
	                    clientBrowser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
	                            "-")).replace(
	                            "OPR", "Opera");
	            }
            } else if (user.contains("chrome")) {
                clientBrowser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if (user.contains("firefox")) {
                clientBrowser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if (user.contains("rv")) {
                clientBrowser = "IE";
            } else {
                clientBrowser = "UnKnown, More-Info: " + browserDetails;
            }
        }

        this.browser = clientBrowser;
    }
    
    private void getIpPort(HttpServletRequest request) {
    	this.port = request.getRemotePort();
    }

    private void getClientUserAgent(HttpServletRequest request) {
        this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    }
    
    private void getLocation() throws LocationException {
    	try {
			InetAddress ip = InetAddress.getByName(this.ipAddress);
			if (!"127.0.0.1".equals(this.ipAddress)) {
				File database = new ClassPathResource("GeoLite2-City.mmdb").getFile();
				DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
				CityResponse response = dbReader.city(ip);
				this.city = response.getCity().getName();
				this.country = response.getCountry().getName();
				this.geoLocation = "GEO:".concat(response.getLocation().getLatitude().toString())
						.concat(";").concat(response.getLocation().getLongitude().toString());
			}
		} catch (IOException | GeoIp2Exception e) {
			throw new LocationException(e.getMessage());
		}
    }

}
