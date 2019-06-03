package com.api.controllers;

import com.api.controllers.exceptions.BadRequestException;
import com.api.controllers.exceptions.NotFoundException;
import com.api.models.CalendarModel;
import com.api.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    /**
     * GET /calendar
     * Method which returns all registered calendar events
     *
     * @return Iterable<CalendarModel> collection of all calendar events
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public @ResponseBody Iterable<CalendarModel> getAllEvents() {
        return calendarService.getAllEvents();
    }

    /**
     * GET /calendar/{id}
     * Method which returns a specific event
     *
     * @return CalendarModel specific calendar event
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/{id}")
    public @ResponseBody CalendarModel getSpecificEvent(@PathVariable(value = "id") int id) throws NotFoundException {
        return calendarService.getSpecificEvent(id);
    }

    /**
     * DELETE /calendar/remove/{id}
     * Method which removes a specific calendar event
     *
     * @param id the event id
     * @return ResponseEntity the response
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/remove/{id}")
    public @ResponseBody ResponseEntity removeEvent(@PathVariable(value = "id") int id) throws NotFoundException {
        return calendarService.removeEvent(id);
    }

    /**
     * POST /calendar/add
     * Method which adds a specific calendar event.
     *
     * @param event - JSON body containing new calendar event.
     * @return ResponseEntity the response
     * @throws BadRequestException
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity setEvent(@RequestBody CalendarModel event) throws BadRequestException {
        return calendarService.setEvent(event);
    }

    /**
     * PUT /calendar/update/{id}
     * Method which updates an existing calendar event
     *
     * @param eventId which calendar event to update
     * @param event JSON body containing calendar updated event data
     * @return ResponseEntity the response
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity updateEvent(@PathVariable(value = "id") int eventId, @RequestBody CalendarModel event) throws NotFoundException {
        return calendarService.updateEvent(eventId, event);
    }

}