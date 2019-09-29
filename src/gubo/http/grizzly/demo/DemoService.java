package gubo.http.grizzly.demo;

import java.io.IOException;
import java.math.BigDecimal;

import org.glassfish.grizzly.http.server.Response;

import gubo.captcha.Captcha;
import gubo.db.LeakTracker.EmptyRequest;
import gubo.http.grizzly.handlergenerator.LogParameters;
import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.querystring.QueryStringField;

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
    @MappingToPath(value="/post-file2", method="POST")
    public String postFile2(DemoRequest params) {
    	String s = new String(params.qualification.getBytes());
    	
    	System.out.println(s);
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
    @LogParameters()
    @MappingToPath(value="/log-paramters", method="POST")
    public String logParamters(LogParametersRequest params) {
        
        return "DemoService.logParamters " + params.num;
    }
    
    
    
}
