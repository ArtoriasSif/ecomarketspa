package han.msvc_feedback.mscv_feedback.client;

import han.msvc_feedback.mscv_feedback.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-products",url = "http://localhost:8083/api/products")
public interface ProductClientRest {

    @GetMapping("/{id}")
        Product findByIdProducto(@PathVariable Long id);
}
