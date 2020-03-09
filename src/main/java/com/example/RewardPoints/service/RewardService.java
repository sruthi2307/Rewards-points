package com.example.RewardPoints.service;

import com.example.RewardPoints.model.CustomerDataInput;
import com.example.RewardPoints.model.RewardResponse;
import com.example.RewardPoints.model.RewardResponseEntity;
import com.example.RewardPoints.model.TransactionByMonth;
import com.example.RewardPoints.utils.RewardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardService {

@Autowired
    RewardUtils rewardUtils;

    public RewardResponse getRewardPoints() {
        RewardResponse finalResponse =  new RewardResponse();
        List<CustomerDataInput> customerDataInputs = rewardUtils.loadInputDataFromFile();
        return getRewardResponse(finalResponse,customerDataInputs);
    }

    //For Input Transaction, Calculate the points
    private RewardResponse getRewardResponse(RewardResponse finalResponse, List<CustomerDataInput> customerDataInputs) {
        List<RewardResponseEntity> rewardResponseEntityList = customerDataInputs.stream().map(input -> getRewardResponseEntity(input)).collect(Collectors.toList());
        finalResponse.getRewardResponseEntityList().addAll(rewardUtils.rearrangeByCustomer(rewardResponseEntityList));
        return finalResponse;
    }

    private RewardResponseEntity getRewardResponseEntity(CustomerDataInput input) {
        Double totalPointsEarned = rewardUtils.calculatePoints(input.getTransactionAmount());
        List<TransactionByMonth> listOFTransactionsByMonth = rewardUtils.getListOFTransactionsByMonth(input, totalPointsEarned);
        return new RewardResponseEntity(input.getCustomerId(),totalPointsEarned,listOFTransactionsByMonth);
    }
}
