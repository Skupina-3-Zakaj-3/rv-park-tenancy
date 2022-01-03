package si.fri.rso.skupina3.rv_park_tenancy.models.converters;

import si.fri.rso.skupina3.lib.ParkDto;
import si.fri.rso.skupina3.lib.RvParkTenancy;
import si.fri.rso.skupina3.lib.UserReservationsDto;
import si.fri.rso.skupina3.rv_park_tenancy.models.entities.RvParkTenancyEntity;

import java.util.Date;

public class UserReservationsDtoConverter {

    public static UserReservationsDto toUserReservationDto(ParkDto parkDto, RvParkTenancyEntity parkTenancy) {
        
        UserReservationsDto dto = new UserReservationsDto();

        dto.setPark_tenancy_id(parkTenancy.getPark_tenancy_id());
        dto.setUser_id(parkTenancy.getUser_id());
        dto.setPark_id(parkTenancy.getPark_id());
        dto.setStart_date(parkTenancy.getStart_date());
        dto.setEnd_date(parkTenancy.getEnd_date());
        dto.setRv_park_bill_id(parkTenancy.getRv_park_bill_id());

        dto.setPark_owner(parkDto.getUser_id());
        dto.setPark_name(parkDto.getName());
        dto.setCost_per_day(parkDto.getCost_per_day());
        dto.setDescription(parkDto.getDescription());
        dto.setLocation(parkDto.getLocation());

        return dto;
    }
}
