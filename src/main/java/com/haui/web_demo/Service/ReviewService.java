package com.haui.web_demo.Service;

import com.haui.web_demo.entities.Reviewroom;
import com.haui.web_demo.entities.Userobject;
import com.haui.web_demo.responsity.Reviewresponsity;
import com.haui.web_demo.responsity.Userresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private Reviewresponsity reviewresponsity;

    @Autowired
    private Userresponsity userresponsity;

    public List<Reviewroom> getReviewByUser(Long userID){
        Userobject userobject = userresponsity.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
        return reviewresponsity.findByUser(userobject);
    }

    public void deletereview(Long id){
        reviewresponsity.deleteById(id);
    }

    public Reviewroom savereview(Reviewroom reviewroom){
        return reviewresponsity.save(reviewroom);
    }


}
