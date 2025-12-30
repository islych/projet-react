package com.shopie.backend.controller;

import com.shopie.backend.model.Order;
import com.shopie.backend.model.User;
import com.shopie.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/orders")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orders", description = "API de gestion des commandes")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    @Operation(summary = "Mes commandes", description = "Récupère les commandes de l'utilisateur connecté")
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Détails d'une commande", description = "Récupère les détails d'une commande")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping
    @Operation(summary = "Créer une commande", description = "Crée une commande à partir du panier")
    public ResponseEntity<Order> createOrder(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Order order = orderService.createOrderFromCart(user);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toutes les commandes", description = "Récupère toutes les commandes (Admin uniquement)")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier le statut", description = "Modifie le statut d'une commande (Admin uniquement)")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam Order.Statut statut) {
        Order order = orderService.updateOrderStatus(id, statut);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/admin/status/{statut}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Commandes par statut", description = "Récupère les commandes par statut (Admin uniquement)")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.Statut statut) {
        List<Order> orders = orderService.getOrdersByStatus(statut);
        return ResponseEntity.ok(orders);
    }
}