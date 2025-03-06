package com.example.psoftprojectg7.Dashboard.api;

import com.example.psoftprojectg7.Dashboard.services.DashboardService;
import com.example.psoftprojectg7.SubscriptionManagement.api.DashboardRequest;
import com.example.psoftprojectg7.SubscriptionManagement.api.Helper;
import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.UserManagement.model.User;
import com.example.psoftprojectg7.UserManagement.repositories.UserRepository;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Tag(name = "Dashboard", description = "Endpoints for statistics")
@RestController
@RequestMapping("/api/dashboard")
@RolesAllowed({Role.MARKETING_DIRECTOR, Role.FINANCIAL_DIRECTOR})
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @Autowired
    private Helper helper;

    @Operation(summary = "Calculates cashflow of the upcoming/past 'n' months")
    @GetMapping("/cashflow")
    public ResponseEntity<CashFlowDTO> getCashFlow(
            HttpServletRequest request,
            @Valid @RequestParam ("timeline") final String timeline,
            @Valid @RequestParam("months") final int months,
            @Valid @RequestParam(value="plan") final String plan)
    {
        long id = helper.getUserByToken(request);
        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();
        Iterator<Role> iterator = userRoles.iterator();
        Role firstRole = iterator.next();
        String role = "Role(authority=PRODUCT_MANAGER)";    //TODO: add Financial Director role
        String role2 = "Role(authority=FINANCIAL_DIRECTOR)";


        if (firstRole.toString().equals(role) || firstRole.toString().equals(role2)) {
            final CashFlowDTO cashFlow = dashboardService.calculateCashFlow(timeline, months, plan);
            return ResponseEntity.ok().body(cashFlow);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only product managers may access satistical data!");
        }
    }


    // .../dashboard/calculaterevenue?plan
    @Operation(summary = "calculate the year-to-date revenue broken down by plan")
    @GetMapping("/revenue")
    public ResponseEntity<RevenueDTO> getRevenue(
            HttpServletRequest request,
            @Valid @RequestParam ("plan") final String plan)


    {
        long id = helper.getUserByToken(request);
        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();
        Iterator<Role> iterator = userRoles.iterator();
        Role firstRole = iterator.next();
        String role1 = "Role(authority=PRODUCT_MANAGER)";    //TODO: add Financial Director role
        String role2 = "Role(authority=FINANCIAL_DIRECTOR)";

        if (firstRole.toString().equals(role1) || firstRole.toString().equals(role2)) {
            final RevenueDTO revenue = dashboardService.calculateRevenue(plan);
            return ResponseEntity.ok().body(revenue);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only product managers may access satistical data!");
        }
    }


    @Operation(summary = "Gets the new subscribers and cancellations on a specific month")
    @PostMapping(value = "/subsandcancellations")
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(Role.PRODUCT_MANAGER)
    public ResponseEntity<Map<String, Integer>> dashboard(
            HttpServletRequest request,
            @Valid @RequestBody final DashboardRequest resource) {
        long Id = helper.getUserByToken(request);

        User user = userRepository.getById(Id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=PRODUCT_MANAGER)";
        if (firstRole.toString().equals(role)) {
            Map<String, Integer> counts = new HashMap<>();

            int newSubsCount = dashboardService.countNewSubscriptionsByMonth(resource.getMonth());
            counts.put("newSubscriptions", newSubsCount);


            int cancelledSubsCount = dashboardService.countCancelledSubscriptionsByMonth(resource.getMonth());
            counts.put("cancelledSubscriptions", cancelledSubsCount);

            final var newDashboardUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(counts.toString())
                    .build().toUri();

            return ResponseEntity.created(newDashboardUri).body(counts);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only product managers can access this information!");
        }

    }

}
