package source.restaurant_web_project.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.entity.Delivery;
import source.restaurant_web_project.repositories.DeliveryRepository;
import source.restaurant_web_project.repositories.ReservationRepository;
import source.restaurant_web_project.repositories.TokenRepository;

import java.util.stream.Collectors;

@Component
public class ScheduleTasks {
    private final TokenRepository tokenRepository;
    private final DeliveryRepository deliveryRepository;
    private final ReservationRepository reservationRepository;

    public ScheduleTasks(TokenRepository tokenRepository, DeliveryRepository deliveryRepository, ReservationRepository reservationRepository) {
        this.tokenRepository = tokenRepository;
        this.deliveryRepository = deliveryRepository;
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanTokens(){
        tokenRepository.deleteAll(tokenRepository.findAll());
    }

    @Scheduled(cron = "0 10 3 * * ?")
    public void cleanPendingDeliveries(){
    }

}
