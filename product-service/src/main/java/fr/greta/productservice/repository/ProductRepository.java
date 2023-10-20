package fr.greta.productservice.repository;

import fr.greta.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository  extends MongoRepository<Product,String> {
}
