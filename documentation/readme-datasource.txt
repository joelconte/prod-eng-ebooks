Do not delete directory.  it contains workspace and repolitory for books.

Adding sql server jdbc to maven pom.xml:  http://claude.betancourt.us/add-microsoft-sql-jdbc-driver-to-maven/ 
(used this command to download jar into my .m2 local repository:  mvn install:install-file -Dfile=sqljdbc4.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0)
 

When adding to pom.xml for the excel file gen code, it had some pre-reqs, but for some reason it loads a lower verison of xml-api code (used for xml file gen for metadata) and so I had to add an exclude so it would not overrite my higher verion of the code...
<exclusion> <groupId>xml-apis</groupId>



Tomcat needs files:
  catalina.properties 
  web.xml (for login timeout)
  server.xml (docBase to webapp)

May need to copy in jdbc jars into tomcat lib dir when deploy.
Yes, tomcat connection pool is external to WAR app, so it needs the jdbc jar in it's lib dir (found this out with postgres)

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

# DataSource Properties to local postgres
ds3.url=jdbc:postgresql://localhost:5432/ebook
ds3.username=postgres
ds3.p=ebook
ds3.maxPoolSize=25



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


  <Resource auth="Container" 
    connectionProperties="" 
    driverClassName="org.postgresql.Driver" 
    factory="org.apache.tomcat.jdbc.pool.DataSourceFactory" 
    initialSize="0" 
    jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;org.apache.tomcat.jdbc.pool.interceptor.StatementDecoratorInterceptor;org.apache.tomcat.jdbc.pool.interceptor.SlowQueryReportJmx;org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer" 
    jmxEnabled="true" 
    logAbandoned="true" 
    maxActive="${ds3.maxPoolSize}" 
    minIdle="0" 
    name="jdbc/myPostgres/DataSource" 
    password="${ds3.p}" 
    removeAbandoned="true" 
    removeAbandonedTimeout="120" 
    testOnBorrow="true" 
    testOnReturn="false" 
    testWhileIdle="true" 
    type="javax.sql.DataSource" 
    url="${ds3.url}" 
    username="${ds3.username}"/>
