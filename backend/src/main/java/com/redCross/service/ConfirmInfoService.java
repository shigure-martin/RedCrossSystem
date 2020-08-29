package com.redCross.service;

import com.redCross.entity.ConfirmInfo;
import com.redCross.repository.ConfirmInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmInfoService extends BasicService<ConfirmInfo, Long> {

    private ConfirmInfoRepository confirmInfoRepository;

    @Autowired
    public ConfirmInfoService(ConfirmInfoRepository confirmInfoRepository) {
        super(confirmInfoRepository);
        this.confirmInfoRepository = confirmInfoRepository;
    }


}
