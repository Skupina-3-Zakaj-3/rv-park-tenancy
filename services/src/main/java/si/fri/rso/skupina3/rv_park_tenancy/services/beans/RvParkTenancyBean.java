package si.fri.rso.skupina3.rv_park_tenancy.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.skupina3.lib.RvParkTenancy;
import si.fri.rso.skupina3.rv_park_tenancy.models.converters.RvParkTenancyConverter;
import si.fri.rso.skupina3.rv_park_tenancy.models.entities.RvParkTenancyEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class RvParkTenancyBean {

    private final Logger log = Logger.getLogger(RvParkTenancyBean.class.getName());

    @Inject
    private EntityManager em;

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

    public RvParkTenancy createRvParkTenancy(RvParkTenancy bill) {

        RvParkTenancyEntity rvParkTenancyEntity = RvParkTenancyConverter.toEntity(bill);

        try {
            beginTx();
            em.persist(rvParkTenancyEntity);
            commitTx();
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

        RvParkTenancyEntity billEntity = em.find(RvParkTenancyEntity.class, parkTenancyId);

        if (billEntity == null) {
            return null;
        }

        RvParkTenancyEntity updatedBillEntity = RvParkTenancyConverter.toEntity(parkTenancy);

        try {
            beginTx();
            updatedBillEntity.setPark_tenancy_id(parkTenancy.getPark_tenancy_id());
            updatedBillEntity = em.merge(updatedBillEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return RvParkTenancyConverter.toDto(updatedBillEntity);
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
