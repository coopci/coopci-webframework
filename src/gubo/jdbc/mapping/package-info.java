/**
 * 
 */
/**
 * @author cooper
 *
 * 按照pojo上的jpa注释，生成 jdbc的PreparedStatement。
 * 之所以做这个，是因为没找到接受由我提供的Connection并且用jdbc方式控制事务和隔离级别的ORM。
 * 目前只支持mysql, 因为用 ` 来quote列名和表名了。
 */
package gubo.jdbc.mapping;