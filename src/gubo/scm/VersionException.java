package gubo.scm;

import gubo.exceptions.ApiException;
// 表示当前版本号不满足需要的版本号。
public class VersionException extends ApiException {

    /**
     * 
     */
    private static final long serialVersionUID = 3407788170963265143L;

    private final String requirement;
    private final String current;
    /**
     *   @param requirement 需要达到的版本号。
     *   @param current 当前版本号。
     **/
    public VersionException(String requirement, String current) {
        this.requirement = requirement;
        this.current = current;
    }
    
    public String getMessage() {
        String msg = "Version not fullfilled, requires: " + this.requirement.toString() + " your version: " + this.current;
        return msg;
    }
    public int getHttpStatus() {
        return 400;
    }
    
    public int getCode() {
        return 400;
    }
}
