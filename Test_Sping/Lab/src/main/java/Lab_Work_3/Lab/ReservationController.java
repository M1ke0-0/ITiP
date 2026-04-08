package Lab_Work_3.Lab;

import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {
    
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping("/hello")
    public String getHello(){
        System.out.println("Hello_working");
        return reservationService.getHello();
    }


}