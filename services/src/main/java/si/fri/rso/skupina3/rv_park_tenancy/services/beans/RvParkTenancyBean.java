package si.fri.rso.skupina3.rv_park_tenancy.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.skupina3.lib.RvParkTenancy;
import si.fri.rso.skupina3.rv_park_tenancy.models.converters.RvParkTenancyConverter;
import si.fri.rso.skupina3.rv_park_tenancy.models.entities.RvParkTenancyEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class RvParkTenancyBean {

    private final Logger log = Logger.getLogger(RvParkTenancyBean.class.getName());

    @Inject
    private EntityManager em;

    public List<RvParkTenancy> getRvs(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, RvParkTenancyEntity.class, queryParameters).stream()
                .map(RvParkTenancyConverter::toDto).collect(Collectors.toList());

    }
}
