package gubo.db;

import gubo.doc.Comment;
import gubo.http.grizzly.handlergenerator.MappingToPath;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

// 追踪jdbc链接泄漏
public class LeakTracker {

    ConcurrentHashMap<Connection, StackTraceElement[]> track = new ConcurrentHashMap<Connection, StackTraceElement[]> ();
    
    public void add(Connection conn, StackTraceElement[] stack) {
        track.put(conn, stack);
    }
    
    public void remove(Connection conn) {
        this.track.remove(conn);
    }
    
    public static class EmptyRequest {}
    @MappingToPath(value = "/dbconn/leak-track", method = "GET")
    @Comment(value = "链接泄漏。", group = "系统监控")
    public ConcurrentHashMap<Connection, StackTraceElement[]> login(EmptyRequest req) {
        return this.track;
    }
}
