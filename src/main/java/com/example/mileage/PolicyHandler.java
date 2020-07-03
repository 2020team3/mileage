package com.example.mileage;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler {

    @Autowired
    MileageRepository mileageRepository;

    @StreamListener(Processor.INPUT)
    public void onEventListen(@Payload PaymentApproved paymentApproved){

        if( PaymentApproved.class.getSimpleName().equals(paymentApproved.getEventType()) ){     
            System.out.println("=========결제요청 =========");
            System.out.println(paymentApproved.getEventType());
            
            Optional<Mileage> mopt = mileageRepository.findByPurchaseId(paymentApproved.getPurchaseId());
            if(mopt.isPresent()) {
            	Mileage mileage =  mopt.get();
            	mileage.setPurchaseId(paymentApproved.getPurchaseId());
            	mileage.setMileage(mileage.getMileage()+paymentApproved.getMileage());
            	mileageRepository.save(mileage);
            }else {
            	Mileage mileage =  new Mileage();
            	mileage.setPurchaseId(paymentApproved.getPurchaseId());
            	mileage.setMileage(1000);
            	mileageRepository.save(mileage);
            }
            
            System.out.println("==================");
        }
    }
    
    @StreamListener(Processor.INPUT)
    public void onEventListen(@Payload PaymentCancelled paymentCancelled){

        if( PaymentCancelled.class.getSimpleName().equals(paymentCancelled.getEventType()) ){
            System.out.println("=========결제취소 =========");          
            System.out.println(paymentCancelled.getEventType());
            mileageRepository.findByPurchaseId(paymentCancelled.getPurchaseId())
            .ifPresent(
                    mileage -> {
                    	mileage.setPurchaseId(paymentCancelled.getPurchaseId());
                    	mileage.setMileage(mileage.getMileage()-paymentCancelled.getMileage());
                    	mileageRepository.save(mileage);
                    }
            );
            System.out.println("==================");
        }
    }
}


