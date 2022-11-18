package acapi.application;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

/**
 * @author wesleykempa
 */
@Component(
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
			  .addHeader("Authorization", "Bearer 24f56fc3943a334ab8a3e2a4cdfc753d9f0d1726167f0f3ef8c1637d6ba794")
			  .build();
			Response response = client.newCall(request).execute();
			msg = response.body().string();

		}catch(IOException ex){ }

		return msg;
		
		//return "it works!";
	}

	@GET
	@Path("/p/{path}")
	@Produces("text/plain")
	public String morning(	@PathParam("path") String path,
							@QueryParam("keywords") String keywords,
							@QueryParam("page") String pageNumber ) {

		System.out.println("Sent path is: "+path);
		System.out.println("keywords are: "+keywords);

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
			  .addHeader("Authorization", "Bearer 24f56fc3943a334ab8a3e2a4cdfc753d9f0d1726167f0f3ef8c1637d6ba794")
			  .build();
			Response response = client.newCall(request).execute();
			msg = response.body().string();

		}catch(IOException ex){ }

		return msg;
		
	}

}