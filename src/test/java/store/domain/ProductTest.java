package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @BeforeEach
    void setUp() {
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
    }

    @Test
    void
