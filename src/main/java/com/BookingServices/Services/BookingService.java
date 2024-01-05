package com.BookingServices.Services;


import com.BookingServices.DTOs.AddNewBankDTO;
import com.BookingServices.DTOs.BookingRequestDTO;
import com.BookingServices.DTOs.ProductListDTO;
import com.BookingServices.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final UserRepository userRepository;
    public Map<String,Object> getList(Integer userId){
        Integer isCheckExist = userRepository.checkUser(userId);
        if(isCheckExist == null){
            return null;
        }
        Map<String,Object> list = new HashMap<>();
        list.put("userId",userId);
        list.put("list",userRepository.listPaymentMethod(userId));
        return list;
    }
    public List<Map<String,Object>> getAllBanks(Integer type){
        List<Map<String,Object>> isCheckNull = userRepository.listingBank(type);
        if(isCheckNull.isEmpty()){
            return Collections.emptyList();
        }
        return userRepository.listingBank(type);
    }

    public Integer addBanks(Integer userId, AddNewBankDTO request){
         Integer user = userRepository.checkUser(userId);
         Integer bank = userRepository.checkBank(request.getBankId());
         List<String> accountNumber = userRepository.checkAccount(userId,request.getBankId());
         if(user == null || bank == null){
             return 0;
         }
        for (String i : accountNumber) {
            if(i.equals(request.getAccountNumber())){
                return 2;
            }
        }
         userRepository.insertBank(userId,request.getBankId(),request.getAccountName(),request.getAccountNumber());
         return 1;
    }

    public boolean addTicket(BookingRequestDTO data) {
        try {
            Integer userId = userRepository.checkUser(data.getUserId());
            Integer hotelId= userRepository.checkHotel(data.getHotelId());
            if(userId == null || hotelId == null){
                return false;
            }
            userRepository.insertTicket(data.getUserId(),data.getHotelId(),1,data.getCheckinDate(), data.getCheckoutDate(), data.getTexas(), data.getCoupon(), data.getNote(), data.getTotalPrice(),data.getAccountId());
            Integer Booking = userRepository.idBooking();
            boolean isCheckAddProduct = addProductList(data.getProductList(),Booking);
            if(!isCheckAddProduct){
                return false;
            }
            return true;
        }catch (Exception exception){
            System.out.println(exception);
            return false;
        }
    }
    private boolean addProductList(List<ProductListDTO> productList, Integer bookingId){
        try{
            for (ProductListDTO i: productList
                 ) {
               if(userRepository.checkRooms(i.getRoomId()) == null){
                   return false;
               }
               userRepository.insertProductList(i.getRoomId(),bookingId,i.getMaxGuest(),i.getRoomQuantity());
            }
            return true;
        }catch (Exception exception){
            System.out.println(exception);
            return false;
        }
    }
}