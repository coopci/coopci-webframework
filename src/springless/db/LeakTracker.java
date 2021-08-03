package springless.db;

import springless.doc.Comment;
import springless.http.grizzly.handlergenerator.MappingToPath;

import java.util.concurrent.ConcurrentHashMap;

// 追踪jdbc链接泄漏
public class LeakTracker {

    ConcurrentHashMap<AutoCloseable, StackTraceElement[]> track = new ConcurrentHashMap<AutoCloseable, StackTraceElement[]> ();
    
    public void add(AutoCloseable conn, StackTraceElement[] stack) {
        track.put(conn, stack);
    }
    
    public void remove(AutoCloseable conn) {
        this.track.remove(conn);
    }
    
    public static class EmptyRequest {}
    @MappingToPath(value = "/dbconn/leak-track", method = "GET")
    @Comment(value = "链接泄漏。", group = "系统监控")
    public ConcurrentHashMap<AutoCloseable, StackTraceElement[]> getAll(EmptyRequest req) {
        return this.track;
    }
}
