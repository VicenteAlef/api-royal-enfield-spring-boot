package com.vicentedev.api_re.mapper;

import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.dto.response.TechnicalSpecResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.TechnicalSpec;
import org.springframework.stereotype.Component;

@Component
public class TechnicalSpecMapper {

    public TechnicalSpec toEntity(TechnicalSpecRequest request, Motorcycle motorcycle) {
        if (request == null) {
            return null;
        }

        return TechnicalSpec.builder()
                .motorcycle(motorcycle)
                .powerHp(request.powerHp())
                .torqueNm(request.torqueNm())
                .weightKg(request.weightKg())
                .fuelCapacityL(request.fuelCapacityL())
                .seatHeightMm(request.seatHeightMm())
                .transmission(request.transmission())
                .frontBrake(request.frontBrake())
                .rearBrake(request.rearBrake())
                .coolingSystem(request.coolingSystem())
                .build();
    }

    public void updateEntityFromRequest(TechnicalSpecRequest request, TechnicalSpec spec) {
        if (request == null || spec == null) {
            return;
        }

        spec.setPowerHp(request.powerHp());
        spec.setTorqueNm(request.torqueNm());
        spec.setWeightKg(request.weightKg());
        spec.setFuelCapacityL(request.fuelCapacityL());
        spec.setSeatHeightMm(request.seatHeightMm());
        spec.setTransmission(request.transmission());
        spec.setFrontBrake(request.frontBrake());
        spec.setRearBrake(request.rearBrake());
        spec.setCoolingSystem(request.coolingSystem());
    }

    public TechnicalSpecResponse toResponse(TechnicalSpec entity) {
        if (entity == null) {
            return null;
        }

        return new TechnicalSpecResponse(
                entity.getId(),
                entity.getPowerHp(),
                entity.getTorqueNm(),
                entity.getWeightKg(),
                entity.getFuelCapacityL(),
                entity.getSeatHeightMm(),
                entity.getTransmission(),
                entity.getFrontBrake(),
                entity.getRearBrake(),
                entity.getCoolingSystem(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
