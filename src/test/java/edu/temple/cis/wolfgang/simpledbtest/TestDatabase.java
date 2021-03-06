/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cis.wolfgang.simpledbtest;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author Paul
 */
public class TestDatabase {
    
    private static String createTables =
"CREATE TABLE IF NOT EXISTS Tables (\n" +
"  ID integer DEFAULT '0' NOT NULL,\n" +
"  TableName varchar(255) DEFAULT NULL,\n" +
"  TableTitle varchar(255) DEFAULT NULL,\n" +
"  MajorOnly integer DEFAULT NULL,\n" +
"  MinYear integer DEFAULT NULL,\n" +
"  MaxYear integer DEFAULT NULL,\n" +
"  Class varchar(255) DEFAULT NULL,\n" +
"  TextColumn varchar(255) DEFAULT NULL,\n" +
"  YearColumn varchar(255) DEFAULT NULL,\n" +
"  LinkColumn varchar(255) DEFAULT NULL,\n" +
"  DrillDownFields varchar(255) DEFAULT NULL,\n" +
"  CodeColumn varchar(255) DEFAULT NULL,\n" +
"  Note clob(64K),\n" +
"  PRIMARY KEY (ID)\n" +
")";
    private static String createFilters =
"CREATE TABLE IF NOT EXISTS Filters (\n" +
"  ID integer DEFAULT '0' NOT NULL,\n" +
"  TableID integer DEFAULT NULL,\n" +
"  ColumnName varchar(255) DEFAULT NULL,\n" +
"  Description varchar(255) DEFAULT NULL,\n" +
"  FilterClass varchar(255) DEFAULT NULL,\n" +
"  TableReference varchar(255) DEFAULT NULL,\n" +
"  AdditionalParam varchar(255) DEFAULT NULL,\n" +
"  PRIMARY KEY (ID)\n" +
")";
    private static String createLegServiceAgencyReports =
"CREATE TABLE IF NOT EXISTS LegServiceAgencyReports (\n" +
"  ID integer DEFAULT '0' NOT NULL,\n" +
"  Title varchar(255) DEFAULT NULL,\n" +
"  Organization varchar(255) DEFAULT NULL,\n" +
"  Year integer DEFAULT NULL,\n" +
"  Month integer DEFAULT NULL,\n" +
"  Day integer DEFAULT NULL,\n" +
"  Hyperlink varchar(255),\n" +
"  Abstract clob(64K),\n" +
"  LegRequest smallint DEFAULT NULL,\n" +
"  Recomendation smallint DEFAULT NULL,\n" +
"  Tax smallint DEFAULT NULL,\n" +
"  Elderly smallint DEFAULT NULL,\n" +
"  Comments varchar(255) DEFAULT NULL,\n" +
"  Initials varchar(255) DEFAULT NULL,\n" +
"  FinalCode integer DEFAULT NULL,\n" +
"  PRIMARY KEY (ID)\n" +
")";

    private static String createPennsylvaniaGeneralFundBalance =
"CREATE TABLE IF NOT EXISTS PennsylvaniaGeneralFundBalance (\n" +
"  ID integer DEFAULT '0' NOT NULL,\n" +
"  Year smallint DEFAULT NULL,\n" +
"  Beginning_Balance smallint DEFAULT NULL,\n" +
"  Revenues integer DEFAULT NULL,\n" +
"  Adjustements integer DEFAULT NULL,\n" +
"  Total_Resources integer DEFAULT NULL,\n" +
"  Expenditures integer DEFAULT NULL,\n" +
"  Adjustments integer DEFAULT NULL,\n" +
"  Ending_Balance integer DEFAULT NULL,\n" +
"  Budget_Stabilization_Fund integer DEFAULT NULL,\n" +
"  PRIMARY KEY (ID)\n" +
")";  
    
    private static String createDeflator =
"CREATE TABLE IF NOT EXISTS Deflator (\n" +
"  Year smallint DEFAULT NULL,\n" +
"  GDP double DEFAULT NULL,\n" +
"  Price_Index double DEFAULT NULL,\n" +
"  ID smallint DEFAULT '0' NOT NULL ,\n" +
"  PRIMARY KEY (ID)\n" +
")";
    
    private static DataSource dataSource;
    
    public static void beforeClass() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        dataSource = new DriverManagerDataSource("jdbc:hsqldb:mem:PAPolicy", "SA", "");
        Connection c = null;
        Statement s = null;
        try {
            c = dataSource.getConnection();
            s = c.createStatement();
            s.executeUpdate(createTables);
            s.executeUpdate(createFilters);
            s.executeUpdate(createLegServiceAgencyReports);
            s.executeUpdate(createPennsylvaniaGeneralFundBalance);
            s.executeUpdate(createDeflator);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (s != null) {
                try {s.close();}catch(Exception e){}
            }
            if (c != null) {
                try {c.close();}catch(Exception e){}
            }
        }     
    }
    
    public static void beforeTest() throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:PAPolicy", "SA", "");
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet dataSet = builder.build(new FileInputStream("partial.xml"));
        IDatabaseConnection connection = databaseTester.getConnection();
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }
    
    public static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
    
}
