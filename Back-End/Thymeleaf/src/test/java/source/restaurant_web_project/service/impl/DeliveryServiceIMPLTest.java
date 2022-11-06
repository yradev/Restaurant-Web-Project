package source.restaurant_web_project.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import source.restaurant_web_project.model.dto.NewDeliveryDTO;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static source.restaurant_web_project.model.entity.enums.DeliveryStatus.PENDING;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceIMPLTest {

    @Mock
    private HttpServletRequest mockedRequest;
    @Mock
    private HttpServletResponse mockedResponse;
    @Mock
    private UserRepository mockedUserRepository;
    @Mock
    private ItemRepository mockedItemRepository;
    @Mock
    private DeliveryRepository mockedDeliveryRepository;
    private ModelMapper modelMapper;

    private DeliveryService deliveryServiceTest;
    private Cookie cookie;
    private User testUserEntity;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        deliveryServiceTest = new DeliveryServiceIMPL(mockedRequest, mockedResponse, mockedUserRepository, mockedItemRepository, modelMapper, mockedDeliveryRepository);

        cookie = new Cookie("ItemsBag", "item1?22.00|item2?22.00|item2?22.00|item1?22.00|");
        testUserEntity = new User();
        testUserEntity.setUsername("test_username");
        testUserEntity.setEnabled(true);
        testUserEntity.setEmail("test_email");
    }

    @Test
    void addItemToBag_moreThan14() {
        Cookie cookie = new Cookie("ItemsBag", "item1?22.00|item2?22.00|item3?22.00|item4?22.00|item5?22.00|item6?22.00|item7?22.00|item8?22.00|" +
                "item9?22.00|item10?22.00|item11?22.00|item12?22.00|item13?22.00|item14?22.00|item15?22.00|item16?22.00|");
        when(mockedRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        Assertions.assertFalse(deliveryServiceTest.addItemToBag("item1", BigDecimal.valueOf(20)));
    }

    @Test
    void addItemToBag_valid() {
        when(mockedRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        Assertions.assertTrue(deliveryServiceTest.addItemToBag("item1", BigDecimal.valueOf(20)));
    }

    @Test
    void getItemsFromBag_nullCookies() {
        when(mockedRequest.getCookies()).thenReturn(null);
        List<ItemBagDTO> items = deliveryServiceTest.getItemsFromBag();
        Assertions.assertEquals(0, items.size());
    }

    @Test
    void getItemsFromBag_itemBagIsEmpty() {
        cookie = new Cookie("anyOtherCookie", "any value");
        when(mockedRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        List<ItemBagDTO> items = deliveryServiceTest.getItemsFromBag();
        Assertions.assertEquals(0, items.size());
    }

    @Test
    void getItemsFromBag() {
        when(mockedRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        List<ItemBagDTO> items = deliveryServiceTest.getItemsFromBag();
        Assertions.assertEquals(2, items.size());
    }
    @Test
    void deleteItemFromBag_CookieBagIsEmpty() {
        when(mockedRequest.getCookies()).thenReturn(null);
        deliveryServiceTest.deleteItemFromBag("item1");
        verify(mockedResponse, times(1)).addCookie(any());
    }


    @Test
    void deleteItemFromBag_CookieBagIsNotEmpty() {
        when(mockedRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        deliveryServiceTest.deleteItemFromBag("item1");
        verify(mockedResponse, times(1)).addCookie(any());
    }

    @Test
    void addNewDelivery() {
        NewDeliveryDTO newDeliveryDTO = new NewDeliveryDTO();
        newDeliveryDTO.setDeliveryAddressName("address1");

        Address address = new Address();
        address.setName("address1");

        testUserEntity.setAddress(List.of(address));

        when(mockedRequest.getCookies()).thenReturn(new Cookie[]{cookie});

        Item item1 = new Item();
        item1.setName("item1");
        item1.setPrice(BigDecimal.valueOf(22));
        item1.setCountInBag(2);

        Item item2 = new Item();
        item2.setName("item2");
        item2.setPrice(BigDecimal.valueOf(22));
        item2.setCountInBag(2);

        when(mockedItemRepository.findItemByName(item1.getName())).thenReturn(item1);
        when(mockedItemRepository.findItemByName(item2.getName())).thenReturn(item2);
        when(mockedUserRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);

        deliveryServiceTest.addNewDelivery(newDeliveryDTO, testUserEntity.getUsername());

        verify(mockedResponse,times(1)).addCookie(any());
    }

    @Test
    void getDeliveriesForCurrentUser() {
        Delivery delivery1 = new Delivery();
        delivery1.setReceiveTime(LocalDateTime.now().minusMinutes(10));

        Delivery delivery2 = new Delivery();
        delivery2.setReceiveTime(LocalDateTime.now());
        Set<Delivery>deliveries = Set.of(delivery1,delivery2);
        testUserEntity.setDeliveries(deliveries);
        when(mockedUserRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);

        List<DeliveryViewDTO>deliveryViewDTOS = deliveryServiceTest.getDeliveriesForCurrentUser(testUserEntity.getUsername());
        Assertions.assertEquals(2,deliveryViewDTOS.size());
    }

    @Test
    void deleteDelivery() {
        Delivery delivery1 = new Delivery();
        delivery1.setId(1);
        delivery1.setStatus(DeliveryStatus.PENDING);
        when(mockedDeliveryRepository.findDeliveryById(1)).thenReturn(delivery1);
        deliveryServiceTest.deleteDelivery(1);
        verify(mockedDeliveryRepository,times(1)).delete(any());

    }

    @Test
    void getDeliveriesForStaff() {
        Delivery delivery1 = new Delivery();
        delivery1.setReceiveTime(LocalDateTime.now().minusMinutes(10));

        Delivery delivery2 = new Delivery();
        delivery2.setReceiveTime(LocalDateTime.now());
        Set<Delivery>deliveries = Set.of(delivery1,delivery2);
        testUserEntity.setDeliveries(deliveries);
        when(mockedDeliveryRepository.findAll()).thenReturn(List.of(delivery1,delivery2));

        List<DeliveryViewDTO>deliveryViewDTOS = deliveryServiceTest.getDeliveriesForStaff();
        Assertions.assertEquals(2,deliveryViewDTOS.size());
    }

    @Test
    void addToHistory() {
        Delivery delivery1 = new Delivery();
        delivery1.setId(1);
        delivery1.setStatus(DeliveryStatus.ACCEPTED);;

        when(mockedDeliveryRepository.findDeliveryById(1)).thenReturn(delivery1);

        deliveryServiceTest.addToHistory(1,"DELIVERED");
        verify(mockedDeliveryRepository,times(1)).saveAndFlush(any());
    }

    @Test
    void getActiveDeliveriesNames() {
        Assertions.assertEquals(4,deliveryServiceTest.getActiveDeliveriesNames().size());
    }

    @Test
    void changeStatus() {
        Delivery delivery1 = new Delivery();
        delivery1.setId(1);
        delivery1.setStatus(DeliveryStatus.ACCEPTED);;

        when(mockedDeliveryRepository.findDeliveryById(1)).thenReturn(delivery1);
        deliveryServiceTest.changeStatus(1,"DELIVERED");
        ArgumentCaptor<Delivery> catcher = ArgumentCaptor.forClass(Delivery.class);
        verify(mockedDeliveryRepository,times(1)).saveAndFlush(catcher.capture());
        Assertions.assertEquals(DeliveryStatus.DELIVERED,catcher.getValue().getStatus());
    }

    @Test
    void getSizeOfActiveDeliveries() {
        List<DeliveryStatus> deliveryStatuses = List.of(PENDING,DeliveryStatus.ACCEPTED,DeliveryStatus.TRAVELLING,DeliveryStatus.FEEDBACK);
        deliveryStatuses.forEach(deliveryStatus -> {
            Delivery newDelivery = new Delivery();
            newDelivery.setStatus(deliveryStatus);
            when(mockedDeliveryRepository.findDeliveriesByStatus(deliveryStatus)).thenReturn(List.of(newDelivery));
        });

        Assertions.assertEquals(4,deliveryServiceTest.getSizeOfActiveDeliveries().size());
    }

    @Test
    void clearStaffDeliveryHistory() {
        Delivery delivery1 = new Delivery();
        delivery1.setActive(false);

        Delivery delivery2 = new Delivery();
        delivery2.setActive(false);

        when(mockedDeliveryRepository.findAll()).thenReturn(List.of(delivery1,delivery2));

        deliveryServiceTest.clearStaffDeliveryHistory();
        verify(mockedDeliveryRepository,times(1)).saveAllAndFlush(any());
    }
}