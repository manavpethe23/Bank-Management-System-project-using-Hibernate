package com.app.configuration;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.app.model.Bank;

/**
 * HibernateUtil class is responsible for creating and managing
 * the Hibernate SessionFactory instance using programmatic configuration.
 * 
 * This class uses a HashMap to define database connection and Hibernate
 * properties, and builds a SessionFactory using the Metadata API.
 */
public class HibernateUtil {

    // Singleton instance of SessionFactory
    private static SessionFactory sf;

    // Service Registry to hold configuration settings
    private static StandardServiceRegistry registry;

    /**
     * Returns a SessionFactory instance.
     * If it's not created yet, it initializes all Hibernate configurations
     * and builds the SessionFactory.
     */
    public static SessionFactory getSessionFactory() {

        try {
            // Check if SessionFactory is already initialized
            if (sf == null) {

                // -------------------------------
                // Step 1: Define connection properties
                // -------------------------------
                Map<String, Object> map = new HashMap<>();

                // Database connection settings
                map.put(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
                map.put(Environment.JAKARTA_JDBC_URL, "jdbc:mysql://localhost:3306/stu");
                map.put(Environment.JAKARTA_JDBC_USER, "root");
                map.put(Environment.JAKARTA_JDBC_PASSWORD, "Asus@123");

                // -------------------------------
                // Step 2: Define Hibernate properties
                // -------------------------------
                map.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                map.put(Environment.HBM2DDL_AUTO, "update");       // auto schema update
                map.put(Environment.SHOW_SQL, "true");              // show SQL in console

                // -------------------------------
                // Step 3: Enable 2nd level caching
                // -------------------------------
                map.put(Environment.USE_SECOND_LEVEL_CACHE, "true");
                map.put(Environment.CACHE_REGION_FACTORY, "org.hibernate.cache.jcache.JCacheRegionFactory");
                map.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");

                // -------------------------------
                // Step 4: Create Service Registry
                // -------------------------------
                registry = new StandardServiceRegistryBuilder()
                        .applySettings(map)
                        .build();

                // -------------------------------
                // Step 5: Add annotated entity class
                // -------------------------------
                MetadataSources mds = new MetadataSources(registry);
                mds.addAnnotatedClass(Bank.class);

                // -------------------------------
                // Step 6: Build Metadata and SessionFactory
                // -------------------------------
                Metadata md = mds.getMetadataBuilder().build();
                sf = md.getSessionFactoryBuilder().build();

                System.out.println("✅ Hibernate SessionFactory created successfully!");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error creating Hibernate SessionFactory!");
        }

        return sf;
    }
}
