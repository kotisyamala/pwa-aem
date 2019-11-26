package pwa.core.servlets;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by kassa on 01/04/2019.
 */
@Component(service=Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=DarkSkyNet Servlet - This servlet will get the weather forecast.",
                Constants.SERVICE_VENDOR + "=DarkSkyNet Weather Forecast",
                HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN+"=/forecast" ,
                HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT+"="+ ("(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=org.osgi.service.http)")
        })
@Designate(ocd = ManifestServlet.Configuration.class)
public class DarkSkyNetServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DarkSkyNetServlet.class);
    private Gson gson = new Gson();
    private HashMap<String, Object> fakeForecast = new HashMap();

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @ObjectClassDefinition(name="OSGi Annotation Demo Servlet")
    public @interface Configuration {

        @AttributeDefinition(
                name = "api key",
                description = "api key for the dark sky net forecast service"
        )
        String apiKey() default "---your--darksky--key---";

        @AttributeDefinition(
                name = "base url",
                description = "base url for the dark sky net forecast service"
        )
        String baseUrl() default "https://api.darksky.net/forecast";

    }

    private String apiKey;
    private String baseUrl;

    @Activate
    @Modified
    public void activate(Configuration configuration){
        this.apiKey = configuration.apiKey();
        this.baseUrl = configuration.baseUrl();
        
        this.fakeForecast.put("fakeData", true);
        this.fakeForecast.put("latitude", 0);
        this.fakeForecast.put("longitude", 0);
        this.fakeForecast.put("timezone", "America/New_York");
        HashMap<String, Object> currently = new HashMap<>();
        currently.put("time", 0);
        currently.put("summary", "Clear");
        currently.put("icon", "clear-day");
        currently.put("temperature", 43.4);
        currently.put("humidity", 0.62);
        currently.put("windSpeed", 3.74);
        currently.put("windBearing", 208);
        this.fakeForecast.put("currently", currently);
        HashMap<String, Object> daily = new HashMap<>();
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> one = new HashMap<>();
        one.put("time", 0);
        one.put("icon", "partly-cloudy-night");
        one.put("sunriseTime", 1553079633);
        one.put("sunsetTime", 1553123320);
        one.put("temperatureHigh", 52.91);
        one.put("temperatureLow", 41.35);
        HashMap<String, Object> two = new HashMap<>();
        two.put("time", 86400);
        two.put("icon", "rain");
        two.put("sunriseTime", 1553165933);
        two.put("sunsetTime", 1553209784);
        two.put("temperatureHigh", 48.01);
        two.put("temperatureLow", 44.17);
        HashMap<String, Object> three = new HashMap<>();
        three.put("time", 172800);
        three.put("icon", "rain");
        three.put("sunriseTime", 1553252232);
        three.put("sunsetTime", 1553296247);
        three.put("temperatureHigh", 50.31);
        three.put("temperatureLow", 33.61);
        HashMap<String, Object> four = new HashMap<>();
        four.put("time", 259200);
        four.put("icon", "partly-cloudy-night");
        four.put("sunriseTime", 1553338532);
        four.put("sunsetTime", 1553382710);
        four.put("temperatureHigh", 46.44);
        four.put("temperatureLow", 33.82);
        HashMap<String, Object> five = new HashMap<>();
        five.put("time", 345600);
        five.put("icon", "partly-cloudy-night");
        five.put("sunriseTime", 1553424831);
        five.put("sunsetTime", 1553469172);
        five.put("temperatureHigh", 60.5);
        five.put("temperatureLow", 43.82);
        HashMap<String, Object> six = new HashMap<>();
        six.put("time", 432000);
        six.put("icon", "rain");
        six.put("sunriseTime", 1553511130);
        six.put("sunsetTime", 1553555635);
        six.put("temperatureHigh", 61.79);
        six.put("temperatureLow", 32.8);
        HashMap<String, Object> seven = new HashMap<>();
        seven.put("time", 518400);
        seven.put("icon", "rain");
        seven.put("sunriseTime", 1553597430);
        seven.put("sunsetTime", 1553642098);
        seven.put("temperatureHigh", 48.28);
        seven.put("temperatureLow", 33.49);
        HashMap<String, Object> eight = new HashMap<>();
        eight.put("time", 604800);
        eight.put("icon", "snow");
        eight.put("sunriseTime", 1553683730);
        eight.put("sunsetTime", 1553728560);
        eight.put("temperatureHigh", 43.58);
        eight.put("temperatureLow", 33.68);
        
        data.add(one);
        data.add(two);
        data.add(three);
        data.add(four);
        data.add(five);
        data.add(six);
        data.add(seven);
        data.add(eight);

        daily.put("data",data);
        
        this.fakeForecast.put("daily", daily);
        
    }


    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws ServletException, IOException {
    	
    	
    	String requestURL = req.getRequestURL().toString();
    	
    	resp.setContentType("application/json");
    	String forecast = null;
		try {
			forecast = getForecast(requestURL);
			
			//log.info(forecast);
			resp.getWriter().write(forecast);
		} catch (Exception e) {
			log.error(e.getMessage());
			resp.getWriter().write(this.gson.toJson(this.fakeForecast));
		}
    }
    
	private String getForecast(String query) throws Exception {
		
		log.info("getting Forecast for: " + query);
		
		String location;
    	
    	int lastSlash = query.lastIndexOf("/");
        if (lastSlash > 0) {
        	location = query.substring(lastSlash + 1, query.length());
        } else {
        	location = "40.7720232,-73.9732319";
        }
        log.info("location: " + location);
		
		String url = this.baseUrl + "/" + this.apiKey + "/" + location;

		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);

		int statusCode = client.executeMethod(method);

		if (statusCode != HttpStatus.SC_OK) {
			log.info("HTTP Method failed: " + method.getStatusLine());
		}

		// Read the response body.
		byte[] responseBody = method.getResponseBody();

		// Deal with the response.
		// Use caution: ensure correct character encoding and is not binary data
		return new String(responseBody);

	}
}
