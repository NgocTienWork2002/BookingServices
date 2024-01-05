package com.BookingServices.Controllers;


import com.BookingServices.DTOs.AddNewBankDTO;
import com.BookingServices.DTOs.BookingRequestDTO;
import com.BookingServices.DTOs.ResponingStatusArrayDTO;
import com.BookingServices.DTOs.ResponingStatusDTO;
import com.BookingServices.Services.BookingService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private  final BookingService bookingService;
    @GetMapping("/getlistaccount")
    public ResponingStatusDTO getAllAccount(@RequestParam("userid") Integer userId, HttpServletResponse response){
        var Services = bookingService.getList(userId);
        if (Services == null){
            response.setStatus(404);
            return new ResponingStatusDTO(response.getStatus(),null,"NOT_FOUND");
        }
        return new ResponingStatusDTO(response.getStatus(), Services,"OK");
    }

    @GetMapping("/getallbank")
    public ResponingStatusArrayDTO getAllBanks(@RequestParam("banktype") Integer banktype, HttpServletResponse response){
        List<Map<String,Object>> banks = bookingService.getAllBanks(banktype);
        if(banks.isEmpty()){
            response.setStatus(404);
            return new ResponingStatusArrayDTO(response.getStatus(),null,"Bank's Type isn't exited");
        }
        return new ResponingStatusArrayDTO(response.getStatus(),banks,"Ok");
    }

    @PostMapping("/addnewbank")
    public ResponingStatusDTO addNewBank(@RequestParam(value = "userid",required = true) Integer userid, HttpServletResponse response,@RequestBody @Valid AddNewBankDTO data){
        Integer Services = bookingService.addBanks(userid,data);
        if(Services == 1){
            return  new ResponingStatusDTO(response.getStatus(),null,"OK");
        } else if (Services == 0) {
            response.setStatus(404);
            return new ResponingStatusDTO(response.getStatus(),null,"NOT FOUND");
        }
        response.setStatus(409);
        return new ResponingStatusDTO(response.getStatus(),null,"The bank account of this bank  is existed");
    }

    @PostMapping("/order")
    public ResponingStatusDTO addBooking(@RequestBody @Valid BookingRequestDTO data, HttpServletResponse response){
        boolean dataService = bookingService.addTicket(data);
        if(dataService){
            return new ResponingStatusDTO(response.getStatus(),null,"OK");
        }
        response.setStatus(404);
        return new ResponingStatusDTO(response.getStatus(),null,"Error");
    }
}