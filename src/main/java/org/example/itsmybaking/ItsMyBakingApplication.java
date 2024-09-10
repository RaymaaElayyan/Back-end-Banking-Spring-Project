package org.example.itsmybaking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info=@Info(
                title="My Banking Acc DEMO",
                description="Backend Rest APIs",
                version="v3 mostly...",
                contact=@Contact(
                        name="Raymaa elayyan",
                email="Raymaabmw99@gmail.com"
        )
                )
)
public class ItsMyBakingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsMyBakingApplication.class, args);
    }

}
