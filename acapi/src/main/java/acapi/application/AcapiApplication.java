package acapi.application;

import java.util.Collections;
import java.util.Set;
import java.util.Properties;
import java.util.Map;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import com.liferay.portal.util.PortalInstances;

import acapi.configuration.ACAPIConfiguration;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;

/**
 * @author wesleykempa
 */

@Component(
	configurationPid = "acapi.configuration.ACAPIConfiguration",
	immediate = true,
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/acapi",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=ACAPI.Rest",
        "auth.verifier.guest.allowed=true",
        "liferay.access.control.disable=true"
	},
	service = Application.class
)
public class AcapiApplication extends Application {

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@Activate
	@Modified
	public void activate(Map<String, Object> properties) {
	
		System.out.println("The sample DXP REST app has been activated/updated at " + new Date().toString());
	
		_acAPIConfiguration = ConfigurableUtil.createConfigurable(ACAPIConfiguration.class, properties);
		
		if (_acAPIConfiguration != null) {
			System.out.println("For sample DXP REST config, token= "+_acAPIConfiguration.token());
		} else {
			System.out.println("The sample DXP REST config object is not yet initialized");
		}
	}

	private String getToken(){
		String token = "";
		try{
			token = _acAPIConfiguration.token();
		} catch(Exception exception){
			System.out.println("Unable to get configuration");
		}
		return token;
	}

	@GET
	@Produces("text/plain")
	public String run() {

		String msg = "";

		try{
			OkHttpClient client = new OkHttpClient().newBuilder()
			  .build();
			Request request = new Request.Builder()
			  .url("https://analytics.liferay.com/api/reports")
			  .method("GET",null)
			  .addHeader("Authorization", "Bearer " + getToken())
			  .build();
			Response response = client.newCall(request).execute();
			msg = response.body().string();

		}catch(IOException ex){ }

		return msg;
		
	}

	@GET
	@Path("/p/{path}")
	@Produces("text/plain")
	public String morning(	@PathParam("path") String path,
							@QueryParam("keywords") String keywords,
							@QueryParam("page") String pageNumber ) {

		String msg = "";
		String url = "https://analytics.liferay.com/api/reports/"+path+"?";

		if(null!=keywords){
			url+="&keywords="+keywords;
		}		

		if(null!=pageNumber){
			url+="&page="+pageNumber;
		}						

		try{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder()
			  .url(url)
			  .method("GET",null)
			  .addHeader("Authorization", "Bearer " + getToken())
			  .build();
			Response response = client.newCall(request).execute();
			msg = response.body().string();

		}catch(IOException ex){ }

		return msg;
		
	}

	private  ACAPIConfiguration _acAPIConfiguration;

}