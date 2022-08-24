package com.vnightray.springsqlitetest.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.vnightray.springsqlitetest.utils.SqliteBuilder;
import com.vnightray.springsqlitetest.utils.SqliteUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@MapperScan(basePackages = {"com.vnightray.springsqlitetest.dao"},
        sqlSessionFactoryRef = "sqlSessionFactory")
@Configuration
public class SqliteConfig {

    @Value("${blog.sqlite.url}")
    private String dataSourcedUrl;


    @Bean(name = "sqliteDataSource")
    public DataSource sqliteDataSource(){
        //尝试创建sqlite文件-不存在时创建
        String dbName = SqliteUtils.getFilePath(dataSourcedUrl);
        String dbFilePath = SqliteUtils.initSqliteFile(dbName);

        //创建数据源
        DataSource dataSource = SqliteBuilder.create().filePath(dbFilePath).build();
        try {
            SqliteUtils.initSqliteDb(dataSource.getConnection());
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }

        return dataSource;
    }


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().
                        getResources("classpath:mapper/**/*.xml"));
//        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
//        conf.setMapUnderscoreToCamelCase(true);
        sessionFactoryBean.setConfiguration(new org.apache.ibatis.session.Configuration());
        return sessionFactoryBean.getObject();

    }


    /**
     * session模板
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate ComSqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 动态数据源
     * @param dataSource
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("sqliteDataSource") DataSource dataSource){
        return  new DynamicDataSource(dataSource);
    }

}
