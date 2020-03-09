package com.example.RewardPoints.api;

import com.example.RewardPoints.model.RewardResponse;
import com.example.RewardPoints.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RewardsController {

    @Autowired
    RewardService rewardService;

    @RequestMapping(value = "/reward-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RewardResponse> getRewardPoints() {
        RewardResponse rewardResponse = rewardService.getRewardPoints();
        return new ResponseEntity<RewardResponse>(rewardResponse, HttpStatus.OK);
    }

}
