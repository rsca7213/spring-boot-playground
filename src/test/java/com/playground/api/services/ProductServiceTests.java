package com.playground.api.services;

import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.*;
import com.playground.api.entities.Product;
import com.playground.api.enums.CurrencyType;
import com.playground.api.enums.ProductCategory;
import com.playground.api.integrations.ports.MultimediaStorageService;
import com.playground.api.repositories.MultimediaRepository;
import com.playground.api.repositories.ProductRepository;
import com.playground.api.utils.CurrencyUtils;
import com.playground.api.utils.MultimediaUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private MultimediaRepository multimediaRepository;

    @Mock
    private MultimediaStorageService multimediaStorageService;

    @Mock
    private MultimediaUtils multimediaUtils;

    @Mock
    private CurrencyUtils currencyUtils;

    @InjectMocks
    private ProductService productService;

    // Request DTOs
    private CreateProductBody createProductBody;
    private ListProductsQuery listProductsQuery;
    private UpdateProductBody updateProductBody;

    // Response DTOs
    private CreateProductResponse createProductResponse;
    private ListProductsResponse listProductsResponse;
    private FindProductResponse findProductResponse;
    private UploadProductImageResponse uploadProductImageResponse;
    private UpdateProductResponse updateProductResponse;

    // Entities
    private Product product;

    // Values used in the tests
    private final UUID uuid = UUID.randomUUID();
    private final String imageUrl = "https://example.com/image.jpg";
    private final String imageUri = "products/" + uuid + ".jpeg";
    private Map<CurrencyType, BigDecimal> exchangeRates;

    @BeforeEach
    void setUp() {
        // Set up entities
        product = new Product();
        product.setId(uuid);
        product.setName("Test Product");
        product.setDescription("This is a test product");
        product.setCategory(ProductCategory.AUTOMOTIVE);
        product.setPrice(100.0);
        product.setStockQuantity(5);

        // Set up exchange rates
        exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyType.USD, BigDecimal.valueOf(product.getPrice()));
        exchangeRates.put(CurrencyType.EUR, BigDecimal.valueOf(product.getPrice() * 0.85));
        exchangeRates.put(CurrencyType.GBP, BigDecimal.valueOf(product.getPrice() * 0.75));
        exchangeRates.put(CurrencyType.JPY, BigDecimal.valueOf(product.getPrice() * 110));
        exchangeRates.put(CurrencyType.CAD, BigDecimal.valueOf(product.getPrice() * 1.25));
        exchangeRates.put(CurrencyType.AUD, BigDecimal.valueOf(product.getPrice() * 1.35));

        // Set up request DTOs
        createProductBody = CreateProductBody.builder()
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();

        listProductsQuery = ListProductsQuery.builder()
                .name(product.getName())
                .category(product.getCategory())
                .minPrice(product.getPrice() - 50.0)
                .maxPrice(product.getPrice() + 50.0)
                .hasStock(true)
                .build();

        updateProductBody = UpdateProductBody.builder()
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();

        // Set up response DTOs
        createProductResponse = CreateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(null)
                .build();

        listProductsResponse = ListProductsResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();

        findProductResponse = FindProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(null)
                .price(exchangeRates)
                .build();

        uploadProductImageResponse = UploadProductImageResponse.builder()
                .productId(product.getId())
                .url(imageUrl)
                .build();

        updateProductResponse = UpdateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(null)
                .build();
    }

    @Test
    void createProduct_Success() {
        // Mock the product repository to return the created product
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        // Call the createProduct method
        CreateProductResponse response = productService.createProduct(createProductBody);

        // Verify that the product repository was called to save the new product
        Mockito.verify(productRepository).save(Mockito.any(Product.class));

        // Assert that the response contains the expected product details
        Assertions.assertEquals(createProductResponse, response);
    }

    @Test
    void createProduct_NameAlreadyExists() {
        // Mock the product repository to indicate that a product with the same name already exists
        Mockito.when(productRepository.findByNameIgnoreCase(createProductBody.getName())).thenReturn(product);

        // Call the createProduct method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.createProduct(createProductBody)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("A product with the same name already exists", exception.getMessage());
    }

    @Test
    void listProducts_Success() {
        // Generate a Page for the product list
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        // Mock the product repository to return a list of products
        Mockito.when(productRepository.findAll(Mockito.<Specification<Product>>any(), Mockito.any()))
                .thenReturn(productPage);

        // Call the listProducts method
        PaginationResponse<ListProductsResponse> response = productService.listProducts(listProductsQuery);

        // Verify that the product repository was called with the correct filters
        Mockito.verify(productRepository).findAll(Mockito.<Specification<Product>>any(), Mockito.any(Pageable.class));

        // Assert that the response contains the expected product details
        Assertions.assertFalse(response.getItems().isEmpty());
        Assertions.assertEquals(1, response.getItems().size());
        Assertions.assertEquals(listProductsResponse, response.getItems().getFirst());
    }

    @Test
    void listProducts_NoResults() {
        // Mock the product repository to return an empty list
        Mockito.when(productRepository.findAll(Mockito.<Specification<Product>>any(), Mockito.any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Call the listProducts method
        PaginationResponse<ListProductsResponse> response = productService.listProducts(listProductsQuery);

        // Verify that the product repository was called with the correct filters
        Mockito.verify(productRepository).findAll(Mockito.<Specification<Product>>any(), Mockito.any(Pageable.class));

        // Assert that the response contains no items
        Assertions.assertTrue(response.getItems().isEmpty());
    }

    @Test
    void findProductById_Success() {
        // Mock the product repository to return the product by ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(java.util.Optional.of(product));

        // Mock the exchangeRates util to return the exchange rates
        Mockito.when(
                currencyUtils.getPricesForCurrencies(
                        CurrencyType.USD,
                        BigDecimal.valueOf(product.getPrice())
                )
        ).thenReturn(exchangeRates);

        // Call the findProductById method
        FindProductResponse response = productService.findProductById(uuid);

        // Verify that the product repository was called with the correct ID
        Mockito.verify(productRepository).findById(uuid);

        // Assert that the response contains the expected product details
        Assertions.assertEquals(findProductResponse, response);
    }

    @Test
    void findProductById_NotFound() {
        // Mock the product repository to return an empty Optional for the given ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(java.util.Optional.empty());

        // Call the findProductById method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.findProductById(uuid)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("A product with the given ID does not exist", exception.getMessage());
    }

    @Test
    void uploadProductImage_Success() {
        // Mock the file upload process
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.isEmpty()).thenReturn(false);
        Mockito.when(mockFile.getContentType()).thenReturn("image/jpeg");

        // Mock the multimediaUtils to validate and extract file extension
        Mockito.when(multimediaUtils.validateAndExtractFileExtension(mockFile)).thenReturn("jpeg");
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product));

        // Mock the multimediaStorageService to return a public URL for the uploaded image
        Mockito.when(multimediaStorageService.upload(mockFile, "products/" + uuid + ".jpeg"))
                .thenReturn(imageUri);

        // Mock the multimediaStorageService to generate a public URL
        Mockito.when(multimediaStorageService.generatePublicUrl(imageUri)).thenReturn(imageUrl);

        // Mock the multimediaRepository to save the multimedia entity
        Mockito.when(multimediaRepository.save(Mockito.any())).thenReturn(null);

        // Call the uploadProductImage method
        UploadProductImageResponse response = productService.uploadProductImage(uuid, mockFile);

        // Verify that the multimediaUtils was called to validate the file
        Mockito.verify(multimediaUtils).validateAndExtractFileExtension(mockFile);

        // Verify that the product repository was called to find the product by ID
        Mockito.verify(productRepository).findById(uuid);

        // Verify that the multimediaStorageService was called to upload the file
        Mockito.verify(multimediaStorageService).upload(mockFile, "products/" + uuid + ".jpeg");

        // Verify that the multimediaStorageService was called to generate the public URL
        Mockito.verify(multimediaStorageService).generatePublicUrl(imageUri);

        // Assert that the exception message is as expected
        Assertions.assertEquals(uploadProductImageResponse, response);
    }

    @Test
    void uploadProductImage_FileEmpty() {
        // Mock an empty file
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.isEmpty()).thenReturn(true);

        // Call the uploadProductImage method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.uploadProductImage(uuid, mockFile)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("The uploaded file is empty", exception.getMessage());
    }

    @Test
    void uploadProductImage_ProductNotFound() {
        // Mock the file upload process
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.isEmpty()).thenReturn(false);

        // Mock the product repository to return an empty Optional for the given ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // Call the uploadProductImage method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.uploadProductImage(uuid, mockFile)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("A product with the given ID does not exist", exception.getMessage());
    }

    @Test
    void updateProduct_Success() {
        // Mock the product repository to return the product by ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        // Call the updateProduct method
        UpdateProductResponse response = productService.updateProduct(uuid, updateProductBody);

        // Verify that the product repository was called with the correct ID
        Mockito.verify(productRepository).findById(uuid);
        Mockito.verify(productRepository).save(Mockito.any(Product.class));

        // Assert that the response contains the expected product details
        Assertions.assertEquals(updateProductResponse, response);
    }

    @Test
    void updateProduct_NotFound() {
        // Mock the product repository to return an empty Optional for the given ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // Call the updateProduct method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.updateProduct(uuid, updateProductBody)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("A product with the given ID does not exist", exception.getMessage());
    }

    @Test
    void updateProduct_NameAlreadyExists() {
        UpdateProductBody updateProductBody = UpdateProductBody.builder()
                .name(product.getName() + " Updated")
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();

        // Mock the product repository to return a product with the same name
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findByNameIgnoreCase(updateProductBody.getName())).thenReturn(product);

        // Call the updateProduct method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.updateProduct(uuid, updateProductBody)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("A product with the same name already exists", exception.getMessage());
    }

    @Test
    void deleteProduct_Success() {
        // Mock the product repository to return the product by ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

        // Call the deleteProduct method
        productService.deleteProduct(uuid);

        // Verify that the product repository was called with the correct ID
        Mockito.verify(productRepository).findById(uuid);
        Mockito.verify(productRepository).save(Mockito.any(Product.class));
    }

    @Test
    void deleteProduct_NotFound() {
        // Mock the product repository to return an empty Optional for the given ID
        Mockito.when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // Call the deleteProduct method and expect an exception
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> productService.deleteProduct(uuid)
        );

        // Assert that the exception message is as expected
        Assertions.assertEquals("A product with the given ID does not exist", exception.getMessage());
    }
}
