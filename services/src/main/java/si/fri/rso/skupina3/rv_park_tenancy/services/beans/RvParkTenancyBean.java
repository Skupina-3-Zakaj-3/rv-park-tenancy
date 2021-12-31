package si.fri.rso.skupina3.rv_park_tenancy.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.skupina3.lib.BillDto;
import si.fri.rso.skupina3.lib.ParkDto;
import si.fri.rso.skupina3.lib.RvParkTenancy;
import si.fri.rso.skupina3.rv_park_tenancy.models.converters.RvParkTenancyConverter;
import si.fri.rso.skupina3.rv_park_tenancy.models.entities.RvParkTenancyEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class RvParkTenancyBean {

    private final Logger log = Logger.getLogger(RvParkTenancyBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;
    private String billBaseUrl;
    private String parkBaseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
//        billBaseUrl = "http://localhost:8087/v1/park_bills";
//        parkBaseUrl = "http://localhost:8081/v1/parks";
        billBaseUrl = "http://20.72.172.42/billing/v1/park_bills";
        parkBaseUrl = "http://20.72.172.42/parks/v1/parks";
    }

    public List<RvParkTenancy> getRvParkTenancies(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, RvParkTenancyEntity.class, queryParameters).stream()
                .map(RvParkTenancyConverter::toDto).collect(Collectors.toList());

    }

    public RvParkTenancy getRvParkTenancy(Integer tenancyId) {
        RvParkTenancyEntity parkTenancyEntity = em.find(RvParkTenancyEntity.class, tenancyId);

        if (parkTenancyEntity == null) {
            throw new NotFoundException();
        }

        return RvParkTenancyConverter.toDto(parkTenancyEntity);
    }

    public RvParkTenancy createRvParkTenancy(RvParkTenancy rvParkTenancy) {

        RvParkTenancyEntity rvParkTenancyEntity = RvParkTenancyConverter.toEntity(rvParkTenancy);

        try {
            beginTx();
            em.persist(rvParkTenancyEntity);
            commitTx();
            ParkDto park = httpClient
                    .target(String.format("%s/%d", parkBaseUrl, rvParkTenancy.getPark_id()))
                    .request()
                    .get(ParkDto.class);

            BillDto bill = new BillDto();
            bill.setPayer_id(rvParkTenancy.getUser_id());
            bill.setReceiver_id(park.getUser_id());
            bill.setPark_id(park.getRv_park_id());

            LocalDate startDate = rvParkTenancy.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = rvParkTenancy.getEnd_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long reservationDays = ChronoUnit.DAYS.between(startDate, endDate);
            reservationDays = reservationDays == 0 ? 1 : reservationDays;
            log.info(String.valueOf(reservationDays));
            float price = reservationDays * park.getCost_per_day();
            bill.setPrice(price);
            bill.setReservation_id(rvParkTenancyEntity.getPark_tenancy_id());

            Response response = httpClient
                    .target(billBaseUrl)
                    .request().post(Entity.json(bill));

            BillDto createdBill = response.readEntity(BillDto.class);
            rvParkTenancyEntity.setRv_park_bill_id(createdBill.getBill_id());
            updateRvParkTenancy(rvParkTenancyEntity.getPark_tenancy_id(),
                    RvParkTenancyConverter.toDto(rvParkTenancyEntity));

        }
        catch (Exception e) {
            rollbackTx();
        }

        if (rvParkTenancyEntity.getPark_tenancy_id() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return RvParkTenancyConverter.toDto(rvParkTenancyEntity);
    }

    public RvParkTenancy updateRvParkTenancy(Integer parkTenancyId, RvParkTenancy parkTenancy) {

        RvParkTenancyEntity rvParkTenancy = em.find(RvParkTenancyEntity.class, parkTenancyId);

        if (rvParkTenancy == null) {
            return null;
        }

        RvParkTenancyEntity updatedParkTenancyEntity = RvParkTenancyConverter.toEntity(parkTenancy);

        try {
            beginTx();
            updatedParkTenancyEntity.setPark_tenancy_id(parkTenancy.getPark_tenancy_id());
            updatedParkTenancyEntity = em.merge(updatedParkTenancyEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return RvParkTenancyConverter.toDto(updatedParkTenancyEntity);
    }

    public boolean deleteRvParkTenancy(Integer parkTenancyId) {

        RvParkTenancyEntity rvParkTenancyEntity = em.find(RvParkTenancyEntity.class, parkTenancyId);

        if (rvParkTenancyEntity != null) {
            try {
                beginTx();
                em.remove(rvParkTenancyEntity);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
