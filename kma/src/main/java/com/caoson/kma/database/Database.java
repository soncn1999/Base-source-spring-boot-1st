package com.caoson.kma.database;

import com.caoson.kma.models.Product;
import com.caoson.kma.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Macbook Pro 16", 2020, 2400.0,"");
                Product productB = new Product("Ipad Air 16", 2020, 400.0,"");

                logger.info("Insert data: " + productRepository.save(productA));
                logger.info("Insert data: " + productRepository.save(productB));
            }
        };
    }
}

//docker run -d --rm --name soncn1999
//-e MYSQL_ROOT_PASSWORD=123456
//-e MYSQL_USER=soncn1999
//-e MYSQL_PASSWORD=123456
//-e MYSQL_DATABASE=test_db
//-p 3306:3306
//--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql
//mysql:latest

//mysql -h localhost -P 3306 --protocol=tcp -u soncn1999 -p