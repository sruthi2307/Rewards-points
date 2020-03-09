package com.example.RewardPoints.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponse {
    private List<RewardResponseEntity> rewardResponseEntityList = Collections.synchronizedList(new ArrayList<>());

}


