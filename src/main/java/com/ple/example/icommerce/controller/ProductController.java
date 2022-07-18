package com.ple.example.icommerce.controller;


import com.ple.example.icommerce.annotation.AuditField;
import com.ple.example.icommerce.annotation.Auditable;
import com.ple.example.icommerce.dto.ProductFilter;
import com.ple.example.icommerce.dto.ProductPageResults;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.dto.ProductResponse;
import com.ple.example.icommerce.entity.AuditActionType;
import com.ple.example.icommerce.entity.tenant.Product;
import com.ple.example.icommerce.service.ProductService;
import com.ple.example.icommerce.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@Validated
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/add")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        log.trace("Create product: {}", productRequest);
        Product createdProduct = productService.create(productRequest);
        if (createdProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ModelUtils.toProductResponse(createdProduct));
    }

    @GetMapping("/{key}")
    @Auditable(action = AuditActionType.VIEW_DETAIL)
    public ResponseEntity<ProductResponse> get(@NotNull @Min(0)
                                               @PathVariable("key")
                                               @AuditField(name = "key") Long key) {
        log.trace("Get detail product: #key: {}", key);
        Optional<Product> product = productService.get(key);
        return product.map(value -> ResponseEntity.ok(ModelUtils.toProductResponse(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{key}")
    public ResponseEntity<ProductResponse> update(@NotNull @Min(0) @PathVariable("key") Long key,
                                                  @Valid @RequestBody ProductRequest productRequest) {
        log.trace("Update the existing product: #key: {}, #product: {}", key, productRequest);
        Optional<Product> product = productService.update(key, productRequest);
        return product.map(value -> ResponseEntity.ok(ModelUtils.toProductResponse(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Auditable(action = AuditActionType.SEARCH)
    public ResponseEntity<ProductPageResults> search(
            @RequestParam(required = false) @AuditField(name = "name") String name,
            @RequestParam(required = false) @AuditField(name = "sku") String sku,
            @RequestParam(defaultValue = "0") @AuditField(name = "minPrice") double minPrice,
            @RequestParam(required = false) @AuditField(name = "maxPrice") Double maxPrice,
            @RequestParam(defaultValue = "0") @AuditField(name = "minQuantity") int minQuantity,
            @RequestParam(required = false) @AuditField(name = "maxQuantity") Integer maxQuantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "key,desc") String[] sort) {

        Pageable pagingSort = PageRequest.of(page, size, getSort(sort));
        ProductFilter productFilter = ProductFilter.builder()
                .name(name)
                .sku(sku)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minQuantity(minQuantity)
                .maxQuantity(maxQuantity)
                .pagingSort(pagingSort).build();

        log.trace("Search product: #filter: {}", productFilter);

        Page<Product> productPage = productService.search(productFilter);
        List<Product> products = productPage.getContent();
        if (CollectionUtils.isEmpty(products)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ProductResponse> productResponses = products.stream()
                .map(ModelUtils::toProductResponse).collect(Collectors.toList());
        ProductPageResults pageResults = ProductPageResults.builder()
                .products(productResponses)
                .currentPage(productPage.getNumber())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .build();

        return new ResponseEntity<>(pageResults, HttpStatus.OK);
    }

    private Sort getSort(String[] sort) {
        List<Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            // with sort more than 2 fields
            // sort=[sortOrder]
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] sortItem = sortOrder.split(",");
                orders.add(new Order(getSortDirection(sortItem[1]), sortItem[0]));
            }
        } else {
            // with sort 1 field
            // sort=[field, direction]
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }
        return Sort.by(orders);
    }

    private Direction getSortDirection(String direction) {
        switch (direction) {
            case "desc":
                return Direction.DESC;
            case "asc":
            default:
                return Direction.ASC;
        }
    }

}
