package com.haui.web_demo.Service;

import com.haui.web_demo.responsity.Discountresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
    @Autowired
    private Discountresponsity discountresponsity;

    public void detelediscount(Long id){
        discountresponsity.deleteById(id);
    }
}
