package lesson5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;


public class ModifyAndDeleteProductTest {

    static ProductService productService;
    Product modifyProduct = null;
    Product existProduct = null;

    int newId;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() throws IOException {
        Response<Product> response = productService.getProductById(1).execute();
        existProduct = response.body();

        modifyProduct = new Product()
                .withTitle("Milk-1")
                .withCategoryTitle("Food")
                .withPrice(100);
    }

    @Test
    void modifyProductTest() throws IOException {
        Response<Product> createResponse = productService.createProduct(modifyProduct).execute();
        newId = createResponse.body().getId();

        Response<Product> response = productService.modifyProduct(modifyProduct
                        .withId(newId)
                        .withTitle("Milk-modify"))
                .execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> deleteResponse = productService.deleteProduct(newId).execute();
        assertThat(deleteResponse.isSuccessful(), CoreMatchers.is(true));
    }



}
