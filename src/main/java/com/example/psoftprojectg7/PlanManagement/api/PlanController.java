package com.example.psoftprojectg7.PlanManagement.api;

import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.model.PriceHistory;
import com.example.psoftprojectg7.PlanManagement.services.PlanService;
import com.example.psoftprojectg7.SubscriptionManagement.api.Helper;
import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.UserManagement.model.User;
import com.example.psoftprojectg7.UserManagement.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Tag(name = "Plans", description = "Endpoints for managing plans")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")

public class PlanController {

    private final PlanService planService;

    private final PlanMapper planMapper;
    private final UserRepository userRepository;

    @Autowired
    private Helper helper;


    //get list of plans
    @Operation(summary = "Gets all plans")
    @GetMapping()
    @RolesAllowed(Role.NEW_COSTUMER)
    public Iterable<PlanDTO> getAllPlans(
            HttpServletRequest request
    ) {
        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=NEW_COSTUMER)";

        if (firstRole.toString().equals(role)) {

            Plan plan = new Plan();
            if (!plan.isActive()) {
                throw new IllegalArgumentException("Plan isn't active");
            } else {
                return planMapper.toDTOList(planService.findAll());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only new costumers can get to know all existing plans!");

        }
    }

    //Creates a new plan
    @Operation(summary = "Creates a new plan")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> createPlan(
            final HttpServletRequest request,
            @Valid @RequestBody final CreatePlanRequest resource) {

        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";

        if (firstRole.toString().equals(role)) {
            final var plan = planService.createPlan(resource);

            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));


    } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Only Marketing directors can create or update a plan!");
    }
}

    @Operation(summary = "Update plan")
    @PatchMapping(value = "/{planName}")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> update(
            HttpServletRequest request,
            @PathVariable("planName") @Parameter(description = "The name of the plan to update") final String planName,
            @Valid @RequestBody final EditPlanRequest resource) {

        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";

        if (firstRole.toString().equals(role)) {

            final var plan = planService.update(planName, resource);
            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can update a plan!");
        }
    }

    @Operation(summary = "Deactivate a subscription")
    @PutMapping(value = "/{planName}/deactivate")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlanDTO> deactivatePlan(
            HttpServletRequest request,
            @PathVariable String planName
    ) {
        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {

            final Plan plan = planService.cancel(planName);

            final var newPlanUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(plan.getPlanName())
                    .build().toUri();

            return ResponseEntity.created(newPlanUri).eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can get a deactivate a plan!");
        }
    }

    @Operation(summary = "Cease plan")
    @PatchMapping(value = "/{planName}/archive")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> ceasePlan(
            HttpServletRequest request,
            @PathVariable("planName") @Parameter(description = "The name of the plan to cease") final String planName) {
        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {

            final var plan = planService.cease(planName);
            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can get a cease a plan!");
        }
    }

    @Operation(summary = "Update price of an existing plan")
    @PatchMapping(value = "/{planName}/updatePrice")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<PlanDTO> updatePrice(
            HttpServletRequest request,
            @PathVariable("planName") @Parameter(description = "The name of the plan to update") final String planName,
            @Valid @RequestBody final EditPriceRequest resource) {

        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {

            final var plan = planService.updatePrice(planName, resource, user.getUsername());
            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can update a plan!");
        }
    }

    @Operation(summary = "Promote a plan")
    @PutMapping(value = "/{planName}")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlanDTO> promotePlan(
            HttpServletRequest request,
            @PathVariable ("planName") @Parameter (description = "The name does not exist") final String planName) {
        long Id = helper.getUserByToken(request);

        User user = userRepository.getById(Id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {
            final Plan plan = planService.promote(planName);
            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only marketing directors can promote a plan!");


        }
    }

    @Operation(summary = "Gets price change history of a plan")
    @GetMapping(value = "/{planName}")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<Iterable<PriceHistory>> getPriceChangeHistory(
            HttpServletRequest request,
            @PathVariable("planName") final String planName) {

        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();
        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {

            final Iterable<PriceHistory> priceHistory = planService.getPriceHistory(planName);

            //return ResponseEntity.ok().body(planMapper.toPriceHistoryDTO(priceHistory));
            return ResponseEntity.ok().body(priceHistory);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only Marketing directors can get a plan's price changing history!");
        }

    }
}



