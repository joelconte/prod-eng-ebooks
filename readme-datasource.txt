Do not delete directory.  it contains workspace and repolitory for books.

Adding sql server jdbc to maven pom.xml:  http://claude.betancourt.us/add-microsoft-sql-jdbc-driver-to-maven/  

Tomcat needs files:
  catalina.properties 
  web.xml (for login timeout)
  server.xml (docBase to webapp)

May need to copy in jdbc jars into tomcat lib dir when deploy.


You will need to create a tomcat server and have it setup to create an oracle datasource on startup with these settings:

Add to catalina.properties:
# DataSource Properties
ds.url=jdbc:oracle:thin:@localhost:1521:xe
ds.username=ebook
ds.p=<password>
ds.maxPoolSize=25
# DataSource Properties to kofax
ds2.url=jdbc:sqlserver://10.88.46.141\\SQLEXPRESS
ds2.username=ODPC_data_view
ds2.p=x
ds2.maxPoolSize=25


also for lds-account:
security.connection.url=ldaps://gdir.wh.ldsglobal.net:636
security.connection.dn=cn=LDAP-familySearchBookScan,ou=apps,o=lds
security.connection.password=x


Add to server.xml
  <GlobalNamingResources>
    <Resource auth="Container" 
connectionProperties="oracle.jdbc.ReadTimeout=120000;includeSynonyms=true;v$session.program=${projectName}" 
driverClassName="oracle.jdbc.OracleDriver" factory="org.apache.tomcat.jdbc.pool.DataSourceFactory" 
initialSize="0" jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;org.apache.tomcat.jdbc.pool.interceptor.StatementDecoratorInterceptor;org.apache.tomcat.jdbc.pool.interceptor.SlowQueryReportJmx;org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer" 
jmxEnabled="true" logAbandoned="true" maxActive="${ds.maxPoolSize}" 
minIdle="0" 
name="jdbc/default/DataSource" 
password="${ds.p}" 
removeAbandoned="true" 
removeAbandonedTimeout="120" 
testOnBorrow="true" 
testOnReturn="false" testWhileIdle="true" 
type="javax.sql.DataSource" 
url="${ds.url}" 
username="${ds.username}"  />

    
  <Resource auth="Container" 
    connectionProperties="" 
    driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver" 
    factory="org.apache.tomcat.jdbc.pool.DataSourceFactory" 
    initialSize="0" 
    jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;org.apache.tomcat.jdbc.pool.interceptor.StatementDecoratorInterceptor;org.apache.tomcat.jdbc.pool.interceptor.SlowQueryReportJmx;org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer" 
    jmxEnabled="true" 
    logAbandoned="true" 
    maxActive="${ds2.maxPoolSize}" 
    minIdle="0" 
    name="jdbc/kofax/DataSource" 
    password="${ds2.p}" 
    removeAbandoned="true" 
    removeAbandonedTimeout="120" 
    testOnBorrow="true" 
    testOnReturn="false" 
    testWhileIdle="true" 
    type="javax.sql.DataSource" 
    url="${ds2.url}" 
    username="${ds2.username}"/>

 
  </GlobalNamingResources>