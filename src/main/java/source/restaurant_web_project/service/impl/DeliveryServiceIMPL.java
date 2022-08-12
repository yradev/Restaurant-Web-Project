package source.restaurant_web_project.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemBagDTO;
import source.restaurant_web_project.model.entity.Address;
import source.restaurant_web_project.model.entity.Delivery;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.model.entity.enums.DeliveryStatus;
import source.restaurant_web_project.repository.DeliveryRepository;
import source.restaurant_web_project.repository.ItemRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.DeliveryService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static source.restaurant_web_project.model.entity.enums.DeliveryStatus.PENDING;

@Service
public class DeliveryServiceIMPL implements DeliveryService {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceIMPL(HttpServletRequest request, HttpServletResponse response, UserRepository userRepository, ItemRepository itemRepository, ModelMapper modelMapper, DeliveryRepository deliveryRepository) {
        this.request = request;
        this.response = response;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void addItemToBag(String itemName,BigDecimal price) {
     Cookie cookie =  Arrays.stream(request.getCookies())
             .filter(a->a.getName().equals("ItemsBag"))
             .map(Cookie::getValue)
             .findFirst()
             .map(a->new Cookie("ItemsBag",String.format("%s%s?%s|",a,itemName,price)))
             .orElse(new Cookie("ItemsBag",String.format("%s?%s|",itemName,price)));

        cookie.setMaxAge(24 * 60 * 60); // expires in 1 days
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");


     response.addCookie(cookie);
    }

    @Override
    public List<ItemBagDTO> getItemsFromBag() {
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return new ArrayList<>();
        }

        String itemBag = Arrays.stream(cookies)
                .filter(a -> a.getName().equals("ItemsBag"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if(itemBag==null){
            return new ArrayList<>();
        }
            List<String> itemsAndPrices = Arrays.stream(itemBag.split("\\|")).toList(); // Separator between items

            List<ItemBagDTO> itemsBag = new ArrayList<>();
            Map<String, Long> countsOfItems= new LinkedHashMap<>();



            itemsAndPrices.forEach(a->{
                String [] splitItemsAndPrices = a.split("\\?"); // Separator between items and prices

                String itemName = splitItemsAndPrices[0];
                BigDecimal itemPrice = BigDecimal.valueOf(Double.parseDouble(splitItemsAndPrices[1]));

                if(countsOfItems.containsKey(itemName)){
                    countsOfItems.put(itemName,countsOfItems.get(itemName)+1);
                }else{
                    countsOfItems.put(itemName,Long.valueOf("1"));
                    itemsBag.add(new ItemBagDTO(itemName,itemPrice));
                }
            });

        itemsBag.forEach(item->{
            item.setCountInBag(countsOfItems.get(item.getName()));
            item.setTotalPrice(BigDecimal.valueOf(countsOfItems.get(item.getName())).multiply(item.getPrice()));
        });


        return itemsBag;
    }

    @Override
    public void deleteItemFromBag(String itemName,BigDecimal price) {
      List<ItemBagDTO> items =  getItemsFromBag();
      StringBuilder cookieValue = new StringBuilder();

          items.stream().peek(a -> {
              if (a.getName().equals(itemName)) {
                  a.setCountInBag(a.getCountInBag() - 1);
              }
          }).filter(a -> a.getCountInBag() > 0).forEach(a -> {
              for (int i = 1; i <= a.getCountInBag(); i++) {
                  cookieValue.append(a.getName()).append("?").append(a.getPrice()).append("|");
              }
          });

        Cookie cookie;

         if(cookieValue.isEmpty()){
            cookie = new Cookie("ItemsBag",null);
            cookie.setMaxAge(0);
        }else{
             cookie = new Cookie("ItemsBag", cookieValue.toString());
             cookie.setMaxAge(24 * 60 * 60); // expires in 1 days
         }

        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void addNewDelivery(String deliveryAddressName, String name) {
        Delivery delivery = new Delivery();
        delivery.setReceiveTime(LocalDateTime.now());
        delivery.setStatus(PENDING);

        Set<Item>items = getItemsFromBag().stream()
                .map(item->{
                    Item currentItem = itemRepository.findItemByName(item.getName());
                    currentItem.setCountInBag(item.getCountInBag());
                    return currentItem;
                })
                .collect(Collectors.toSet());

        delivery.setItems(items);
        delivery.setTotalPrice(
                BigDecimal.valueOf(items.stream().mapToDouble(item->item.getPrice().multiply(BigDecimal.valueOf(item.getCountInBag())).doubleValue()).sum())
        );

        User user = userRepository.findUserByUsername(name);
        delivery.setDeliver(user);

        Address address = user.getAddress().stream().filter(add->add.getName().equals(deliveryAddressName)).findFirst().orElse(null);
        delivery.setAddress(address);

        delivery.setActive(true);
        deliveryRepository.saveAndFlush(delivery);

        Cookie cookie = new Cookie("ItemsBag",null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public List<DeliveryViewDTO> getDeliveriesForCurrentUser(String name) {
        return userRepository.findUserByUsername(name).getDeliveries().stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDelivery(long id) {
        Delivery delivery = deliveryRepository.findDeliveryById(id);
        if(delivery.getStatus().equals(PENDING)) {
            deliveryRepository.delete(delivery);
        }
    }

    @Override
    public List<DeliveryViewDTO> getDeliveriesForStaff() {
        return deliveryRepository.findAll().stream().map(delivery->modelMapper.map(delivery,DeliveryViewDTO.class)).collect(Collectors.toList());
    }
}
