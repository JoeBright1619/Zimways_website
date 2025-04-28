package auca.ac.rw.food.delivery.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "auca.ac.rw.food.delivery.management")
public class FoodDeliveryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryManagementApplication.class, args);
	}

}
