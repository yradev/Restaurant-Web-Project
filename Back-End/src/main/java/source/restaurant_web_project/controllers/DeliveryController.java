package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.models.dto.ChangeStatusDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;
import source.restaurant_web_project.services.DeliveryService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping()
    public ResponseEntity<?> getDeliveriesForAuthenticatedUser(){
        return ResponseEntity.ok(deliveryService.getDeliveriesForAuthenticatedUser());
    }

    @GetMapping("{id}")
    public ResponseEntity<DeliveryViewDTO> getDelivery(@PathVariable long id){
        return ResponseEntity.ok(deliveryService.getDeliveryDetails(id));
    }

    @PostMapping("add")
    public ResponseEntity<?>addDelivery(@RequestBody AddDeliveryDTO addDeliveryDTO){
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass())
                .getDelivery(deliveryService.addNewDelivery(addDeliveryDTO))).toUri()).build();
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("staff")
    public ResponseEntity<?> getDeliveriesForStaff(){
        return ResponseEntity.ok(deliveryService.getDeliveriesForStaff());
    }
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestBody ChangeStatusDTO changeStatusDTO){
        deliveryService.changeStatus(id,changeStatusDTO.getStatus());
        return ResponseEntity.ok().build();
    }
}