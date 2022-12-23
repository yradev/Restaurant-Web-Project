package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;
import source.restaurant_web_project.models.entity.*;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.repositories.DeliveryItemRepository;
import source.restaurant_web_project.repositories.DeliveryRepository;
import source.restaurant_web_project.repositories.ItemRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.DeliveryService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static source.restaurant_web_project.models.entity.enums.DeliveryStatus.*;

@Service
public class DeliveryServiceIMPL implements DeliveryService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryItemRepository deliveryItemRepository;

    public DeliveryServiceIMPL( UserRepository userRepository, ItemRepository itemRepository, ModelMapper modelMapper, DeliveryRepository deliveryRepository, DeliveryItemRepository deliveryItemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.deliveryRepository = deliveryRepository;
        this.deliveryItemRepository = deliveryItemRepository;
    }

    @Override
    public long addNewDelivery(@Valid AddDeliveryDTO addDeliveryDTO) {
        Delivery newDelivery = modelMapper.map(addDeliveryDTO, Delivery.class);

        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        newDelivery.setDeliver(user);

        Address address = user.getAddress().stream().filter(add -> add.getName().equals(addDeliveryDTO.getDeliveryAddressName())).findFirst().orElse(null);
        newDelivery.setAddress(address);
        deliveryRepository.saveAndFlush(newDelivery);

        newDelivery.setStatus(PENDING);

        long totalPrice = addDeliveryDTO.getItems()
                .stream().mapToLong(item -> {
                    Item currentItem = itemRepository.findItemByName(item.getName());
                    if (currentItem == null) {
                        throw new BadRequestException("We dont have item with this name!");
                    }

                    return currentItem.getPrice().multiply(BigDecimal.valueOf(item.getCount())).longValue();
                })
                .sum();

        newDelivery.setTotalPrice(BigDecimal.valueOf(totalPrice));

        Delivery delivery = deliveryRepository.saveAndFlush(newDelivery);

        List<DeliveryItem> deliveries = addDeliveryDTO.getItems().stream()
                .map(a -> {
                    Item currentItem = itemRepository.findItemByName(a.getName());

                    DeliveryItem deliveryItem = new DeliveryItem();
                    deliveryItem.setDelivery(delivery);
                    deliveryItem.setCount(a.getCount());
                    deliveryItem.setItem(currentItem);

                    return deliveryItem;
                }).toList();

        deliveryItemRepository.saveAllAndFlush(deliveries);

        return delivery.getId();
    }



    @Override
    public List<DeliveryViewDTO> getDeliveriesForAuthenticatedUser() {
        return userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .getDeliveries().stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryViewDTO.class))
                .sorted((a, b) -> b.getReceiveTime().compareTo(a.getReceiveTime()))
                .collect(Collectors.toList());
    }



    @Override
    public List<DeliveryViewDTO> getDeliveriesForStaff() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryViewDTO.class))
                .sorted((a, b) -> b.getReceiveTime().compareTo(a.getReceiveTime()))
                .toList();
    }


    @Override
    public void changeStatus(long deliveryID, String status) {
        Delivery delivery = deliveryRepository.findDeliveryById(deliveryID);
        delivery.setStatus(DeliveryStatus.valueOf(status));
        deliveryRepository.saveAndFlush(delivery);
    }

    @Override
    public DeliveryViewDTO getDeliveryDetails(Long id) {
        Delivery delivery = deliveryRepository.findDeliveryById(id);

        if(delivery == null){
            throw new NotFoundException("We dont have delivery with this name!");
        }

        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!user.getDeliveries().contains(delivery) && user.getRoles().stream().noneMatch(a->a.getName().equals("ROLE_STAFF"))){
            throw new UnauthorizedException();
        }

        return modelMapper.map(delivery,DeliveryViewDTO.class);
    }
}