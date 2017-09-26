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


See TOMCAT files in readme-TOMCAT_SETTINGS folder. jun-2017
