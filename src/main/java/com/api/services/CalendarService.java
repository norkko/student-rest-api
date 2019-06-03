package com.api.services;

import com.api.controllers.exceptions.BadRequestException;
import com.api.controllers.exceptions.InternalErrorException;
import com.api.controllers.exceptions.NotFoundException;
import com.api.models.CalendarModel;
import com.api.repositories.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    /**
     * Method which returns all registered calendar events
     *
     * @return Iterable<CalendarModel> collection of all calendar events
     */
    public @ResponseBody Iterable<CalendarModel> getAllEvents() {
        return calendarRepository.findAll();
    }

    /**
     * Method which returns a specific event
     *
     * @return CalendarModel specific calendar event
     */
    public @ResponseBody CalendarModel getSpecificEvent(int id) throws NotFoundException {
        try {
            if (calendarRepository.findById(id) == null) throw new NotFoundException();
            return calendarRepository.findById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Calendar event not found");
        } catch (Exception e) {
            throw new BadRequestException("Bad Request: " + e);
        }
    }

    /**
     * Method which removes a specific calendar event
     *
     * @param id the event id
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity removeEvent(int id) throws NotFoundException {
        try {
            if (calendarRepository.findById(id) == null) throw new NotFoundException();
            calendarRepository.deleteById(id);
            return new ResponseEntity<>("Event successfully removed", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Comment not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * POST /calendar/add
     * Method which adds a specific calendar event.
     *
     * @param event - JSON body containing new calendar event
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity setEvent(@RequestBody CalendarModel event) throws BadRequestException {
        try {
            calendarRepository.save(event);
            return new ResponseEntity<>("Event successfully created", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Comment not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which updates an existing calendar event
     *
     * @param eventId which calendar event to update
     * @param event JSON body containing calendar updated event data
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity updateEvent(int eventId, @RequestBody CalendarModel event) throws NotFoundException {
        try {
            if (calendarRepository.findById(eventId) == null) throw new NotFoundException();
            CalendarModel oldEvent = calendarRepository.findById(eventId);
            oldEvent.setCreatedAt(event.getCreatedAt());
            oldEvent.setExpiresAt(event.getExpiresAt());
            oldEvent.setTitle(event.getTitle());
            oldEvent.setDescription(event.getDescription());
            calendarRepository.save(oldEvent);
            return new ResponseEntity<>("Event successfully updated", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Comment not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }
}
