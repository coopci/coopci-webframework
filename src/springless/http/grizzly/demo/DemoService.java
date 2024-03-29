package springless.http.grizzly.demo;

import java.io.IOException;
import java.math.BigDecimal;

import org.glassfish.grizzly.http.server.Response;

import springless.captcha.Captcha;
import springless.db.LeakTracker.EmptyRequest;
import springless.http.grizzly.handlergenerator.LogParameters;
import springless.http.grizzly.handlergenerator.MappingToPath;
import springless.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import springless.http.querystring.QueryStringField;

public class DemoService {

    @MappingToPath(value="/post-strings", method="POST")
    public String postStrings() {
        
        return "DemoService.postStrings";
    }
    
    @MappingToPath(value="/post-file", method="POST")
    public String postFile(InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler, PostFileRequest params) {
        return "DemoService.postFile";
    }
    
    /**
     *  演示 有MultipartFile类型字段的请求。
     *  只有在请求的content-type是 "multipart/form-data"的情况下，MultipartFile类型的字段才能被填对。
     *	"底层原理" 在 {@link AnnotationAwareHandler} 
     **/
    @LogParameters()
    @MappingToPath(value="/post-file2", method="POST")
    public String postFile2(DemoRequest params) {
    	if (params.qualification !=null) {
    		String s = new String(params.qualification.getBytes());
        	
        	System.out.println(s);	
    	} else {
    		System.out.println("params.qualification=null");
    	}
    	
        return "DemoService.postFile";
    }
    
    @MappingToPath(value="/redirect", method="GET")
    public String redirect(EmptyRequest req, Response response) throws IOException {
    	response.sendRedirect("alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill&cardNo=6217000010041030555&bankAccount=张三&money=1&amount=1&bankMark=CCB&bankName=中国建设银行");
        return null;
    }
    
    // http://localhost:8777/captcha
    @MappingToPath(value="/captcha", method="GET")
    public String captcha(EmptyRequest req, Response response) throws IOException {
    	// response.sendRedirect("alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill&cardNo=6217000010041030555&bankAccount=张三&money=1&amount=1&bankMark=CCB&bankName=中国建设银行");
        
    	byte[] data = Captcha.generateImage("1234");
    	response.setContentType("image/png");
    	response.setContentLength(data.length);
    	response.getOutputStream().write(data);
    	response.getOutputStream().close();
    	return null;
    }
    
    public static class LogParametersRequest {
    	@QueryStringField()
    	public BigDecimal num;
    }
    @LogParameters(loggerClass=DemoService.class)
    @MappingToPath(value="/log-paramters", method="POST")
    public String logParamters(LogParametersRequest params) {
        
        return "DemoService.logParamters " + params.num;
    }
    
    
    
}
