package com.example.vermillion.Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Test
    void home_ShouldReturnIndexView() {
        HomeController controller = new HomeController();
        String viewName = controller.home();
        assertEquals("index", viewName);
    }
}