package si.fri.rso.skupina3.rv_park_tenancy.v1.resources;

import si.fri.rso.skupina3.lib.RvParkTenancy;
import si.fri.rso.skupina3.rv_park_tenancy.services.beans.RvParkTenancyBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/rv-park-tenancies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RvParkTenancyResource {

    private final Logger log = Logger.getLogger(RvParkTenancyResource.class.getName());

    @Inject
    private RvParkTenancyBean rvParkTenancyBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getRvParkTenancies() {

        log.info("getRvParkTenancies() - GET");
        List<RvParkTenancy> rvs = rvParkTenancyBean.getRvParkTenancies(uriInfo);

        return Response.status(Response.Status.OK).entity(rvs).build();
    }

    @GET
    @Path("{id}")
    public Response getRvParkTenancy(@PathParam("id") Integer id) {

        log.info("getRvParkTenancy() - GET");

        RvParkTenancy rvParkTenancy = rvParkTenancyBean.getRvParkTenancy(id);

        if(rvParkTenancy == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(rvParkTenancy).build();
    }

    @POST
    public Response createRvParkTenancy(RvParkTenancy rvParkTenancy) {

        log.info("RvParkTenancy() - POST");

        if (rvParkTenancy == null || rvParkTenancy.getPark_id() == null || rvParkTenancy.getUser_id() == null ||
                rvParkTenancy.getStart_date() == null || rvParkTenancy.getEnd_date() == null
        ) {
            log.info("Some needed values are missing!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        else if (rvParkTenancy.getEnd_date().before(rvParkTenancy.getStart_date())) {
            log.info("End date must be after start date!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        else {
            rvParkTenancy = rvParkTenancyBean.createRvParkTenancy(rvParkTenancy);
        }

        return Response.status(Response.Status.OK).entity(rvParkTenancy).build();
    }

    @PUT
    @Path("{parkTenancyId}")
    public Response putRvParkTenancy(@PathParam("parkTenancyId") Integer parkTenancyId, RvParkTenancy parkTenancy) {

        log.info("putRvParkTenancy() - PUT");

        parkTenancy.setPark_tenancy_id(parkTenancyId);
        parkTenancy = rvParkTenancyBean.updateRvParkTenancy(parkTenancyId, parkTenancy);

        if (parkTenancy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(parkTenancy).build();

    }

    @DELETE
    @Path("{parkTenancyId}")
    public Response deleteRvParkTenancy(@PathParam("parkTenancyId") Integer parkTenancyId) {

        log.info("deleteRvParkTenancy() - DELETE");

        boolean deleted = rvParkTenancyBean.deleteRvParkTenancy(parkTenancyId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
