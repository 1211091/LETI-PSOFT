package com.example.psoftprojectg7.SubscriptionManagement.api;

import com.example.psoftprojectg7.PlanManagement.api.PlanDTO;
import com.example.psoftprojectg7.PlanManagement.api.PlanMapper;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.services.SubscriptionService;
import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.UserManagement.model.User;
import com.example.psoftprojectg7.UserManagement.repositories.UserRepository;
import com.example.psoftprojectg7.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
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
import java.io.IOException;
import java.util.*;

@Tag (name = "Subscriptions", description = "Endpoints for managing user subscriptions")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    private final PlanMapper planMapper;

    private final UserRepository userRepository;

    @Autowired
    private Helper helper;

    @Operation(summary = "Gets all subscriptions")
    @GetMapping
    @RolesAllowed(Role.USER_ADMIN)
    public Iterable<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionMapper.toDTOList(subscriptionService.findAll());
    }

    @Operation(summary = "Creates a subscription")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.NEW_COSTUMER)
    public ResponseEntity<SubscriptionDTO> createSubscription(
            HttpServletRequest request,
            @Valid @RequestBody final CreateSubscriptionRequest resource) {
        long id = helper.getUserByToken(request);

        resource.setUserId(id);
        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=NEW_COSTUMER)";

        if (firstRole.toString().equals(role)) {
            if (firstRole.toString().equals(role)) {
                Role newRole = new Role("SUBSCRIBER");
                iterator.remove();
                user.addAuthority(newRole);
            }

            final var subscription = subscriptionService.create(resource);

            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(subscription.getSubscriptionId()).toUri()).body(subscriptionMapper.toSubscriptionDTO(subscription));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only new costumers can subscribe to a plan!");
        }
    }


    @Operation(summary = "Switch plan (upgrade/downgrade)")
    @PatchMapping(value = "/switchPlan")
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<Subscription> switchPlan(
            HttpServletRequest request,
            @Valid @RequestBody final SwitchPlanRequest resource) {

        long userId = helper.getUserByToken(request);

        User user = userRepository.getById(userId);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {
            final var subscription = subscriptionService.switchPlan(resource, userId);

            return ResponseEntity.ok().body(subscription);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers can switch their plan!");
        }
    }

    @Operation(summary = "Gets a specific subscription")
    @GetMapping(value = "/{subscriptionId}")
    @RolesAllowed(Role.USER_ADMIN)
    public ResponseEntity<SubscriptionDTO> findById(
            HttpServletRequest request,
            @PathVariable("subscriptionId") final Long subscriptionId) {
        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=USER_ADMIN)";
        if (firstRole.toString().equals(role)) {
            final var subscription = subscriptionService.findOne(subscriptionId).orElseThrow(() -> new NotFoundException(Subscription.class, subscriptionId));

            return ResponseEntity.ok().eTag(Long.toString(subscription.getVersion())).body(subscriptionMapper.toSubscriptionDTO(subscription));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only admins can get a specific subscription!");
        }
    }

    @Operation(summary = "Gets a specific subscription's plan details")
    @GetMapping(value = "/planDetails")
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<PlanDTO> getPlanDetails(
            HttpServletRequest request) {
        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";
        if (firstRole.toString().equals(role)) {
            final var plan = subscriptionService.getPlanDetails(id);

            return ResponseEntity.ok().eTag(Long.toString(plan.getVersion())).body(planMapper.toPlanDTO(plan));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers can get their plan's details!");
        }
    }

    @Operation(summary = "Migrate subscribers from one plan to another")
    @PatchMapping(value = "/{planName}")
    @RolesAllowed(Role.MARKETING_DIRECTOR)
    public ResponseEntity<ArrayList<Subscription>> partialUpdate(
            HttpServletRequest request,
            @PathVariable("planName") final String planName,
            @Valid @RequestBody final MigrateSubsPlanRequest resource) {

        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=MARKETING_DIRECTOR)";
        if (firstRole.toString().equals(role)) {
            final var subscriptions = subscriptionService.migrate(planName, resource);

            return ResponseEntity.ok().body(subscriptions);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only marketing directors can migrate all users from one plan to another!");
        }
    }

    @Operation(summary = "Renew annual subscription")
    @PatchMapping
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<Subscription> renewAnnualSubscription(
            HttpServletRequest request) {

        long Id = helper.getUserByToken(request);
        User user = userRepository.getById(Id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();
        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {

            final var subscription = subscriptionService.renewAnnualSubscription(Id);

            return ResponseEntity.ok().body(subscription);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers can renew their subscription!");
        }
    }

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    @Operation(summary = "Cancel a subscription")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<SubscriptionDTO> cancelSubscription(
            HttpServletRequest request
    ) {
        long Id = helper.getUserByToken(request);

        User user = userRepository.getById(Id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {
            final Subscription subscription = subscriptionService.cancel(Id);
            return ResponseEntity.ok().eTag(Long.toString(subscription.getVersion())).body(subscriptionMapper.toSubscriptionDTO(subscription));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers can cancel their subscription!");
        }
    }

    @Operation(summary = "Reactivate a subscription")
    @PutMapping(value = "/reactivate")
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<SubscriptionDTO> ReActivateSubscription(
            HttpServletRequest request
    ) {
        long Id = helper.getUserByToken(request);

        User user = userRepository.getById(Id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {
            final Subscription subscription = subscriptionService.reActivate(Id);
            return ResponseEntity.ok().eTag(Long.toString(subscription.getVersion())).body(subscriptionMapper.toSubscriptionDTO(subscription));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers can reactivate their subscription!");
        }
    }


    @Operation(summary = "Gets funny jokes")
    @GetMapping(value = "/jokes")
    @RolesAllowed(Role.SUBSCRIBER)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> jokes(
            HttpServletRequest request
    ) throws IOException {
        {
            long Id = helper.getUserByToken(request);

            User user = userRepository.getById(Id);

            Set<Role> userRoles = user.getAuthorities();

            Iterator<Role> iterator = userRoles.iterator();

            Role firstRole = iterator.next();

            String role = "Role(authority=SUBSCRIBER)";
            if (firstRole.toString().equals(role)) {
                final String joke = subscriptionService.getJoke(Id);
                return ResponseEntity.ok().body(joke);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Only subscribers can access this information!");
            }
        }
    }

    /*@Operation(summary = "Gets weather forecast")
    @GetMapping(value = "/weather")
    @RolesAllowed(Role.SUBSCRIBER)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> weather(
            HttpServletRequest request,
            WeatherForecastRequest resource
    ) throws IOException {
        {
            long Id = helper.getUserByToken(request);

            User user = userRepository.getById(Id);

            Set<Role> userRoles = user.getAuthorities();

            Iterator<Role> iterator = userRoles.iterator();

            Role firstRole = iterator.next();

            String role = "Role(authority=SUBSCRIBER)";
            if (firstRole.toString().equals(role)) {
                final String joke = subscriptionService.getWeather(Id, resource);
                return ResponseEntity.ok().body(joke);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Only subscribers can access this information!");
            }
        }
    }*/
}
