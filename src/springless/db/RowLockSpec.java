package springless.db;

/**
 * innodb 的 显式锁 。 
 **/
public enum RowLockSpec {
    
    None(""),
    ForShare("FOR SHARE"),
    ForShareNowait("FOR SHARE NOWAIT"),
    ForShareSkipLocked("FOR SHARE SKIP LOCKED"),
    ForUpdate("FOR UPDATE"),
    ForUpdateNowait("FOR UPDATE NOWAIT"),
    ForUpdateSkipLocked("FOR UPDATE SKIP LOCKED");
    
    private String sql;
    
    RowLockSpec(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
