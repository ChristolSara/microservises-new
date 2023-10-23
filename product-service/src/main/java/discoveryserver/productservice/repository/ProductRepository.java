package discoveryserver.productservice.repository;

import discoveryserver.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository  extends MongoRepository<Product,String> {
}
