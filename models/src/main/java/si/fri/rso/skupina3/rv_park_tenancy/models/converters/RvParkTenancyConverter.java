package si.fri.rso.skupina3.rv_park_tenancy.models.converters;

import si.fri.rso.skupina3.rv_park_tenancy.models.entities.RvParkTenancyEntity;
import si.fri.rso.skupina3.lib.RvParkTenancy;

public class RvParkTenancyConverter {

    public static RvParkTenancy toDto(RvParkTenancyEntity entity) {

        RvParkTenancy dto = new RvParkTenancy();
        dto.setPark_tenancy_id(entity.getPark_tenancy_id());
        dto.setRv_park_bill_id(entity.getRv_park_bill_id());
        dto.setUser_id(entity.getUser_id());
        dto.setStart_date(entity.getStart_date());
        dto.setEnd_date(entity.getEnd_date());

        return dto;
    }

    public static RvParkTenancyEntity toEntity(RvParkTenancy dto) {

        RvParkTenancyEntity entity = new RvParkTenancyEntity();
        entity.setPark_tenancy_id(dto.getPark_tenancy_id());
        entity.setRv_park_bill_id(dto.getRv_park_bill_id());
        entity.setUser_id(dto.getUser_id());
        entity.setStart_date(dto.getStart_date());
        entity.setEnd_date(dto.getEnd_date());

        return entity;

    }
}
