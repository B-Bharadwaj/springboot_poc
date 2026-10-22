    package com.example.order_poc.seed;

import com.example.order_poc.model.Order;
import com.example.order_poc.model.OrderItem;
import com.example.order_poc.model.Product;
import com.example.order_poc.model.User;
import com.example.order_poc.repository.OrderRepository;
import com.example.order_poc.repository.ProductRepository;
import com.example.order_poc.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public DataSeeder(ProductRepository productRepository, UserRepository userRepository,
                      OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // @Override tells the compiler this method implements CommandLineRunner.run (compile error if it doesn't).
    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new Product(null, "Laptop", 999.99, 10),
                    new Product(null, "Mouse", 19.50, 10),
                    new Product(null, "Keyboard", 49.00, 5)));
        }
        List<Product> products = productRepository.findAll();

        List<User> users = userRepository.saveAll(List.of(
                newUser("Alice", "alice@example.com"),
                newUser("Bob", "bob@example.com"),
                newUser("Carol", "carol@example.com")));

        String[] statuses = {"NEW", "PAID", "SHIPPED", "NEW", "DELIVERED"};
        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setUser(users.get(i % users.size()));
            order.setCreatedAt(LocalDateTime.now().minusDays(5 - i));
            order.setStatus(statuses[i]);
            for (int j = 0; j < 3; j++) {
                Product product = products.get(j % products.size());
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(j + 1);
                item.setUnitPrice(product.getPrice());
                order.getItems().add(item);
            }
            orderRepository.save(order);
        }
    }

    private User newUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
