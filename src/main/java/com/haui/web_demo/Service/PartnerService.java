package com.haui.web_demo.Service;

import com.haui.web_demo.entities.Partner;
import com.haui.web_demo.entities.Userobject;
import com.haui.web_demo.responsity.Partnerresponsity;
import com.haui.web_demo.responsity.Userresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerService {
    @Autowired
    private Partnerresponsity partnerresponsity;

    @Autowired
    private Userresponsity userresponsity;

    public Partner savepartner(Partner partner){
        return partnerresponsity.save(partner);
    }

    public void deletepartner(Long partnerID){
        partnerresponsity.deleteById(partnerID);
    }

    public List<Partner> getByUser(Long userID){
        Userobject userobject = userresponsity.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
        return partnerresponsity.findByUser(userobject);
    }

}
