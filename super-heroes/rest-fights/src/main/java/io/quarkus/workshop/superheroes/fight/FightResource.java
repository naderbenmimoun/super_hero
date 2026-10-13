package io.quarkus.workshop.superheroes.fight;

import org.jboss.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Timeout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/fights")
@Produces(APPLICATION_JSON)
@ApplicationScoped
public class FightResource {

    @Inject
    Logger logger;

    @Inject
    FightService service;

    @ConfigProperty(name = "process.milliseconds", defaultValue = "0")
    long tooManyMilliseconds;

    private void veryLongProcess() {
        try {
            Thread.sleep(tooManyMilliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GET
    @Path("/randomfighters")
    @Timeout(500)
    public Response getRandomFighters() {
        veryLongProcess();
        Fighters fighters = service.findRandomFighters();
        logger.debug("Get random fighters " + fighters);
        return Response.ok(fighters).build();
    }

    @GET
    public Response getAllFights() {
        List<Fight> fights = service.findAllFights();
        logger.debug("Total number of fights " + fights.size());
        return Response.ok(fights).build();
    }

    @GET
    @Path("/{id}")
    public Response getFight(@PathParam("id") Long id) {
        Fight fight = service.findFightById(id);
        if (fight != null) {
            logger.debug("Found fight " + id);
            return Response.ok(fight).build();
        }

        logger.debug("No fight found with id " + id);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Fight fight(@Valid Fighters fighters) {
        return service.persistFight(fighters);
    }

    @POST
    @Path("/narrate")
    @Consumes(APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response narrateFight(@Valid Fight fight) {
        logger.debug("Narrate the fight " + fight);
        String narration = service.narrateFight(fight);
        return Response.status(Response.Status.CREATED).entity(narration).build();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Fight Resource";
    }
}
