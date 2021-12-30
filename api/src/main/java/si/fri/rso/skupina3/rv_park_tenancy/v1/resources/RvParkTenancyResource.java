package si.fri.rso.skupina3.rv_park_tenancy.v1.resources;

import si.fri.rso.skupina3.lib.RvParkTenancy;
import si.fri.rso.skupina3.rv_park_tenancy.services.beans.RvParkTenancyBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/rv_park_tenancies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RvParkTenancyResource {

    private Logger log = Logger.getLogger(RvParkTenancyResource.class.getName());

    @Inject
    private RvParkTenancyBean rvParkTenancyBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getrvParkTenancies() {

        log.info("getrvParkTenancy() - GET");
        List<RvParkTenancy> rvs = rvParkTenancyBean.getRvs(uriInfo);

        return Response.status(Response.Status.OK).entity(rvs).build();
    }
}
